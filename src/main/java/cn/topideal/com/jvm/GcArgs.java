package cn.topideal.com.jvm;

/**
 * GC参数
 */
public class GcArgs {

    public static void main(String[] args) {
        test6();
    }


    /**
     * 并行GC额外出发新生代GC
     * -XX:+UseSerialGC         [Full GC (System.gc())
     * -XX:+UseParNewGC         [Full GC (System.gc())
     * -XX:+UseParallelGC       [GC (System.gc()) [Full GC (System.gc())
     * -XX:+UseParallelOldGC    [GC (System.gc()) [Full GC (System.gc())
     * -XX:+UseConcMarkSWeepGC  [Full GC (System.gc())
     * -xx:+UseG1GC             [Full GC (System.gc())
     * <p>
     * 通过对比我们发现
     * 只有parallelGC和parallelOldGC在调用System.gc();时会触发一次新生代gc
     * 这是为了先在新生代收集一次垃圾，避免将所有工作同时交给FullGC，降低停顿的时间
     * 可以通过-XX:-ScavengeBeforeFullGC 禁止FullGC前的MinorGC 默认开启+
     */
    public static void test6() {
        byte[] aa = new byte[5 * 1024 * 1024];
        System.gc();
    }

    /**
     * -XX:+ExplicitGCInvokesConcurrent
     * System.gc();默认情况下是不支持并发执行，通过修改上面的参数就可以达到让gc并发执行
     * <p>
     * (initial-mark)
     * [GC concurrent-root-region-scan-start]
     * [GC concurrent-root-region-scan-end, 0.0003672 secs]
     * [GC concurrent-mark-start]
     * [GC concurrent-mark-end, 0.0000293 secs]
     * [GC remark [Finalize Marking, 0.0000395 secs]
     * [GC cleanup 5768K->5768K(10M), 0.0001024 secs]
     * <p>
     * 貌似没什么作用
     */
    public static void test5() {
        byte[] aa = new byte[5 * 1024 * 1024];
        byte[] bb = new byte[6 * 1024 * 1024];
        System.gc();
    }

    /**
     * -XX:+DisableExpicitGC 禁止手动调用System.gc();
     */
    public static void test4() {
        byte[] aa = new byte[5 * 1024 * 1024];
        byte[] bb = new byte[6 * 1024 * 1024];
        System.gc();
    }

    /**
     * -XX:SurvivorRatio=2 eden:from:to = 2:1:1
     * 调整eden和from/to的比例
     * 默认是8：1：1
     * eden space 2048K
     * from space 512K
     * to   space 512K
     * PSYoungGen      total 2560K
     * <p>
     * -XX:NewRatio=2
     * 调整新生代和老生带的比例 new:old=1:2
     * PSYoungGen      total 2048K
     * ParOldGen       total 7680K
     * <p>
     * -XX:MaxMetaspaceSize=10m 元空间最大10m
     * -XX:+PrintFlagsFinal 打印所有vm参数
     * -XX:+CMSClassUnloadingEnabled 清理元空间的class数据
     * <p>
     * -Xmx10m
     * -Xms10m
     * -XX:+PrintGCDetails
     * -XX:SurvivorRatio=2
     * -XX:NewRatio=3
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=/Users/liuyou/a.dump
     * -XX:MaxMetaspaceSize=10m
     * -XX:MaxDirectMemorySize=10m
     * -XX:+PrintVMOptions
     * -XX:+PrintFlagsFinal
     * -XX:+UseConcMarkSweepGC
     * -XX:+CMSClassUnloadingEnabled
     */
    public static void test3() {
        byte[] aa = new byte[5 * 1024 * 1024];
        //byte[] bb = new byte[6 * 1024 * 1024];
        System.gc();
    }

    /**
     * -Xms10m
     * -Xmx10m
     * -XX:+PrintGCDetails
     * -XX:SurvivorRatio=2
     * -XX:NewRatio=3
     * -XX:+HeapDumpOnOutOfMemoryError  对内存溢出时将堆的信息输出日志
     * -XX:HeapDumpPath=/Users/liuyou/a.dump 日志记录
     * <p>
     * PSYoungGen      total 2048K
     * ParOldGen       total 7680K
     * eden space 1536K
     * from space 512K
     * to   space 512K
     * ParallelGC
     */
    public static void test2() {
        byte[] aa = new byte[5 * 1024 * 1024];
        //byte[] bb = new byte[6 * 1024 * 1024];
        System.gc();
    }

    /**
     * -XX:+PrintHeapAtGC 每次GC前打印堆信息
     * {Heap before GC invocations=1 (full 0):
     * PSYoungGen      total 2560K, used 1428K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 69% used [0x00000007bfd00000,0x00000007bfe652f8,0x00000007bff00000)
     * from space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * to   space 512K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007bff80000)
     * ParOldGen       total 7168K, used 5120K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 71% used [0x00000007bf600000,0x00000007bfb00010,0x00000007bfd00000)
     * Metaspace       used 2960K, capacity 4496K, committed 4864K, reserved 1056768K
     * class space    used 327K, capacity 388K, committed 512K, reserved 1048576K
     * 0.102: [GC (System.gc()) [PSYoungGen: 1428K->496K(2560K)] 6548K->5656K(9728K), 0.0014797 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * Heap after GC invocations=1 (full 0):
     * PSYoungGen      total 2560K, used 496K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 0% used [0x00000007bfd00000,0x00000007bfd00000,0x00000007bff00000)
     * from space 512K, 96% used [0x00000007bff00000,0x00000007bff7c010,0x00000007bff80000)
     * to   space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * ParOldGen       total 7168K, used 5160K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 71% used [0x00000007bf600000,0x00000007bfb0a010,0x00000007bfd00000)
     * Metaspace       used 2960K, capacity 4496K, committed 4864K, reserved 1056768K
     * class space    used 327K, capacity 388K, committed 512K, reserved 1048576K
     * }
     * <p>
     * -XX:+PrintGCTimeStamps
     * 0.102: [GC (System.gc()) [PSYoungGen: 1428K->496K(2560K)] 6548K->5656K(9728K), 0.0014797 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * <p>
     * -XX:+PrintGCApplicationConcurrentTime
     * 0.101: Application time: 0.0050400 seconds
     * 0.108: Application time: 0.0004365 seconds
     * <p>
     * -XX:+PrintGCApplicationStoppedTime
     * 0.112: Total time for which application threads were stopped: 0.0065667 seconds, Stopping threads took: 0.0000281 seconds
     */
    private static void test() {
        byte[] aa = new byte[5 * 1024 * 1024];
        System.gc();
    }
}
