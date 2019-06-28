package cn.topideal.com.multithread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁原理
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
