package cn.topideal.com.jvm;

/**
 * JVM的三种参数类型
 * 标配参数 Java -version
 * x参数 xmode
 * xx参数
 * <p>
 * -XX:+PrintGCDetails -XX:-PrintGCDetails
 * -XX:+UseSerialGC -XX:-UseSerialGC
 * <p>
 * jps -l 查看所有java进程
 * jinfo -flag PrintGCDetails 6860 查看Java某一个进程是否开启某个属性值
 */
public class GcFlags {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello GC");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
