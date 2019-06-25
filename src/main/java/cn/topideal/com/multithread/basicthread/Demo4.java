package cn.topideal.com.multithread.basicthread;

/**
 * 这个例子是正确的
 * 因为synchronized (demo4)和synchronized void join(long millis)两个锁住的是同一个对象即demo4
 * 所以线程释放了锁资源
 */
public class Demo4 extends Thread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start");
        final Demo4 demo4 = new Demo4();

        // 启动demo4线程并且占用锁之后调用join(long)方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (demo4) {
                        System.out.println("进入同步代码块，threadName ->{} 占有 demo4 的锁\t" + Thread.currentThread().getName());
                        demo4.start();
                        demo4.join(4 * 1000);
                        System.out.println("退出同步代码块，threadName ->{}\t" + Thread.currentThread().getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "threadA").start();

        // 休眠2秒钟，调用对象的同步方法
        Thread.currentThread().sleep(2 * 1000);
        demo4.test2();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void test2() {
        System.out.println("进入test2方法，占有锁，threadname->{}\t" + currentThread().getName());
    }
}
