package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.TimeUnit;

/**
 * Java多线程基础
 * 线程的六种状态 NEW，RUNNABLE，BLOCKED，WAITING，TIMED_WAITING，TERMINATED
 * 线程创建的方式
 */
public class ThreadBasicDemo {

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();// TIMED_WAITING (sleeping)

        new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();//BLOCKED (on object monitor)


        new Thread(() -> {
            synchronized (lock2) {
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();// WAITING (on object monitor)

        new Thread(() -> {
            synchronized (lock2) {
                try {
                    lock2.wait(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();// TIMED_WAITING (on object monitor)

        new Thread(() -> {
            while (true) {

            }
        }, "E").start();// RUNNABLE
    }
}
