package cn.topideal.com.multithread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Conditon源码分析
public class ConditionSourceAnalyse {

    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

//        Class<ConditionSourceAnalyse> aClass = ConditionSourceAnalyse.class;
//        ConditionSourceAnalyse conditionSourceAnalyse = new ConditionSourceAnalyse();
//
//        conditionSourceAnalyse.getClass();


        //System.out.println(Thread.interrupted());
        //System.out.println(Thread.currentThread().isInterrupted());

        Thread main = Thread.currentThread();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得锁，进入等待");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "获得锁，退出等待");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            System.out.println(main.getState());

        }, "AA").start();


        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得锁，进入等待");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "获得锁，退出等待");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(main.getState());
        }, "BB").start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(() -> {
            lock.lock();
            try {
                //condition.signal();
                condition.signalAll();
            } finally {
                lock.unlock();
            }
            System.out.println(main.getState());
        }, "CC").start();

    }

}
