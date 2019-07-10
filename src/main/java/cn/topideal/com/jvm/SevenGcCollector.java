package cn.topideal.com.jvm;

/**
 * 7大垃圾收集器
 * <p>
 * -Xmx20m
 * -Xms20m
 * -Xmn6m
 * -XX:+PrintGCDetails
 */
public class SevenGcCollector {

    /**
     * -XX:+UseSerialGC
     * 但线程垃圾收集器
     * 独占式
     * DefNew
     */
    public static void serialGc() {
        print();
    }

    public static void serialOldGc() {
        print();
    }

    /**
     * -XX:+UseParNewGC
     * ParNew
     * 由Serial串行到ParNew并行
     *
     * 老年代使用serialOldGC
     */
    public static void parNewGc() {
        print();
    }

    /**
     * 默认的垃圾收集器 并发收集器
     * PSYoungGen
     * 注重吞吐量
     * 老年代使用serialOld
     */
    public static void paralleGc() {
        print();
    }

    /**
     * -XX:+parallelOldGC
     */
    public static void parallelOldGc() {
        print();
    }

    /**
     * 并发CMS
     * -XX:+UseConcMarkSweepGC
     *  新生代用ParNew收集器
     * 老年代 CMS Initial Mark 初始标记 CMS-concurrent-mark-start 并发标记
     * 新生代采用ParNew
     */
    public static void consMarkSweepGc() {
        print();
    }

    /**
     * -XX:+UseG1GC
     * GC pause (G1 Humongous Allocation)
     */
    public static void g1Gc() {
        print();
    }

    public static void print() {
        byte[] aa = new byte[1 * 1024 * 1024];
        byte[] bb = new byte[2 * 1024 * 1024];
        byte[] cc = new byte[2 * 1024 * 1024];
        byte[] dd = new byte[4 * 1024 * 1024];

        System.gc();
    }


    public static void main(String[] args) {
        serialOldGc();
    }
}
