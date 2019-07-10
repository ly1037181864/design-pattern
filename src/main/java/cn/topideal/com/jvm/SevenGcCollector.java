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
        byte[] aa = new byte[2 * 1024 * 1024];
        System.gc();
    }

    /**
     * -XX:+UseParNewGC
     * ParNew
     * 由Serial串行到ParNew并行
     */
    public static void parNewGc() {
        byte[] aa = new byte[2 * 1024 * 1024];
        byte[] bb = new byte[2 * 1024 * 1024];
        byte[] cc = new byte[4 * 1024 * 1024];
        byte[] dd = new byte[4 * 1024 * 1024];

        System.gc();
    }

    /**
     * 默认的垃圾收集器 并发收集器
     * PSYoungGen
     * 注重吞吐量
     */
    public static void paralleGc() {
        byte[] aa = new byte[2 * 1024 * 1024];
        byte[] bb = new byte[2 * 1024 * 1024];
        byte[] cc = new byte[4 * 1024 * 1024];
        byte[] dd = new byte[4 * 1024 * 1024];

        System.gc();
    }

    /**
     * 并发CMS
     */
    public static void consMarkSweepGc() {
    }


    public static void main(String[] args) {
        parNewGc();
    }
}
