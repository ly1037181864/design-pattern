package cn.topideal.com.multithread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock非公平锁的底层原理
 * 非公平锁和公平锁的流程差不多，就少了一个环境就是在尝试获取锁的时候，少了一个判断同步队列中是否还存在等待线程的过程
 * <p>
 * 释放锁的流程两者一样
 */
public class NonfairLock extends Thread {

    Lock lock;

    public NonfairLock(String name, Lock lock) {
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        NonfairLock ta = new NonfairLock("AA", lock);
        ta.start();

        NonfairLock tb = new NonfairLock("BB", lock);
        tb.start();
    }
}
