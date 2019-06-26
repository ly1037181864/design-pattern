package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.TimeUnit;

/**
 * yied方法在执行的过程中会释放掉CPU资源，但不会释放锁资源
 * AA	尝试获取锁
 * AA	获得了锁
 * BB	尝试获取锁
 * <p>
 * 换成wait方法
 * AA	尝试获取锁
 * AA	获得了锁
 * BB	尝试获取锁
 * BB	获得了锁
 * <p>
 * 通过对比得出yied方法在执行的过程中会释放掉CPU资源，但不会释放锁资源
 */
public class YiedDemo {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t尝试获取锁");
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "\t获得了锁");
                while (true) {
                    //Thread.yield();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "AA").start();

        TimeUnit.MICROSECONDS.sleep(200);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t尝试获取锁");
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "\t获得了锁");
            }
        }, "BB").start();
    }
}
