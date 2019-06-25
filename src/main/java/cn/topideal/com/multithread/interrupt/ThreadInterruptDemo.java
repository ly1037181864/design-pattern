package cn.topideal.com.multithread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 线程中断
 * 不能使用stop，这种简单粗暴的kill线程，必然会导致一系列的问题
 * interrupt
 * volatile关键字
 */
public class ThreadInterruptDemo extends Thread {

    private int i;
    private volatile boolean flag;

    @Override
    public void run() {
        //test1();
        //test2();
        test5();
    }

    public void test5() {
        while (!flag) {

            //System.out.println(currentThread().getName() + "\t" + this.isInterrupted());
            System.out.println(currentThread().getName() + "\t" + Thread.interrupted());
        }
    }


    private void test1() {
        while (!currentThread().isInterrupted())
            i++;

        System.out.println("i=:+" + i);
    }

    private void test2() {
        while (!flag)
            i++;

        System.out.println("i=:" + i);
    }

    public void shutDown() {
        flag = true;
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadInterruptDemo demo = new ThreadInterruptDemo();
        demo.start();

        TimeUnit.MICROSECONDS.sleep(50);
//        for (int i = 0; i < 5; i++) {
//            TimeUnit.MICROSECONDS.sleep(5);
//            demo.interrupt();
//        }

        demo.interrupt();

        TimeUnit.MICROSECONDS.sleep(50);
        demo.shutDown();
    }

    /**
     * 通过volatile变量实现线程的中断
     *
     * @throws InterruptedException
     */
    private static void test4() throws InterruptedException {
        ThreadInterruptDemo demo = new ThreadInterruptDemo();
        demo.start();

        TimeUnit.SECONDS.sleep(2);
        System.out.println("线程开始要中断了");
        demo.shutDown();
    }

    /**
     * 通过线程自己的interrupt方法实现中断
     * 但这个中断只是一种信号，具体的中断则由线程自己决定
     *
     * @throws InterruptedException
     */
    private static void test3() throws InterruptedException {
        ThreadInterruptDemo demo = new ThreadInterruptDemo();
        demo.start();

        TimeUnit.SECONDS.sleep(2);
        System.out.println("线程开始要中断了");
        demo.interrupt();
    }
}
