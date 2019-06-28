package cn.topideal.com.multithread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁原理
 *
 * ReentrantLock公平锁的实现逻辑
 * lock逻辑
 * 1 线程会去尝试获取一次锁，如果锁的状态为0则会去判断同步队列中是否存在等待的线程，如果存在则当前线程获取锁失败，否则当前线程获取锁成功，并独占该锁
 * 2 当锁的状态不为0时，意味着有其他线程已经占有锁，那么如果占有锁的线程是但前线程，则状态加1，实现可重入，否则获取锁失败
 * 3 当尝试获取锁失败后，后构建一个Note节点对象，属于线程独占模式，如果同步队列已经存在则将构建好的Note节点加入队尾，否则对同步队列进行初始化，构建
 * head节点并将新构建的Note节点加入队尾
 * 4 再次判断如果当前note节点的前一个节点是head节点，第一中是先前被阻塞的线程被唤醒后的操作，第二就是head节点首次被获取锁失败的线程，这个时候，仍然
 * 回去再次尝试获取锁
 * 5 如果是之前被阻塞的线程唤醒后再次尝试获取锁时，此时一定会成功
 * 6 如果是首次获取锁失败，如果占有锁的线程此时释放了锁，那么此次重新获取锁有可能成功
 * 7 如果再次尝试获取锁成功，则会将当前note节点设置为head节点，加锁的逻辑结束
 * 8 如果是首次获取锁失败，如果再次尝试获取锁失败，那么就会将当前note节点的前一个节点的waitState状态设置为-1并挂起当前线程
 *
 * unlock逻辑
 * 1 线程首先会去尝试释放锁，如果释放的锁是重入锁，这个时候，就需要多次重复调用unlock，如果释放锁成功，即state状态复位为0
 * 2 此时会去判断head节点是否为空，或者它的状态是否不等于0，如果头节点不存在意味着同步队列不存在，也就意味这不存在阻塞的线程，这个时候就不需要再次
 * 唤醒同步队列中的线程去获取锁这个动作。如果头节点不为空，且头节点的状态不等0，就意味着同步队列中一定存在阻塞线程，这时释放锁时一定需要唤醒他们，这
 * 里需要注意的是，因为线程尝试获取锁失败后，挂起线程时，会将当前线程所在的note节点的前一个节点的waitState状态设置为-1，所以此处就需要判断head节
 * 点是否不等于0，最终唤醒同步队列中的等待线程，解锁逻辑完成
 *
 * 被唤醒的线程的逻辑
 * 1 再次进入循环，获取note节点的头节点，此时就是head节点，因为唤醒的就是head节点的下一个节点
 * 2 此时会去再次尝试获取锁，这时由于上一个线程已经释放了锁，这时state一定为0，再次判断同步队列中是否存在等待线程，由于此次唤醒的是head节点的下一个
 * 节点，h != t && ((s = h.next) == null || s.thread != Thread.currentThread()); 头节点不等于尾节点成立，但是h.next一定不为空，且
 * s.thread一定是当前线程，所以此时返回的是false，也就是当前被唤醒的线程会去持有锁，并当前的note节点设置为head节点，最后lock流程结束
 *
 */
public class FailLock extends Thread {
    private Lock lock;
    private String name;

    public FailLock(Lock lock, String name) {
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获得锁");
            //睡眠
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock(true);
        new FailLock(lock, "AA").start();

        new FailLock(lock, "BB").start();


    }
}
