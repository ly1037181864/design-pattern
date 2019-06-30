package cn.topideal.com.multithread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁的原理
 */
public class ReadOnWriteLock extends Thread {

    ReentrantReadWriteLock lock;

    public ReadOnWriteLock(String name, ReentrantReadWriteLock lock) {
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.writeLock().lock();
        try {
            Thread.sleep(2000);

            System.out.println(Thread.currentThread().getName() + "\t当前线程获取锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }


    }

    private void readLock() {
        lock.readLock().lock();
        try {
            Thread.sleep(2000);

            System.out.println(Thread.currentThread().getName() + "\t当前线程获取锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        ReadOnWriteLock lockA = new ReadOnWriteLock("AA", lock);
        lockA.start();

        ReadOnWriteLock lockB = new ReadOnWriteLock("BB", lock);
        lockB.start();

        ReadOnWriteLock lockC = new ReadOnWriteLock("CC", lock);
        lockC.start();

    }

    private static void test() {
        int EXCLUSIVE_MASK = (1 << 16) - 1;
        int n = 1 << 17;
        System.out.println(n & EXCLUSIVE_MASK);
    }
}
