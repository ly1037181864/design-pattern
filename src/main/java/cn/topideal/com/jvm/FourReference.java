package cn.topideal.com.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 四大引用
 * 强引用
 * 软引用
 * 若引用
 * 虚引用
 */
public class FourReference {

    /**
     * 强引用
     * 任何时候都不会被垃圾回收，即使发生OOM
     */
    public static void StrongReference() {
        Object obj = new Object();
    }

    /**
     * 虚引用
     * 任何时候都无法通过get获取到
     * 当发生GC的时候会调用对象的finalize()方法，可以做一些资源释放，或者对象复活的操作
     * finalize()方法的弊端相见FinalizeDemo
     */
    public static void PhantomReference() {
        //byte[] aa = new byte[10 * 1024 * 1024];

        Man man = new Man(23, "liuyou");
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue();
        PhantomReference<Object> phantomReference = new PhantomReference(man, referenceQueue);

        System.out.println(man);
        System.out.println(phantomReference.get());
        man = null;
        System.out.println(phantomReference.get());
        System.gc();
        System.out.println(phantomReference.get());
    }

    /**
     * 弱引用
     * 只要发生GC一定会被回收，不论内存是否充足
     */
    public static void WeakReference() {
        byte[] aa = new byte[10 * 1024 * 1024];

        WeakReference<Object> weakReference = new WeakReference(aa);

        System.out.println(aa);
        aa = null;
        System.gc();
        System.out.println(weakReference.get());

    }

    /**
     * 软引用
     * 注释dd、ee
     * [B@266474c2
     * [B@266474c2
     * 两次都能获取到
     * 放开dd、ee
     * [B@266474c2
     * null
     * <p>
     * 通过对比可以得出，软引用就是在内存足够的情况下，是不会进行回收的，但是内存不够的情况下就会回收这部分的内存空间
     */
    public static void SoftReference() {
        byte[] aa = new byte[10 * 1024 * 1024];
        SoftReference<Object> reference = new SoftReference(aa);
        System.gc();
        System.out.println(reference.get());

        aa = null;
        byte[] bb = new byte[2 * 1024 * 1024];
        //byte[] cc = new byte[2 * 1024 * 1024];
        //byte[] dd = new byte[4 * 1024 * 1024];
        //byte[] ee = new byte[8 * 1024 * 1024];
        System.gc();
        System.out.println(reference.get());
    }

    /**
     * -XX:+PrintReferenceGC
     * 必须要开启-XX:+PrintGCDetails  -XX:+PrintGC这个不能显示出GC的详细信息
     * [SoftReference, 0 refs, 0.0000135 secs][WeakReference, 12 refs, 0.0000069 secs]
     * [FinalReference, 87 refs, 0.0000375 secs]
     * [PhantomReference, 0 refs, 0 refs, 0.0000233 secs]
     * [JNI Weak Reference, 0.0000084 secs]
     *
     * @param args
     */
    public static void main(String[] args) {
        SoftReference();
        WeakReference();
        PhantomReference();
    }
}

class Man {
    int age;
    String name;

    public Man(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + "," + this.age;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("拜拜");
    }
}
