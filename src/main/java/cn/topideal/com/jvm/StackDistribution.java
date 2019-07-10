package cn.topideal.com.jvm;

/**
 * 栈上分配
 */
public class StackDistribution {

    /**
     * Heap
     * PSYoungGen      total 2560K, used 13K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 0% used [0x00000007bfd00000,0x00000007bfd03758,0x00000007bff00000)
     * from space 512K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007bff80000)
     * to   space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * ParOldGen       total 7168K, used 380K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 5% used [0x00000007bf600000,0x00000007bf65f210,0x00000007bfd00000)
     * Metaspace       used 2959K, capacity 4496K, committed 4864K, reserved 1056768K
     * class space    used 326K, capacity 388K, committed 512K, reserved 1048576K
     * <p>
     * [GC (System.gc()) [PSYoungGen: 1326K->496K(2560K)] 7470K->6664K(9728K), 0.0013670 secs]
     * [Full GC (System.gc()) [PSYoungGen: 496K->0K(2560K)] [ParOldGen: 6168K->380K(7168K)] 6664K->380K(9728K)
     * 首先从整个堆的情况看
     * 新生代 PSYoungGen      total 2560K, used 13K
     * 老年代 ParOldGen       total 7168K, used 380K
     * 堆的大小是10M 所以到这里我们直到，6M对象已经被GC回收了
     * <p>
     * 在查看GC日志
     * minorGC时，新生代由1326K->496K，而堆的大小是7470K->6664K 这里就可以说明该对象由于是大对象被直接分配到了老年代
     * 但是到了FullGC时，新生代由496K->0K，而整个堆的大小呢6664K->380K，当再次进行FullGC时，才被销毁
     */
    public static void test() {
        byte[] aa = new byte[6 * 1024 * 1024];//分配6M的空间
    }


    public static class User {
        private int age;
        private String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }

    /**
     * 在堆里分配20B 循环调用1亿次 也即堆里共计消耗内存20*1亿B大约1.86G
     */
    public static void alloc() {
        User user = new User(23, "liuyou");
    }

    /**
     * [GC (Allocation Failure) [PSYoungGen: 2047K->496K(2560K)] 2047K->504K(9728K), 0.0013903 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * [GC (Allocation Failure) [PSYoungGen: 2543K->496K(2560K)] 2551K->504K(9728K), 0.0009658 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * [GC (Allocation Failure) [PSYoungGen: 2543K->432K(2560K)] 2551K->440K(9728K), 0.0012607 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * <p>
     * Heap
     * PSYoungGen      total 2560K, used 1361K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 45% used [0x00000007bfd00000,0x00000007bfde86f0,0x00000007bff00000)
     * from space 512K, 84% used [0x00000007bff00000,0x00000007bff6c010,0x00000007bff80000)
     * to   space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * ParOldGen       total 7168K, used 8K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 0% used [0x00000007bf600000,0x00000007bf602000,0x00000007bfd00000)
     * Metaspace       used 2971K, capacity 4500K, committed 4864K, reserved 1056768K
     * class space    used 328K, capacity 388K, committed 512K, reserved 1048576K
     * <p>
     * 整个新生代 total 2560K, used 1361K
     * 整个老年代 total 7168K, used 8K
     * 而GC日志都是minorGC没有FullGC，这就说明vm启用了栈上分配，对象随着栈帧调用的结束而结束
     * <p>
     * 启用栈上分配
     * -Xms10m
     * -Xmx10m
     * -XX:+DoEscapeAnalysis
     * -XX:+EliminateAllocations
     * -XX:+PrintGCDetails
     * -XX:-UseTLAB
     * 耗时 8左右
     * <p>
     * 不启用站上分配
     * -Xms10m
     * -Xmx10m
     * -XX:+PrintGCDetails
     * 耗时10
     * <p>
     * -XX:+PrintHeapAtGC 每次GC前打印详细的堆信息
     * {Heap before GC invocations=1 (full 0):
     * PSYoungGen      total 2560K, used 2048K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 100% used [0x00000007bfd00000,0x00000007bff00000,0x00000007bff00000)
     * from space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * to   space 512K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007bff80000)
     * ParOldGen       total 7168K, used 0K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 0% used [0x00000007bf600000,0x00000007bf600000,0x00000007bfd00000)
     * Metaspace       used 2913K, capacity 4500K, committed 4864K, reserved 1056768K
     * class space    used 318K, capacity 388K, committed 512K, reserved 1048576K
     * [GC (Allocation Failure) [PSYoungGen: 2048K->496K(2560K)] 2048K->520K(9728K), 0.0012320 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * Heap after GC invocations=1 (full 0):
     * PSYoungGen      total 2560K, used 496K [0x00000007bfd00000, 0x00000007c0000000, 0x00000007c0000000)
     * eden space 2048K, 0% used [0x00000007bfd00000,0x00000007bfd00000,0x00000007bff00000)
     * from space 512K, 96% used [0x00000007bff00000,0x00000007bff7c010,0x00000007bff80000)
     * to   space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
     * ParOldGen       total 7168K, used 24K [0x00000007bf600000, 0x00000007bfd00000, 0x00000007bfd00000)
     * object space 7168K, 0% used [0x00000007bf600000,0x00000007bf606000,0x00000007bfd00000)
     * Metaspace       used 2913K, capacity 4500K, committed 4864K, reserved 1056768K
     * class space    used 318K, capacity 388K, committed 512K, reserved 1048576K
     * }
     */
    public static void test2() {
        byte[] aa = new byte[20];
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            //alloc();
            test2();
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
