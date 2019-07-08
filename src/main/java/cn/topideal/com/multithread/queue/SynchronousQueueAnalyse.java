package cn.topideal.com.multithread.queue;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SynchronousQueue源码分析
 * 不存储元素的阻塞队列，也即单个元素的队列
 * <p>
 * 必须搭配put和take使用。put操作必须要配合一个take操作，否则会出现阻塞
 *
 * transfer(null,false,0) take
 * transfer(null,true,0)  poll
 * transfer(e,false,0)    put
 * transfer(e,true,0)     offer
 */
public class SynchronousQueueAnalyse<E> extends AbstractQueueAnalyse<E>
        implements BlockingQueue<E>, java.io.Serializable {

    abstract static class Transferer<E> {
        /**
         * Performs a put or take.
         *
         * @param e     if non-null, the item to be handed to a consumer;
         *              if null, requests that transfer return an item
         *              offered by producer.
         * @param timed if this operation should timeout
         * @param nanos the timeout, in nanoseconds
         * @return if non-null, the item provided or received; if null,
         * the operation failed due to timeout or interrupt --
         * the caller can distinguish which of these occurred
         * by checking Thread.interrupted.
         */
        abstract E transfer(E e, boolean timed, long nanos);
    }

    /**
     * The number of CPUs, for spin control
     */
    static final int NCPUS = Runtime.getRuntime().availableProcessors();

    /**
     * The number of times to spin before blocking in timed waits.
     * The value is empirically derived -- it works well across a
     * variety of processors and OSes. Empirically, the best value
     * seems not to vary with number of CPUs (beyond 2) so is just
     * a constant.
     */
    static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;

    /**
     * The number of times to spin before blocking in untimed waits.
     * This is greater than timed value because untimed waits spin
     * faster since they don't need to check times on each spin.
     */
    static final int maxUntimedSpins = maxTimedSpins * 16;

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices.
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Dual stack
     */
    static final class TransferStack<E> extends Transferer<E> {
        /*
         * This extends Scherer-Scott dual stack algorithm, differing,
         * among other ways, by using "covering" nodes rather than
         * bit-marked pointers: Fulfilling operations push on marker
         * nodes (with FULFILLING bit set in mode) to reserve a spot
         * to match a waiting node.
         */

        /* Modes for SNodes, ORed together in node fields */
        /**
         * Node represents an unfulfilled consumer 代表一个没有匹配的消费者节点
         * 表示消费数据的消费者
         */
        static final int REQUEST = 0;
        /**
         * Node represents an unfulfilled producer 代表一个没有匹配的生产者节点
         * 表示生产数据的生产者
         */
        static final int DATA = 1;
        /**
         * Node is fulfilling another unfulfilled DATA or REQUEST 表示一个已经匹配了一个没有匹配的生产者或消费者
         * 表示匹配另一个生产者或消费者
         */
        static final int FULFILLING = 2;

        /**
         * Returns true if m has fulfilling bit set.
         */
        static boolean isFulfilling(int m) {
            return (m & FULFILLING) != 0;
        }

        /**
         * Node class for TransferStacks.
         */
        static final class SNode {
            volatile SNode next;        // next node in stack 栈中下一个node
            volatile SNode match;       // the node matched to this 已经匹配的node
            volatile Thread waiter;     // to control park/unpark 得带的线程
            Object item;                // data; or null for REQUESTs 数据项
            int mode; //操作模式生产or消费
            // Note: item and mode fields don't need to be volatile
            // since they are always written before, and read after,
            // other volatile/atomic operations.

            SNode(Object item) {
                this.item = item;
            }

            boolean casNext(TransferStack.SNode cmp, TransferStack.SNode val) {
                return cmp == next &&
                        UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
            }

            /**
             * Tries to match node s to this node, if so, waking up thread.
             * Fulfillers call tryMatch to identify their waiters.
             * Waiters block until they have been matched.
             *
             * @param s the node to match
             * @return true if successfully matched to s
             */
            boolean tryMatch(SNode s) {
                //如果match为null则设置match为s节点
                if (match == null &&
                        UNSAFE.compareAndSwapObject(this, matchOffset, null, s)) {
                    Thread w = waiter;//如果线程空旋线程被阻塞了，这里唤醒
                    if (w != null) {    // waiters need at most one unpark
                        waiter = null;
                        LockSupport.unpark(w);
                    }
                    return true;
                }
                return match == s;//返回匹配结果
            }

            /**
             * Tries to cancel a wait by matching node to itself.
             */
            void tryCancel() {
                UNSAFE.compareAndSwapObject(this, matchOffset, null, this);
            }

            boolean isCancelled() {
                return match == this;
            }

            // Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long matchOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = SNode.class;
                    matchOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("match"));
                    nextOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }

        /**
         * The head (top) of the stack
         */
        volatile SNode head;

        boolean casHead(SNode h, SNode nh) {
            return h == head &&
                    UNSAFE.compareAndSwapObject(this, headOffset, h, nh);
        }

        /**
         * Creates or resets fields of a node. Called only from transfer
         * where the node to push on stack is lazily created and
         * reused when possible to help reduce intervals between reads
         * and CASes of head and to avoid surges of garbage when CASes
         * to push nodes fail due to contention.
         */
        static SNode snode(SNode s, Object e, SNode next, int mode) {
            if (s == null) s = new SNode(e);
            s.mode = mode;
            s.next = next;
            return s;
        }

        /**
         * Puts or takes an item.
         */
        @Override
        @SuppressWarnings("unchecked")
        E transfer(E e, boolean timed, long nanos) {
            /*
             * Basic algorithm is to loop trying one of three actions:
             *
             * 1. If apparently empty or already containing nodes of same
             *    mode, try to push node on stack and wait for a match,
             *    returning it, or null if cancelled.
             *
             * 2. If apparently containing node of complementary mode,
             *    try to push a fulfilling node on to stack, match
             *    with corresponding waiting node, pop both from
             *    stack, and return matched item. The matching or
             *    unlinking might not actually be necessary because of
             *    other threads performing action 3:
             *
             * 3. If top of stack already holds another fulfilling node,
             *    help it out by doing its match and/or pop
             *    operations, and then continue. The code for helping
             *    is essentially the same as for fulfilling, except
             *    that it doesn't return the item.
             */

            SNode s = null; // constructed/reused as needed
            int mode = (e == null) ? REQUEST : DATA;//操作类型生产者还是消费者 put or take 如果e为空，表示当前是消费者线程，否则是生产者线程

            for (; ; ) {
                SNode h = head;
                //头节点为空或者 头节点的类型等于当前操作类型
                if (h == null || h.mode == mode) {  // empty or same-mode
                    // put take == false    offer poll == true
                    //是否阻塞操作，这里需要反着理解
                    if (timed && nanos <= 0) {      // can't wait // 设置了timed并且等待时间小于等于0，表示不能等待，需要立即操作
                        if (h != null && h.isCancelled())// 头结点不为null并且头结点被取消
                            casHead(h, h.next);     // 重新设置头结点（弹出之前的头结点）
                        else  // 头结点为null或者头结点没有被取消
                            return null;
                    } else if (casHead(h, s = snode(s, e, h, mode))) {//如果当前head节点不为空，且操作类型与head节点不同，生成一个SNode结点；
                        // 将原来的head头结点设置为该结点的next结点；将head头结点设置为该结点
                        // 空旋或者阻塞直到s结点被FulFill操作所匹配
                        SNode m = awaitFulfill(s, timed, nanos);
                        if (m == s) {  // 匹配的结点为s结点（s结点被取消） // wait was cancelled
                            clean(s); // 清理s结点
                            return null;
                        }
                        if ((h = head) != null && h.next == s) // h重新赋值为head头结点，并且不为null；头结点的next域为s结点，表示有结点插入到s结点之前，完成了匹配
                            casHead(h, s.next);     // help s's fulfiller // 比较并替换head域（移除插入在s之前的结点和s结点）
                        return (E) ((mode == REQUEST) ? m.item : s.item); // 根据此次转移的类型返回元素
                    }
                    //如果head节点不是2
                } else if (!isFulfilling(h.mode)) { // try to fulfill 没有FULFILLING标记，尝试匹配
                    if (h.isCancelled())            // already cancelled // 被取消
                        casHead(h, h.next);         // pop and retry // 比较并替换head域（弹出头结点）
                    else if (casHead(h, s = snode(s, e, h, FULFILLING | mode))) { // 生成一个SNode结点；将原来的head头结点设置为该结点的next结点；将head头结点设置为该结点
                        for (; ; ) { // loop until matched or waiters disappear
                            SNode m = s.next;       // m is s's match // 保存s的next结点
                            if (m == null) {        // all waiters are gone // next域为null
                                casHead(s, null);   // pop fulfill node  // 比较并替换head域
                                s = null;           // use new node next time // 赋值s为null
                                break;              // restart main loop
                            }
                            SNode mn = m.next;  // m结点的next域
                            if (m.tryMatch(s)) { // 尝试匹配，并且成功
                                casHead(s, mn);     // pop both s and m  // 比较并替换head域（弹出s结点和m结点）
                                return (E) ((mode == REQUEST) ? m.item : s.item);//如果当前时获取操作则取当前节点的下一个节点的item，否则当前就是put操作，返回自己的item元素
                            } else                  // lost match // 匹配不成功
                                s.casNext(m, mn);   // help unlink // 比较并替换next域（弹出m结点）
                        }
                    }
                } else {                            // help a fulfiller // 头结点正在匹配
                    SNode m = h.next;               // m is h's match // 保存头结点的next域
                    if (m == null)                  // waiter is gone // next域为null
                        casHead(h, null);           // pop fulfilling node // 比较并替换head域（m被其他结点匹配了，需要弹出h）
                    else {
                        SNode mn = m.next; // next域不为null 获取m结点的next域
                        if (m.tryMatch(h))          // help match  // m与h匹配成功
                            casHead(h, mn);         // pop both h and m // 比较并替换head域（弹出h和m结点）
                        else                        // lost match // 匹配不成功
                            h.casNext(m, mn);       // help unlink // 比较并替换next域（移除m结点）
                    }
                }
            }
        }

        /**
         * Spins/blocks until node s is matched by a fulfill operation.
         *
         * @param s     the waiting node
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched node, or s if cancelled
         */
        SNode awaitFulfill(SNode s, boolean timed, long nanos) {
            /*
             * When a node/thread is about to block, it sets its waiter
             * field and then rechecks state at least one more time
             * before actually parking, thus covering race vs
             * fulfiller noticing that waiter is non-null so should be
             * woken.
             *
             * When invoked by nodes that appear at the point of call
             * to be at the head of the stack, calls to park are
             * preceded by spins to avoid blocking when producers and
             * consumers are arriving very close in time.  This can
             * happen enough to bother only on multiprocessors.
             *
             * The order of checks for returning out of main loop
             * reflects fact that interrupts have precedence over
             * normal returns, which have precedence over
             * timeouts. (So, on timeout, one last check for match is
             * done before giving up.) Except that calls from untimed
             * SynchronousQueue.{poll/offer} don't check interrupts
             * and don't wait at all, so are trapped in transfer
             * method rather than calling awaitFulfill.
             * 当多个操作如take or put操作时，即从操作队列开始连续多个take或者put操作时，这里需要注意的时为什么不是offer和poll因为他们根本到
             * 不了这一步，在上一步if (timed && nanos <= 0)判断时就已经返回null了，所以这里不考虑。这里以take操作为先，如果多个连续的take
             * 操作，还没有等来一个put操作，那么在空旋到一定次数后线程就会被挂起，put操作其实也一样，其实这样的考虑是有一定的好处的，那就时避免了
             * 线程的上下文切换
             */
            final long deadline = timed ? System.nanoTime() + nanos : 0L;// 根据timed标识计算截止时间
            Thread w = Thread.currentThread();
            // 根据s确定空旋等待的时间 spins 空转  nanos 阻塞
            int spins = (shouldSpin(s) ?//如果当前节点是否是head节点或head节点的操作类型是2
                    (timed ? maxTimedSpins : maxUntimedSpins) : 0);
            for (; ; ) {
                if (w.isInterrupted()) // 当前线程被中断
                    s.tryCancel(); // 取消s结点
                SNode m = s.match; // 获取s结点的match域
                if (m != null) // m不为null，存在匹配结点
                    return m; //到这里已经说明有put和take操作匹配上了，所以这个时候直接返回即可
                if (timed) { // 设置了timed
                    nanos = deadline - System.nanoTime(); // 确定继续等待的时间
                    if (nanos <= 0L) { // 继续等待的时间小于等于0，等待超时
                        s.tryCancel(); // 取消s结点
                        continue;
                    }
                }
                if (spins > 0)  // 空旋等待的时间大于0
                    spins = shouldSpin(s) ? (spins - 1) : 0;  // 确实是否还需要继续空旋等待
                else if (s.waiter == null) // 等待线程为null
                    s.waiter = w; // establish waiter so can park next iter // 设置waiter线程为当前线程
                else if (!timed) //没有设置timed标识
                    LockSupport.park(this);//阻塞当前线程
                else if (nanos > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanos);//指定时间阻塞
            }
        }

        /**
         * Returns true if node s is at head or there is an active
         * fulfiller.
         * 如果当前s节点是head节点，或者head节点类型是2的时候返回true
         */
        boolean shouldSpin(SNode s) {
            SNode h = head;
            return (h == s || h == null || isFulfilling(h.mode));
        }

        /**
         * Unlinks s from the stack.
         */
        void clean(SNode s) {
            s.item = null;   // forget item // s结点的item设置为null
            s.waiter = null; // forget thread // waiter域设置为null

            /*
             * At worst we may need to traverse entire stack to unlink
             * s. If there are multiple concurrent calls to clean, we
             * might not see s if another thread has already removed
             * it. But we can stop when we see any node known to
             * follow s. We use s.next unless it too is cancelled, in
             * which case we try the node one past. We don't check any
             * further because we don't want to doubly traverse just to
             * find sentinel.
             */

            SNode past = s.next; // 获取s结点的next节点
            if (past != null && past.isCancelled()) // next节点不为null并且next节点被取消
                past = past.next; // 重新设置past

            // Absorb cancelled nodes at head
            SNode p;
            while ((p = head) != null && p != past && p.isCancelled()) // 从栈顶头结点开始到past结点（不包括），将连续的取消结点移除
                casHead(p, p.next); // 比较并替换head域（弹出取消的结点）从head节点开始把所有的取消节点从队列中干掉

            // Unsplice embedded nodes
            while (p != null && p != past) { // 移除上一步骤没有移除的非连续的取消结点 这一步的意思是从整个队列中移除所有的取消节点，此处是
                // 接着上一步head节点开始后的截止节点开始遍历
                SNode n = p.next;
                if (n != null && n.isCancelled())
                    p.casNext(n, n.next);
                else
                    p = n;
            }
        }

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferStack.class;
                headOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("head"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * Dual Queue
     */
    static final class TransferQueue<E> extends Transferer<E> {
        /*
         * This extends Scherer-Scott dual queue algorithm, differing,
         * among other ways, by using modes within nodes rather than
         * marked pointers. The algorithm is a little simpler than
         * that for stacks because fulfillers do not need explicit
         * nodes, and matching is done by CAS'ing QNode.item field
         * from non-null to null (for put) or vice versa (for take).
         */

        /**
         * Node class for TransferQueue.
         */
        static final class QNode {
            volatile QNode next;          // next node in queue
            volatile Object item;         // CAS'ed to or from null
            volatile Thread waiter;       // to control park/unpark
            final boolean isData;

            QNode(Object item, boolean isData) {
                this.item = item;
                this.isData = isData;
            }

            boolean casNext(QNode cmp, QNode val) {
                return next == cmp &&
                        UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
            }

            boolean casItem(Object cmp, Object val) {
                return item == cmp &&
                        UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
            }

            /**
             * Tries to cancel by CAS'ing ref to this as item.
             */
            void tryCancel(Object cmp) {
                UNSAFE.compareAndSwapObject(this, itemOffset, cmp, this);
            }

            boolean isCancelled() {
                return item == this;
            }

            /**
             * Returns true if this node is known to be off the queue
             * because its next pointer has been forgotten due to
             * an advanceHead operation.
             */
            boolean isOffList() {
                return next == this;
            }

            // Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long itemOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = QNode.class;
                    itemOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("item"));
                    nextOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }

        /**
         * Head of queue
         */
        transient volatile QNode head;
        /**
         * Tail of queue
         */
        transient volatile QNode tail;
        /**
         * Reference to a cancelled node that might not yet have been
         * unlinked from queue because it was the last inserted node
         * when it was cancelled.
         */
        transient volatile QNode cleanMe;

        TransferQueue() {
            QNode h = new QNode(null, false); // initialize to dummy node.
            head = h;
            tail = h;
        }

        /**
         * Tries to cas nh as new head; if successful, unlink
         * old head's next node to avoid garbage retention.
         */
        void advanceHead(QNode h, QNode nh) {
            if (h == head &&
                    UNSAFE.compareAndSwapObject(this, headOffset, h, nh))
                h.next = h; // forget old next
        }

        /**
         * Tries to cas nt as new tail.
         */
        void advanceTail(QNode t, QNode nt) {
            if (tail == t)
                UNSAFE.compareAndSwapObject(this, tailOffset, t, nt);
        }

        /**
         * Tries to CAS cleanMe slot.
         */
        boolean casCleanMe(QNode cmp, QNode val) {
            return cleanMe == cmp &&
                    UNSAFE.compareAndSwapObject(this, cleanMeOffset, cmp, val);
        }

        /**
         * Puts or takes an item.
         */
        @Override
        @SuppressWarnings("unchecked")
        E transfer(E e, boolean timed, long nanos) {
            /* Basic algorithm is to loop trying to take either of
             * two actions:
             *
             * 1. If queue apparently empty or holding same-mode nodes,
             *    try to add node to queue of waiters, wait to be
             *    fulfilled (or cancelled) and return matching item.
             *
             * 2. If queue apparently contains waiting items, and this
             *    call is of complementary mode, try to fulfill by CAS'ing
             *    item field of waiting node and dequeuing it, and then
             *    returning matching item.
             *
             * In each case, along the way, check for and try to help
             * advance head and tail on behalf of other stalled/slow
             * threads.
             *
             * The loop starts off with a null check guarding against
             * seeing uninitialized head or tail values. This never
             * happens in current SynchronousQueue, but could if
             * callers held non-volatile/final ref to the
             * transferer. The check is here anyway because it places
             * null checks at top of loop, which is usually faster
             * than having them implicitly interspersed.
             */

            QNode s = null; // constructed/reused as needed
            boolean isData = (e != null);//操作类型，如果是put操作则isData为true 否则是读为false

            for (; ; ) {
                QNode t = tail;
                QNode h = head;
                if (t == null || h == null)         // saw uninitialized value
                    continue;                       // spin
                //如果当前head、tail节点是哨兵节点，或者是当天节点和tail节点的操作类型相同
                if (h == t || t.isData == isData) { // empty or same-mode
                    QNode tn = t.next; //获取tail节点的尾节点
                    if (t != tail) //其他线程修改了尾节点需要再次重新判断                 // inconsistent read
                        continue;
                    if (tn != null) {  //如果尾节点的下一个节点不为空，则说明有其他线程追加了新的尾节点，也需要再次重新判断              // lagging tail
                        advanceTail(t, tn);
                        continue;
                    }
                    if (timed && nanos <= 0)        // can't wait
                        return null;
                    if (s == null) //构建新的QNode节点
                        s = new QNode(e, isData);
                    if (!t.casNext(null, s))   //追加到tail节点后 继续重新判断    // failed to link in
                        continue;

                    advanceTail(t, s);      //设置新的尾节点        // swing tail and wait
                    //通过空旋或阻塞的方式等待新的操作，如果当前是put操作，则需要等待新的take操作，否则会一直空旋或阻塞下去，直到匹配到结果，并返回结果
                    Object x = awaitFulfill(s, e, timed, nanos);
                    if (x == s) {   //这里有线程调用了中断操作，导致匹配到的结果为自身                // wait was cancelled
                        clean(t, s);//这里需要做清除操作
                        return null;
                    }

                    if (!s.isOffList()) {  //匹配成功         // not already unlinked
                        advanceHead(t, s); //重新更新head节点         // unlink if head
                        if (x != null)              // and forget fields
                            s.item = s;
                        s.waiter = null;
                    }
                    return (x != null) ? (E) x : e;

                } else {   //到这里证明已经有其他线程操作了上述步骤，且当前操作类型与head节点的类型不同，这个时候就需要去进行匹配操作                         // complementary-mode
                    QNode m = h.next;               // node to fulfill
                    if (t != tail || m == null || h != head)
                        continue;                   // inconsistent read

                    Object x = m.item;
                    if (isData == (x != null) ||    // m already fulfilled
                            x == m ||                   // m cancelled
                            !m.casItem(x, e)) {         // lost CAS
                        advanceHead(h, m);          // dequeue and retry
                        continue;
                    }

                    advanceHead(h, m);              // successfully fulfilled
                    LockSupport.unpark(m.waiter);
                    return (x != null) ? (E) x : e;
                }
            }
        }

        /**
         * Spins/blocks until node s is fulfilled.
         *
         * @param s     the waiting node
         * @param e     the comparison value for checking match
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched item, or s if cancelled
         */
        Object awaitFulfill(QNode s, E e, boolean timed, long nanos) {
            /* Same idea as TransferStack.awaitFulfill */
            final long deadline = timed ? System.nanoTime() + nanos : 0L;
            Thread w = Thread.currentThread();
            int spins = ((head.next == s) ?
                    (timed ? maxTimedSpins : maxUntimedSpins) : 0);
            for (; ; ) {
                if (w.isInterrupted())
                    s.tryCancel(e);
                Object x = s.item;
                if (x != e)
                    return x;
                if (timed) {
                    nanos = deadline - System.nanoTime();
                    if (nanos <= 0L) {
                        s.tryCancel(e);
                        continue;
                    }
                }
                if (spins > 0)
                    --spins;
                else if (s.waiter == null)
                    s.waiter = w;
                else if (!timed)
                    LockSupport.park(this);
                else if (nanos > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanos);
            }
        }

        /**
         * Gets rid of cancelled node s with original predecessor pred.
         */
        void clean(QNode pred, QNode s) {
            s.waiter = null; // forget thread
            /*
             * At any given time, exactly one node on list cannot be
             * deleted -- the last inserted node. To accommodate this,
             * if we cannot delete s, we save its predecessor as
             * "cleanMe", deleting the previously saved version
             * first. At least one of node s or the node previously
             * saved can always be deleted, so this always terminates.
             */
            while (pred.next == s) { // Return early if already unlinked
                QNode h = head;
                QNode hn = h.next;   // Absorb cancelled first node as head
                if (hn != null && hn.isCancelled()) {
                    advanceHead(h, hn);
                    continue;
                }
                QNode t = tail;      // Ensure consistent read for tail
                if (t == h)
                    return;
                QNode tn = t.next;
                if (t != tail)
                    continue;
                if (tn != null) {
                    advanceTail(t, tn);
                    continue;
                }
                if (s != t) {        // If not tail, try to unsplice
                    QNode sn = s.next;
                    if (sn == s || pred.casNext(s, sn))
                        return;
                }
                QNode dp = cleanMe;
                if (dp != null) {    // Try unlinking previous cancelled node
                    QNode d = dp.next;
                    QNode dn;
                    if (d == null ||               // d is gone or
                            d == dp ||                 // d is off list or
                            !d.isCancelled() ||        // d not cancelled or
                            (d != t &&                 // d not tail and
                                    (dn = d.next) != null &&  //   has successor
                                    dn != d &&                //   that is on list
                                    dp.casNext(d, dn)))       // d unspliced
                        casCleanMe(dp, null);
                    if (dp == pred)
                        return;      // s is already saved node
                } else if (casCleanMe(null, pred))
                    return;          // Postpone cleaning s
            }
        }

        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;
        private static final long tailOffset;
        private static final long cleanMeOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferQueue.class;
                headOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("head"));
                tailOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("tail"));
                cleanMeOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("cleanMe"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * The transferer. Set only in constructor, but cannot be declared
     * as final without further complicating serialization.  Since
     * this is accessed only at most once per public method, there
     * isn't a noticeable performance penalty for using volatile
     * instead of final here.
     */
    private transient volatile Transferer<E> transferer;

    /**
     * Creates a {@code SynchronousQueue} with nonfair access policy.
     */
    public SynchronousQueueAnalyse() {
        this(false);
    }

    /**
     * Creates a {@code SynchronousQueue} with the specified fairness policy.
     *
     * @param fair if true, waiting threads contend in FIFO order for
     *             access; otherwise the order is unspecified.
     */
    public SynchronousQueueAnalyse(boolean fair) {
        transferer = fair ? new TransferQueue<E>() : new TransferStack<E>();//公平 队列   非公平 栈
    }

    /**
     * Adds the specified element to this queue, waiting if necessary for
     * another thread to receive it.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        if (transferer.transfer(e, false, 0) == null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * up to the specified wait time for another thread to receive it.
     *
     * @return {@code true} if successful, or {@code false} if the
     * specified waiting time elapses before a consumer appears
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {
        if (e == null) throw new NullPointerException();
        if (transferer.transfer(e, true, unit.toNanos(timeout)) != null)
            return true;
        if (!Thread.interrupted())
            return false;
        throw new InterruptedException();
    }

    /**
     * Inserts the specified element into this queue, if another thread is
     * waiting to receive it.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        return transferer.transfer(e, true, 0) != null;
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * for another thread to insert it.
     *
     * @return the head of this queue
     * @throws InterruptedException {@inheritDoc}
     */
    @Override
    public E take() throws InterruptedException {
        E e = transferer.transfer(null, false, 0);
        if (e != null)
            return e;
        Thread.interrupted();
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, waiting
     * if necessary up to the specified wait time, for another thread
     * to insert it.
     *
     * @return the head of this queue, or {@code null} if the
     * specified waiting time elapses before an element is present
     * @throws InterruptedException {@inheritDoc}
     */
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = transferer.transfer(null, true, unit.toNanos(timeout));
        if (e != null || !Thread.interrupted())
            return e;
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, if another thread
     * is currently making an element available.
     *
     * @return the head of this queue, or {@code null} if no
     * element is available
     */
    @Override
    public E poll() {
        return transferer.transfer(null, true, 0);
    }

    /**
     * Always returns {@code true}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return {@code true}
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Always returns zero.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return zero
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * Always returns zero.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return zero
     */
    @Override
    public int remainingCapacity() {
        return 0;
    }

    /**
     * Does nothing.
     * A {@code SynchronousQueue} has no internal capacity.
     */
    @Override
    public void clear() {
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param o the element
     * @return {@code false}
     */
    @Override
    public boolean contains(Object o) {
        return false;
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param o the element to remove
     * @return {@code false}
     */
    @Override
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Returns {@code false} unless the given collection is empty.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false} unless given collection is empty
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns {@code null}.
     * A {@code SynchronousQueue} does not return elements
     * unless actively waited on.
     *
     * @return {@code null}
     */
    @Override
    public E peek() {
        return null;
    }

    /**
     * Returns an empty iterator in which {@code hasNext} always returns
     * {@code false}.
     *
     * @return an empty iterator
     */
    @Override
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    /**
     * Returns an empty spliterator in which calls to
     * {@link java.util.Spliterator#trySplit()} always return {@code null}.
     *
     * @return an empty spliterator
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.emptySpliterator();
    }

    /**
     * Returns a zero-length array.
     *
     * @return a zero-length array
     */
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * Sets the zeroeth element of the specified array to {@code null}
     * (if the array has non-zero length) and returns it.
     *
     * @param a the array
     * @return the specified array
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length > 0)
            a[0] = null;
        return a;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(Collection<? super E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; (e = poll()) != null; ) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; n < maxElements && (e = poll()) != null; ) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /*
     * To cope with serialization strategy in the 1.5 version of
     * SynchronousQueue, we declare some unused classes and fields
     * that exist solely to enable serializability across versions.
     * These fields are never used, so are initialized only if this
     * object is ever serialized or deserialized.
     */

    @SuppressWarnings("serial")
    static class WaitQueue implements java.io.Serializable {
    }

    static class LifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3633113410248163686L;
    }

    static class FifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3623113410248163686L;
    }

    private ReentrantLock qlock;
    private WaitQueue waitingProducers;
    private WaitQueue waitingConsumers;

    /**
     * Saves this queue to a stream (that is, serializes it).
     *
     * @param s the stream
     * @throws java.io.IOException if an I/O error occurs
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        boolean fair = transferer instanceof TransferQueue;
        if (fair) {
            qlock = new ReentrantLock(true);
            waitingProducers = new FifoWaitQueue();
            waitingConsumers = new FifoWaitQueue();
        } else {
            qlock = new ReentrantLock();
            waitingProducers = new LifoWaitQueue();
            waitingConsumers = new LifoWaitQueue();
        }
        s.defaultWriteObject();
    }

    /**
     * Reconstitutes this queue from a stream (that is, deserializes it).
     *
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object
     *                                could not be found
     * @throws java.io.IOException    if an I/O error occurs
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (waitingProducers instanceof FifoWaitQueue)
            transferer = new TransferQueue<E>();
        else
            transferer = new TransferStack<E>();
    }

    // Unsafe mechanics
    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
        try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
        }
    }


    /**
     * transfer(null,false,0) take
     * transfer(null,true,0)  poll
     * transfer(e,false,0)    put
     * transfer(e,true,0)     offer
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        //System.out.println(blockingQueue.size());
        //System.out.println(blockingQueue.poll());
        //System.out.println(blockingQueue.add("a"));
        //System.out.println(blockingQueue.offer("b"));
        //System.out.println(blockingQueue.add("b"));

        new Thread(() -> {
            while (true) {
                try {
                    String str = UUID.randomUUID().toString().substring(0, 8);
                    blockingQueue.put(str);
                    Random random = new Random();
                    int sleep = random.nextInt(5);
                    TimeUnit.SECONDS.sleep(sleep + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "P").start();

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        //System.out.println(blockingQueue.take());
    }
}
