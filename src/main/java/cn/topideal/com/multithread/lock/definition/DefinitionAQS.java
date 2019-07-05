package cn.topideal.com.multithread.lock.definition;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * 手写自定义非公平锁
 */
public abstract class DefinitionAQS {

    //锁状态
    protected volatile int state;
    private volatile Node head;
    private volatile Node tail;
    protected Thread exclusiveOwnerThread;


    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(), arg))
            Thread.currentThread().interrupt();
    }

    public boolean acquireQueued(Node node, int arg) {
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.prev;
                if (head == p && tryAcquire(arg)) {
                    //System.out.println(node.thread.getName() + "\t 唤醒后获得锁");
                    setHead(node);
                    p.next = null;
                    failed = false;
                    return failed;
                }
                if (compareAndSetStatus(p, 0, -1))
                    LockSupport.park(node.thread);
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    public void cancelAcquire(Node node) {
        if (node == null || head == null)
            return;

        Node h = head;
        Node n;
        Node p = null;
        for (; ; ) {
            n = h.next;
            if (node == n) {
                if (p != null) {
                    p.next = node.next;
                    node.next = null;
                    break;
                }
            }
            p = n;
        }
    }

    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.status != 0) {
                Node n = h.next;
                if (n != null && compareAndSetStatus(h, -1, 0)) {
                    LockSupport.unpark(n.thread);//唤醒head节点的下一个节点
                }
            }
            return true;
        }
        return false;
    }

    private Node addWaiter() {
        Node node = new Node(Thread.currentThread());
        Node prev = tail;
        if (prev != null) {
            node.prev = prev;
            if (compareAndSetTail(prev, node)) {
                prev.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }

    private Node enq(final Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) {
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return node;
                }
            }
        }
    }

    protected abstract boolean tryRelease(int arg);

    protected abstract boolean tryAcquire(int arg);

    static class Node {
        private Thread thread;
        private Node next;
        private Node prev;
        volatile private int status;

        public Node(Thread thread) {
            this.thread = thread;
        }

        public Node() {
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    protected final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    protected final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    protected final boolean compareAndSetStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, statusOffset, expect, update);
    }


    public Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

    public void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }

    private static final Unsafe unsafe;
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long statusOffset;

    static {
        try {

            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            stateOffset = unsafe.objectFieldOffset
                    (DefinitionAQS.class.getDeclaredField("state"));

            headOffset = unsafe.objectFieldOffset
                    (DefinitionAQS.class.getDeclaredField("head"));

            tailOffset = unsafe.objectFieldOffset
                    (DefinitionAQS.class.getDeclaredField("tail"));

            statusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("status"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
