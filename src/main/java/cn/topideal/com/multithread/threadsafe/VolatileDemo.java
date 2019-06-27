package cn.topideal.com.multithread.threadsafe;

import java.util.concurrent.TimeUnit;

/**
 * volatile
 * 保证线程的可见性、不保证线程的原子性、禁止指令重排序
 */
public class VolatileDemo extends Thread {
    //boolean flags = false; 线程出现死循环
    volatile boolean flags = false;//线程正常退出

    public static void main(String[] args) throws InterruptedException {
        VolatileDemo demo = new VolatileDemo();
        demo.start();

        TimeUnit.MICROSECONDS.sleep(200);
        demo.shutDown();
    }

    @Override
    public void run() {
        while (!flags) ;
    }

    public void shutDown() {
        flags = true;
    }
}

