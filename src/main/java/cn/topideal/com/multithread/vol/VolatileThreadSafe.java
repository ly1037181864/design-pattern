package cn.topideal.com.multithread.vol;

import java.util.concurrent.TimeUnit;

/**
 * 保证可见性 Lock前缀指令
 * 有序性 happens-before、内存屏障
 * 不保证原子性 volatile关键字只能保证对volatile修饰的变量的写操作不能重排序到volatile修饰的变量的读操作，当一个线程对volatile变量进行了写操作，但其他线程并未执行load,read
 * 操作而是进行use,assign等操作时，最后进行write操作，这就会导致写覆盖，因为它无法做到像锁一样对同步操作进行锁定，因此无法做到操作的原子性
 */
public class VolatileThreadSafe {
    private boolean flags = false;//非volatile关键字修饰的变量
    volatile private boolean vFlags = false;//volatile关键字修饰的变量
    volatile private int i;

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 20个线程每个线程加1000次
     * i=:18426
     * 出现了写丢失
     * <p>
     * volatile关键字不保证原子性
     */
    public static void test2() {
        VolatileThreadSafe safe = new VolatileThreadSafe();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safe.addPlus();
                }
            }, String.valueOf(i)).start();
        }
        System.out.println("i=:" + safe.getI());

    }

    /**
     * volatile可见性
     * AA	线程开始运行
     * BB	线程开始运行
     * BB	线程结束运行
     * 对volatile关键字修饰的变量在main线程修改为true时，线程BB立即看到这一修改并及时的停止了线程循环
     * volatile如何保证可见性的呢？
     * volatile关键字修饰的变量，会生成一个Lock的前缀指令，当底层操作系统读取到这条指令时，会强制要求将该变量的写刷回主内存，
     * 同时将其他CPU缓存值置为无效，而其他线程在读取该变量时，必须从主内存中获取
     * 总线锁、缓存一致性协议MESI
     */
    public static void test1() {

        VolatileThreadSafe safe = new VolatileThreadSafe();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t线程开始运行" + System.currentTimeMillis());

            while (!safe.isFlags()) {
            }

            System.out.println(Thread.currentThread().getName() + "\t线程结束运行");
        }, "AA").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t线程开始运行" + System.currentTimeMillis());

            while (!safe.isvFlags()) {
            }

            System.out.println(Thread.currentThread().getName() + "\t线程结束运行");
        }, "BB").start();

        //睡眠200毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t线程开始运行" + System.currentTimeMillis());
        //尝试中断线程
        safe.setFlags(true);
        safe.setvFlags(true);
        System.out.println(Thread.currentThread().getName() + "\t线程结束运行" + System.currentTimeMillis());
    }


    public boolean isFlags() {
        return flags;
    }

    public void setFlags(boolean flags) {
        this.flags = flags;
    }

    public boolean isvFlags() {
        return vFlags;
    }

    public void setvFlags(boolean vFlags) {
        this.vFlags = vFlags;
    }

    public void addPlus() {
        i++;
    }

    public int getI() {
        return i;
    }
}
