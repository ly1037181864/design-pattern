package cn.topideal.com.multithread.lock.definition;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 手写Lock测试
 */
public class DefinitionLockTest {
    public static void main(String[] args) {
        DefinitionLock lock = new ReentrantDefinitionLock();
        Data data = new Data();
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    for (int j = 0; j < 1000; j++) {
                        data.add();
                    }
                } finally {
                    lock.unlock();
                }
            }, String.valueOf(i)).start();
        }


        while (Thread.activeCount() > 2) {
        }
        System.out.println(Thread.currentThread().getName() + "\t i=:" + data.i);

    }

    private static void test() {
        DefinitionLock lock = new ReentrantDefinitionLock();
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                lock.lock();
                try {

                    System.out.println(Thread.currentThread().getName() + "获得了锁");
                    Random random = new Random();
                    int sleep = random.nextInt(5);
                    TimeUnit.SECONDS.sleep(sleep * 100 + 1);
                    System.out.println(Thread.currentThread().getName() + "休眠了" + (sleep * 100 + 1) + "秒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }, String.valueOf(i)).start();
        }
    }
}

class Data {

    public static int i;

    public void add() {
        i++;
    }

}
