package cn.topideal.com.multithread.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS算法的ABA问题
 * 针对ABA问题的解决
 */
public class CasAbaQue {
    private AtomicInteger integer = new AtomicInteger(5);

    public int getInteger() {
        return integer.get();
    }

    public boolean setInteger(int exp, int update) {
        return integer.compareAndSet(exp, update);
    }

    public static void main(String[] args) {
        //abaUnsafeQue();

        //abaUnsafeToResolveTest();

        integerTest();

    }

    /**
     * false
     * true
     * false
     * true
     * true
     * true
     * true
     * true
     * true
     * ====================
     * false
     * true
     * false
     * true
     * false
     * true
     * true
     * true
     * true
     * <p>
     * 引用类型/new与基本类型比较看内容 内容相同比较相等
     * 引用类型与new比较，永远不等
     * 引用类型与引用类型比较，-128-127之间，相等，否则不等
     */
    public static void integerTest() {
        Integer a = new Integer(5);
        Integer b = 5;
        int c = 5;
        Integer d = 5;

        System.out.println(a == b);//false 栈空间!=堆空间
        System.out.println(a == c);//true  自动拆箱 内容比较
        System.out.println(a == d);//false 栈空间!=堆空间
        System.out.println(b == c);//true  栈空间 值在-128-127之间，为同一个Ingeger对象 注意：Ingeger存在缓存，范围是-128-127之间
        System.out.println(b == d);//true  自动拆箱 内容比较
        System.out.println(c == d);//true  自动拆箱 内容比较
        System.out.println(a == 5);//true  自动拆箱 内容比较
        System.out.println(b == 5);//true  自动拆箱 内容比较
        System.out.println(d == 5);//true  自动拆箱 内容比较

        System.out.println("====================");
        Integer e = new Integer(129);
        Integer f = 129;
        int g = 129;
        Integer h = 129;
        System.out.println(e == f);//false  栈空间!=堆空间
        System.out.println(e == g);//true   自动拆箱 内容比较
        System.out.println(e == h);//false  栈空间!=堆空间
        System.out.println(f == g);//true   自动拆箱 内容比较
        System.out.println(f == h);//false  栈空间 值在-128-127之间，为同一个Ingeger对象 注意：Ingeger存在缓存，范围是-128-127之间
        System.out.println(g == h);//true   自动拆箱 内容比较
        System.out.println(e == 129);//true 自动拆箱 内容比较
        System.out.println(f == 129);//true 自动拆箱 内容比较
        System.out.println(h == 129);//true 自动拆箱 内容比较
    }

    /**
     * A1	true
     * A1	true
     * B1	false
     * A2	true
     * A2	true
     * B2	false
     * A3	true
     * A3	false
     * B3	false
     */
    public static void abaUnsafeToResolveTest() {
        abaUnsafeToResolve();
        while (Thread.activeCount() > 2) ;

        abaUnsafeToResolve2();
        while (Thread.activeCount() > 2) ;

        abaUnsafeToResolve3();
    }

    /**
     * A2	true
     * A2	true
     * B2	false
     */
    public static void abaUnsafeToResolve2() {
        AtomicStampedReference<Integer> asr = new AtomicStampedReference(5, 1);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(5, 102, 1, 2));//true
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(102, 5, 2, 3));//true
        }, "A2").start();

        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(5, 2019, 1, 3));//false
        }, "B2").start();
    }

    /**
     * A3	true
     * A3	false
     * B3	false
     */
    public static void abaUnsafeToResolve3() {
        AtomicStampedReference<Integer> asr = new AtomicStampedReference(5, 1);
        new Thread(() -> {
            /**
             * 出现这种情况的原因对于Integer而言内部是有缓存的，-128-127，默认情况下会返回缓存数组里的数据，而不是从新创建Integer
             * compareAndSet对Integer比较时最终是比较两者引用是否相等
             */
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(5, 2019, 1, 2));//true
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(2019, 5, 2, 3));//false
        }, "A3").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(5, 2019, 1, 2));//false
        }, "B3").start();
    }

    /**
     * A1	true
     * A1	true
     * B1	false
     */
    public static void abaUnsafeToResolve() {
        AtomicStampedReference<Integer> asr = new AtomicStampedReference(5, 1);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(asr.getReference(), 2019, 1, 2));//true
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(asr.getReference(), 5, 2, 3));//true
        }, "A1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + asr.compareAndSet(asr.getReference(), 2019, 1, 2));//false
        }, "B1").start();

    }

    /**
     * 尽管线程B将5修改成2019成功了，但是线程A却经历了将5->2019和2019->5的过程，因此从这个角度来说线程B将5->2019其实已经存在数据安全风险
     */
    private static void abaUnsafeQue() {
        CasAbaQue casAbaQue = new CasAbaQue();

        new Thread(() -> {
            //将5->2019
            System.out.println(Thread.currentThread().getName() + "\t" + casAbaQue.setInteger(5, 2019));

            try {
                //TODO
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //将2019->5
            System.out.println(Thread.currentThread().getName() + "\t" + casAbaQue.setInteger(2019, 5));

        }, "A").start();

        new Thread(() -> {
            //等待线程A操作完成
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //将5->2019
            System.out.println(Thread.currentThread().getName() + "\t" + casAbaQue.setInteger(5, 2019));

        }, "B").start();


        while (Thread.activeCount() > 2) ;

        System.out.println(casAbaQue.getInteger());
    }

}
