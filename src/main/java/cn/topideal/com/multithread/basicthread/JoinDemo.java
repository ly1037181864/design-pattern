package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.TimeUnit;

/**
 * join方法执行时会释放锁资源
 * 这里有个踩坑点
 * 那就是join方法本身是final的非静态的且加了synchronized关键字，意味着join方法的锁对象是当前线程，为什么有的说不会释放锁，有的会释放锁
 * 那是因为调用join方法时，如果调用对象或线程又加了一个锁，且锁的对象不是调用join对象，那么意味着其实加了两把锁，一把是自己加的，一般是join
 * 线程加的，当调用join方法时，join方法会释放掉当前线程的锁，但是不会释放调用放自己加的锁
 * <p>
 * 即test3中，main方法自己生成了lock锁对象，且线程BB和CC都是共享lock锁对象，当线程BB调用aa.join方法时，其实是加了两把锁，线程BB自己加了
 * synchronized关键字锁住lock对象，而join方法申明中又加了关键字synchronized，意味着join方法会对线程aa对象加锁，当join方法里面调用Object.wait
 * 方法时其实已经将aa对象锁释放掉了，但是无法释放线程BB自己加synchronized锁，因为此时线程AA处于死循环状态中，而线程BB又必须要等到线程AA结
 * 束后才能释放锁
 * <p>
 * 因为synchronized (lock)和synchronized void join(long millis)两个锁住的不是同一个对象，即线程BB锁住的是Object锁，而线程join()
 * 锁住的是aa线程对象锁
 * <p>
 * 对于线程DD和EE来说synchronized (aa)和synchronized void join(long millis)两个锁住的是同一个对象，因此调用join()方法时，线程释放
 * 了锁资源
 * <p>
 * join主要是调用join方法的当前线程阻塞，知道join对象线程执行完毕。
 */
public class JoinDemo {

    public static void main(String[] args) throws InterruptedException {
        test1();
        //test2();
        //test3();
    }

    /**
     * "FF" #16 prio=5 os_prio=31 tid=0x00007fd4c09c9000 nid=0x5c03 waiting for monitor entry [0x000070000eb22000]
     * "EE" #15 prio=5 os_prio=31 tid=0x00007fd4c09c8800 nid=0xa707 runnable [0x000070000ea1f000]
     * "CC" #13 prio=5 os_prio=31 tid=0x00007fd4c1128000 nid=0x5a03 waiting for monitor entry [0x000070000e91c000]
     * "BB" #12 prio=5 os_prio=31 tid=0x00007fd4c10a4800 nid=0xa803 in Object.wait() [0x000070000e819000]
     * "AA" #11 prio=5 os_prio=31 tid=0x00007fd4c10a3000 nid=0x5703 runnable [0x000070000e716000]
     * 控制台输出
     * BB获得锁资源
     * DD获取锁成功
     * EE获取锁成功
     */
    public static void test3() {
        //锁
        Object lock = new Object();
        Thread aa = new Thread(() -> {
            while (true) ;

        }, "AA");
        aa.start();

        new Thread(() -> {
            try {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获得锁资源");
                    aa.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "BB").start();

        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获取锁成功");
            }
        }, "CC").start();

        new Thread(() -> {
            synchronized (aa) {
                System.out.println(Thread.currentThread().getName() + "获取锁成功");
                try {
                    aa.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DD").start();

        //睡眠200毫秒，目的是为了能看到DD获取过锁aa
        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (aa) {
                System.out.println(Thread.currentThread().getName() + "获取锁成功");
                //产生死锁
                while (true) ;
            }
        }, "EE").start();

        //睡眠200毫秒，目的是为了EE线程优先获取到锁
        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (aa) {
                System.out.println(Thread.currentThread().getName() + "获取锁成功");
            }
        }, "FF").start();

    }

    public static void test1() {
        //锁
        Object lock = new Object();

        Thread aa = new Thread(() -> {
            while (true) ;

        }, "AA");
        aa.start();

        new Thread(() -> {
            try {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获得锁资源");
                    aa.join();
                }
                System.out.println(Thread.currentThread().getName() + "执行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "BB").start();

        //让线程BB有足够的时间优先获取到锁资源
        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获得锁资源");
            }
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }, "CC").start();

        System.out.println(Thread.currentThread().getName() + "执行结束");
    }

    private static void test2() {
        Thread aa = new Thread(() -> {
            while (true) ;
        }, "AA");
        aa.start();

        Thread bb = new Thread(() -> {
            try {
                synchronized (aa) {
                    System.out.println(Thread.currentThread().getName() + "获取锁成功");
                    //这个方法释放锁
                    aa.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束运行");
        }, "BB");
        bb.start();

        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread cc = new Thread(() -> {
            synchronized (aa) {
                System.out.println(Thread.currentThread().getName() + "获取锁成功");
            }
        }, "CC");
        cc.start();

        System.out.println(Thread.currentThread().getName() + "结束运行");
    }
}
/**
 * "FF" #16 prio=5 os_prio=31 tid=0x00007fd4c09c9000 nid=0x5c03 waiting for monitor entry [0x000070000eb22000]
 * java.lang.Thread.State: BLOCKED (on object monitor)
 * at cn.topideal.com.multithread.basicthread.JoinDemo.lambda$test3$5(JoinDemo.java:72)
 * - waiting to lock <0x000000076ac2fc50> (a java.lang.Thread)
 * at cn.topideal.com.multithread.basicthread.JoinDemo$$Lambda$6/793589513.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * <p>
 * "EE" #15 prio=5 os_prio=31 tid=0x00007fd4c09c8800 nid=0xa707 runnable [0x000070000ea1f000]
 * java.lang.Thread.State: RUNNABLE
 * at cn.topideal.com.multithread.basicthread.JoinDemo.lambda$test3$4(JoinDemo.java:59)
 * - locked <0x000000076ac2fc50> (a java.lang.Thread)
 * at cn.topideal.com.multithread.basicthread.JoinDemo$$Lambda$5/189568618.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * <p>
 * "CC" #13 prio=5 os_prio=31 tid=0x00007fd4c1128000 nid=0x5a03 waiting for monitor entry [0x000070000e91c000]
 * java.lang.Thread.State: BLOCKED (on object monitor)
 * at cn.topideal.com.multithread.basicthread.JoinDemo.lambda$test3$2(JoinDemo.java:38)
 * - waiting to lock <0x000000076ac2fc40> (a java.lang.Object)
 * at cn.topideal.com.multithread.basicthread.JoinDemo$$Lambda$3/1867083167.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * <p>
 * "BB" #12 prio=5 os_prio=31 tid=0x00007fd4c10a4800 nid=0xa803 in Object.wait() [0x000070000e819000]
 * java.lang.Thread.State: WAITING (on object monitor)
 * at java.lang.Object.wait(Native Method)
 * - waiting on <0x000000076ac2fc50> (a java.lang.Thread)
 * at java.lang.Thread.join(Thread.java:1252)
 * - locked <0x000000076ac2fc50> (a java.lang.Thread)
 * at java.lang.Thread.join(Thread.java:1326)
 * at cn.topideal.com.multithread.basicthread.JoinDemo.lambda$test3$1(JoinDemo.java:29)
 * - locked <0x000000076ac2fc40> (a java.lang.Object)
 * at cn.topideal.com.multithread.basicthread.JoinDemo$$Lambda$2/1072408673.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * <p>
 * "AA" #11 prio=5 os_prio=31 tid=0x00007fd4c10a3000 nid=0x5703 runnable [0x000070000e716000]
 * java.lang.Thread.State: RUNNABLE
 * at cn.topideal.com.multithread.basicthread.JoinDemo.lambda$test3$0(JoinDemo.java:20)
 * at cn.topideal.com.multithread.basicthread.JoinDemo$$Lambda$1/363771819.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * <p>
 * "Service Thread" #10 daemon prio=9 os_prio=31 tid=0x00007fd4c082b800 nid=0x3703 runnable [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "C1 CompilerThread3" #9 daemon prio=9 os_prio=31 tid=0x00007fd4c3004800 nid=0x3903 waiting on condition [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "C2 CompilerThread2" #8 daemon prio=9 os_prio=31 tid=0x00007fd4c200a000 nid=0x3603 waiting on condition [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "C2 CompilerThread1" #7 daemon prio=9 os_prio=31 tid=0x00007fd4c2003800 nid=0x3503 waiting on condition [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "C2 CompilerThread0" #6 daemon prio=9 os_prio=31 tid=0x00007fd4c1069000 nid=0x3d03 waiting on condition [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "Monitor Ctrl-Break" #5 daemon prio=5 os_prio=31 tid=0x00007fd4bf057000 nid=0x3303 runnable [0x000070000e001000]
 * java.lang.Thread.State: RUNNABLE
 * at java.net.SocketInputStream.socketRead0(Native Method)
 * at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
 * at java.net.SocketInputStream.read(SocketInputStream.java:171)
 * at java.net.SocketInputStream.read(SocketInputStream.java:141)
 * at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
 * at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
 * at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
 * - locked <0x000000076ac843d0> (a java.io.InputStreamReader)
 * at java.io.InputStreamReader.read(InputStreamReader.java:184)
 * at java.io.BufferedReader.fill(BufferedReader.java:161)
 * at java.io.BufferedReader.readLine(BufferedReader.java:324)
 * - locked <0x000000076ac843d0> (a java.io.InputStreamReader)
 * at java.io.BufferedReader.readLine(BufferedReader.java:389)
 * at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)
 * <p>
 * "Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fd4c000c800 nid=0x3f07 runnable [0x0000000000000000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * "Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fd4c2003000 nid=0x3003 in Object.wait() [0x000070000ddfb000]
 * java.lang.Thread.State: WAITING (on object monitor)
 * at java.lang.Object.wait(Native Method)
 * - waiting on <0x000000076ab08ed0> (a java.lang.ref.ReferenceQueue$Lock)
 * at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
 * - locked <0x000000076ab08ed0> (a java.lang.ref.ReferenceQueue$Lock)
 * at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
 * at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)
 * <p>
 * "Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fd4c0026800 nid=0x4b03 in Object.wait() [0x000070000dcf8000]
 * java.lang.Thread.State: WAITING (on object monitor)
 * at java.lang.Object.wait(Native Method)
 * - waiting on <0x000000076ab06bf8> (a java.lang.ref.Reference$Lock)
 * at java.lang.Object.wait(Object.java:502)
 * at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
 * - locked <0x000000076ab06bf8> (a java.lang.ref.Reference$Lock)
 * at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)
 * <p>
 * "VM Thread" os_prio=31 tid=0x00007fd4c1005000 nid=0x4c03 runnable
 * <p>
 * "GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fd4c0003000 nid=0x1f07 runnable
 * <p>
 * "GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fd4c0807800 nid=0x1d03 runnable
 * <p>
 * "GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fd4c1000000 nid=0x5403 runnable
 * <p>
 * "GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fd4c001f000 nid=0x5203 runnable
 * <p>
 * "GC task thread#4 (ParallelGC)" os_prio=31 tid=0x00007fd4c1800000 nid=0x2c03 runnable
 * <p>
 * "GC task thread#5 (ParallelGC)" os_prio=31 tid=0x00007fd4c1001000 nid=0x5003 runnable
 * <p>
 * "GC task thread#6 (ParallelGC)" os_prio=31 tid=0x00007fd4c1001800 nid=0x4e03 runnable
 * <p>
 * "GC task thread#7 (ParallelGC)" os_prio=31 tid=0x00007fd4c2000000 nid=0x4d03 runnable
 * <p>
 * "VM Periodic Task Thread" os_prio=31 tid=0x00007fd4c200a800 nid=0x5603 waiting on condition
 * <p>
 * JNI global references: 331
 */
