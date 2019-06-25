package cn.topideal.com.multithread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 线程中断的掩饰案例
 * 线程中断仅仅只是一个标识位，仅且表示当前线程收到过中断请求而已，如果程序逻辑中不对此中断作出响应，线程并不会进行中断操作
 * 在NEW、TERMINATED状态下，线程会无视对其进行的中断操作
 * 在BLOCK状态下，线程中断状态设置有效
 * 在WAITING、TIME_WAITING状态下进行中断设置操作会抛出中断异常，并对已进行的中断操作进行复位
 * 在RUNNABLE状态下，线程中断状态设置有效
 */
public class InterruptDemo extends Thread {

    public InterruptDemo(String name) {
        super(name);
    }

    public static void main(String[] args) {
        //threadStateInNew();
        //threadStateInTerminated();
        //threadStateInBlock();
        //threadStateInWait();
        interruptException();
    }

    /**
     * GG	当前线程的状态：RUNNABLE	 异常内当前线程的中断状态：false
     * GG	当前线程的状态：RUNNABLE	 释放锁后当前线程的中断状态：false
     * HH	当前线程的状态：RUNNABLE	 异常内当前线程的中断状态：false
     * HH	当前线程的状态：RUNNABLE	 释放锁后当前线程的中断状态：false
     * 从执行结果中可以看到，当线程处于WAITING/TIME_WAITING状态下被中断后，处于阻塞状态下的线程会自动唤醒，并将已设置的中断标识复位
     */
    public static void interruptException() {
        Object lock = new Object();
        Thread gg = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 当前线程的中断状态：" + currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 异常内当前线程的中断状态：" + currentThread().isInterrupted());
                }
            }
            System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 释放锁后当前线程的中断状态：" + currentThread().isInterrupted());
        }, "GG");

        gg.start();


        try {
            TimeUnit.MICROSECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gg.interrupt();

        Thread hh = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(30);
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 当前线程的中断状态：" + currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 异常内当前线程的中断状态：" + currentThread().isInterrupted());
                }
            }
            System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 释放锁后当前线程的中断状态：" + currentThread().isInterrupted());
        }, "HH");

        hh.start();

        try {
            TimeUnit.MICROSECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hh.interrupt();

    }

    /**
     * NEW状态下中断线程 false
     */
    public static void threadStateInNew() {
        InterruptDemo demo = new InterruptDemo("AA");
        //New一个线程的初始状态为NEW
        System.out.println(demo.getName() + "\t当前线程的状态：" + demo.getState() + "\t 当前线程的中断状态：" + demo.isInterrupted());
        demo.interrupt();
        demo.start();

        System.out.println(demo.getName() + "\t当前线程的状态：" + demo.getState() + "\t 当前线程的中断状态：" + demo.isInterrupted());
    }

    /**
     * TERMINATED 状态下中断线程 false
     */
    public static void threadStateInTerminated() {
        InterruptDemo demo = new InterruptDemo("BB");
        demo.start();

        while (Thread.activeCount() > 2) {

        }
        //所有线程已销毁 除main线程以外
        demo.interrupt();
        //线程消亡后的状态为TERMINATED
        System.out.println(demo.getName() + "\t当前线程的状态：" + demo.getState() + "\t 当前线程的中断状态：" + demo.isInterrupted());

    }

    /**
     * BLOCKED 状态下中断线程 true
     */
    public static void threadStateInBlock() {
        Object lock = new Object();
        Thread cc = new Thread(() -> {
            System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
            synchronized (lock) {
                System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
            }
            System.out.println(currentThread().getName() + "\t当前系统时间：" + System.currentTimeMillis());
        }, "CC");
        cc.start();


        Thread dd = new Thread(() -> {
            System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
            synchronized (lock) {
                System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
            }
            System.out.println(currentThread().getName() + "\t当前系统时间：" + System.currentTimeMillis());
        }, "DD");
        dd.start();


        //对线程进行中断
        try {
            TimeUnit.MICROSECONDS.sleep(5);
            System.out.println(currentThread().getName() + "\t当前系统时间：" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cc.interrupt();
        dd.interrupt();

        System.out.println(cc.getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环外当前线程的中断状态：" + cc.isInterrupted());
        System.out.println(dd.getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环外内当前线程的中断状态：" + dd.isInterrupted());

    }

    /**
     * WAITING/TIME_WAITING 状态下中断线程 调用中断方法时会将当前中断状态设置为true
     * 但在此状态下的线程中断会抛出异常，并且在抛出异常前，会将当前线程的中断状态复位
     * 可以利用这个抛异常的状态实现线程中断，在异常中进行线程退出的逻辑，但不建议采用这种方式
     */
    public static void threadStateInWait() {
        Object lock = new Object();
        Thread ee = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
                    lock.wait();
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 异常内当前线程的中断状态：" + currentThread().isInterrupted());
                }
            }
        }, "EE");

        ee.start();

        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ee.interrupt();
        System.out.println(ee.getName() + "\t当前线程的状态：" + ee.getState() + "\t 循环外内当前线程的中断状态：" + ee.isInterrupted());

        Thread ff = new Thread(() -> {
            synchronized (lock) {
                System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
                lock.notify();
                System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
                try {
                    TimeUnit.SECONDS.sleep(20);
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 循环内当前线程的中断状态：" + currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 异常内当前线程的中断状态：" + currentThread().isInterrupted());
                }

            }
        }, "FF");

        ff.start();

        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ff.interrupt();
        System.out.println(ff.getName() + "\t当前线程的状态：" + ff.getState() + "\t 循环外内当前线程的中断状态：" + ff.isInterrupted());
    }

    @Override
    public void run() {
        System.out.println(currentThread().getName() + "\t当前线程的状态：" + currentThread().getState() + "\t 当前线程的中断状态：" + currentThread().isInterrupted());
    }
}
