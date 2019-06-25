package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.TimeUnit;

/**
 * Sleep方法是否是释放锁以及索取的锁是对象锁还是类锁
 * sleep方法获取到的锁取决于同步代码块或者方法上的锁的类型
 * sleep方法在释放CPU时间片后并不会释放锁资源
 */
public class SleepAndJoinDemo {
    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    /**
     * main线程结束了
     * BB尝试获取锁资源
     * BB获取了锁资源
     * AA尝试获取锁资源
     */
    public static void test3() {
        Object lock = new Object();

        Thread aa = new Thread(() -> {

            try {
                TimeUnit.MICROSECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "尝试获取锁资源");
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获取了锁资源");
            }
            System.out.println(Thread.currentThread().getName() + "释放了锁资源");
        }, "AA");
        aa.start();


        Thread bb = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "尝试获取锁资源");
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + "获取了锁资源");
                    //join方法会释放CPU时间片，但不会释放对象锁资源
                    aa.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "释放了锁资源");
        }, "BB");
        bb.start();

        System.out.println("main线程结束了");

    }

    /**
     * join方法主要用于阻塞当前线程
     */
    public static void test2() {
        Thread aa = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t线程结束了");
        }, "AA");

        aa.start();

        Thread bb = new Thread(() -> {
            try {
                //阻塞当前线程即线程BB
                aa.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t线程结束了");
        }, "BB");

        bb.start();

        System.out.println(Thread.currentThread().getName() + "\t线程结束了");
    }

    private static void test1() {
        Object lock = new Object();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t试图获得锁lock");
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + "\t获得锁lock");
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t试图获得锁lock");
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + "\t获得锁lock");
                    lock.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();
    }
}
