package cn.topideal.com.multithread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁的原理
 */
public class ReadOnWriteLock extends Thread {

    ReentrantReadWriteLock lock;
    private int num;

    public ReadOnWriteLock(String name, int num, ReentrantReadWriteLock lock) {
        super(name);
        this.num = num;
        this.lock = lock;
    }

    @Override
    public void run() {
        if (num % 2 == 0)
            readLock();//偶数读锁
        else
            writeLock();//奇数写锁

    }

    private void writeLock() {
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

        ReadOnWriteLock lockA = new ReadOnWriteLock("AA", 2, lock);//读锁
        lockA.start();

        ReadOnWriteLock lockB = new ReadOnWriteLock("BB", 1, lock);//写锁
        lockB.start();

        ReadOnWriteLock lockC = new ReadOnWriteLock("CC", 4, lock);//读锁
        lockC.start();

    }

    private static void test() {
        int EXCLUSIVE_MASK = (1 << 16) - 1;
        int n = 1 << 17;
        System.out.println(n & EXCLUSIVE_MASK);
    }
}
