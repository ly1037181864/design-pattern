package cn.topideal.com.jvm;

/**
 * 四大垃圾收集算法
 * 引用计数法    循环引用
 * 标记清除      空间碎片，两次操作效率低
 * 标记整理      空间利用率好，两次操作有一定的性能开销
 * 复制算法      S0/S1 from-to  空间浪费，不会产生碎片
 * 分代算法
 * 分区算法
 * <p>
 * 什么是垃圾
 * 引用计数法
 * 枚举跟节点进行可达性分析 GCRoot 对象从GCRoots集合(sets集合)出发，如果对象与对象之间，能够与GCRoot关联起来，则该对象存活否则就是垃圾，应该被回收
 * <p>
 * 以下四种对象可做GCRoot
 * 栈针种局部变量表里的引用对象
 * 方法区里的静态的引用对象
 * 方法区里的常量的引用对象
 * 本地方法栈里的引用对象
 */
public class GCRoots {
    private Object obj;

    public GCRoots() {
    }

    public GCRoots(Object obj) {
        this.obj = obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    /**
     * 从这个例子种说明我们的GC不是采用引用计数法来判断当前对象是否还存活
     * -XX:+PrintGCDetails
     * -Xmx20m
     * -Xms20m
     * <p>
     * Runtime.getRuntime().maxMemory() / 1024d / 1024d 19.5
     * Runtime.getRuntime().totalMemory() / 1024d / 1024d 19.5
     * <p>
     * PSYoungGen      total 6144K
     * ParOldGen       total 13824K
     * Metaspace       total 1056768K
     * PSYoungGen + ParOldGen = Heap   6144+13824 = 19968k
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().maxMemory() / 1024d / 1024d);
        System.out.println(Runtime.getRuntime().totalMemory() / 1024d / 1024d);
        System.out.println(Runtime.getRuntime().freeMemory() / 1024d / 1024d);

        GCRoots aa = new GCRoots();
        GCRoots bb = new GCRoots(aa);
        aa.setObj(bb);

        aa = null;
        bb = null;
        System.gc();


    }
}
