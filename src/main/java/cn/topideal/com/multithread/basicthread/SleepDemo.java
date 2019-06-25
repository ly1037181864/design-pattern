package cn.topideal.com.multithread.basicthread;

/**
 * Sleep方法是否是释放锁以及索取的锁是对象锁还是类锁
 * sleep方法获取到的锁取决于同步代码块或者方法上的锁的类型
 * sleep方法在释放CPU时间片后并不会释放锁资源
 */
public class SleepDemo {
    public static void main(String[] args) {
        test1();
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
