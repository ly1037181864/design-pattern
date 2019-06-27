package cn.topideal.com.multithread.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS 算法
 * Compare and Swap比较与交换
 * Unsafe类直接像C一样通过类似指针的方式操作底层内存数据
 * <p>
 * compareAndSwapInt(Object var1, long var2, int var4, int var5);var1 当前对象 var2 内存地址偏移量 var4 期望值 var5 更新值
 */
public class AtomicIntegerCasDemo {
    private volatile int i;
    private AtomicInteger integer = new AtomicInteger(0);

    public int getI() {
        return i;
    }

    public void addPlus() {
        i++;
    }

    public int getInteger() {
        return integer.get();
    }

    public void addPlusAtomic() {
        integer.incrementAndGet();
    }

    /**
     * i=:18589
     * integer=:20000
     *
     * @param args
     */
    public static void main(String[] args) {
        AtomicIntegerCasDemo demo = new AtomicIntegerCasDemo();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    demo.addPlus();
                }
            }, "A" + i).start();
        }

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    demo.addPlusAtomic();
                }
            }, "B" + i).start();
        }

        //等所有线程都算完结果
        if (Thread.activeCount() > 2) {

        }

        System.out.println("i=:" + demo.getI());
        System.out.println("integer=:" + demo.getInteger());
    }
}
