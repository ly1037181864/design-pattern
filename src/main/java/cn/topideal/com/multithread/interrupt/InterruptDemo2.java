package cn.topideal.com.multithread.interrupt;


import java.util.concurrent.TimeUnit;

/**
 * 线程中断
 * true         aa.isInterrupted()
 * false        aa.interrupted()
 * main
 * false        Thread.interrupted()
 * true         aa.isInterrupted()
 * <p>
 * 原因
 * 出现这种情况的原因就是isInterrupted()和interrupted()一个属于当前实例的，一个属于类的，
 * isInterrupted()是返回当前实例所在的线程的中断状态
 * interrupted()由于是静态方法，它返回的是Thread.currentThread()即当前线程的中断状态，
 * Thread.interrupted()对于这个很好理解，当前线程就是main线程
 * 但aa.interrupted()到底当前线程是谁呢，通过aa.currentThread().getName()我们可以看到它的当前线程就是main
 * 为什么会这样的，其实很好理解，因为interrupted()是静态方法，而调用interrupted()时又是在main线程中，所以它的
 * 当前线程当然就是main线程了，所以会导致aa.interrupted()看似调用的是aa的中断结果，但实际上是获取调用这个静态方法
 * interrupted()所在线程的中断状态
 */
public class InterruptDemo2 {
    public static void main(String[] args) throws InterruptedException {

        Thread aa = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {

                }
            }
        });
        aa.start();

        TimeUnit.SECONDS.sleep(1);

        //中断线程
        aa.interrupt();

        //查看线程中断状态
        System.out.println(aa.isInterrupted());//true

        //查看线程中断状态
        System.out.println(aa.interrupted());//false

        System.out.println(aa.currentThread().getName());

        //查看线程中断状态
        System.out.println(Thread.interrupted());//false

        //查看线程中断状态
        System.out.println(aa.isInterrupted());//true
    }


}
