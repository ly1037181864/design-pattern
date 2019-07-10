package cn.topideal.com.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * G1垃圾收集器
 */
public class G1Gc {

    /**
     * -Xmx1g
     * -Xms1g
     * -XX:+UseG1GC
     * -XX:+PrintGCDetails
     * -Xloggc:/Users/liuyou/gc.log
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=/Users/liuyou/a.dump
     * <p>
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=/Users/liuyou/a.dump
     * -XX:InitialHeapSize=1073741824   初始化堆1g
     * -XX:MaxHeapSize=1073741824       最大堆1g
     * -XX:+PrintGC                     打印gc日志
     * -XX:+PrintGCDetails              打印gc详细信息
     * -XX:+PrintGCTimeStamps           打印gc时间戳
     * -XX:+UseCompressedClassPointers  压缩类指针
     * -XX:+UseCompressedOops           压缩指针，节约内存占用，避免从32位操作系统到64位操作系统，出现内存占用扩大的情况
     * -XX:+UseG1GC
     * <p>
     * 指针压缩1. 64位平台上默认打开
     * 1)使用-XX:+UseCompressedOops压缩对象指针
     * "oops"指的是普通对象指针("ordinary" object pointers)。
     * Java堆中对象指针会被压缩成32位。
     * 使用堆基地址（如果堆在低26G内存中的话，基地址为0）
     * 2)使用-XX:+UseCompressedClassPointers选项来压缩类指针
     * 对象中指向类元数据的指针会被压缩成32位
     * 类指针压缩空间会有一个基地址
     * <p>
     * 2. 元空间和类指针压缩空间的区别
     * 1)类指针压缩空间只包含类的元数据，比如InstanceKlass, ArrayKlass
     * 仅当打开了UseCompressedClassPointers选项才生效
     * 为了提高性能，Java中的虚方法表也存放到这里
     * 这里到底存放哪些元数据的类型，目前仍在减少
     * 2)元空间包含类的其它比较大的元数据，比如方法，字节码，常量池等。
     * <p>
     * 1.272: [GC pause (G1 Evacuation Pause) (young), 0.0215926 secs]                   系统开启1.272发生了一次新生代gc
     * [Parallel Time: 20.6 ms, GC Workers: 8]                                           新生代gc耗时20.6毫秒 GC Workers: 8个gc工作线程
     * [GC Worker Start (ms): Min: 1272.1, Avg: 1272.2, Max: 1272.2, Diff: 0.1]          开启GC工作线程的时间信息 Diff差值=Max-Min
     * [Ext Root Scanning (ms): Min: 0.1, Avg: 1.0, Max: 3.0, Diff: 2.9, Sum: 8.4]       跟扫描信息
     * [Update RS (ms): Min: 0.0, Avg: 1.2, Max: 3.4, Diff: 3.4, Sum: 9.7]               更新RS记忆集合(记录不同区域之间对象相互引用的集合，避免全堆扫描)
     * [Processed Buffers: Min: 0, Avg: 0.6, Max: 1, Diff: 1, Sum: 5]                    RS扫描
     * [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.2, Diff: 0.2, Sum: 0.2]                 对象拷贝
     * [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]      GC工作线程终止
     * [Object Copy (ms): Min: 16.2, Avg: 17.5, Max: 19.3, Diff: 3.1, Sum: 140.0]        GC尝试终止的次数
     * [Termination (ms): Min: 0.0, Avg: 0.5, Max: 0.6, Diff: 0.6, Sum: 3.9]
     * [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
     * [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]         GC在其他任务中的耗时
     * [GC Worker Total (ms): Min: 20.3, Avg: 20.3, Max: 20.3, Diff: 0.1, Sum: 162.4]
     * [GC Worker End (ms): Min: 1292.5, Avg: 1292.5, Max: 1292.5, Diff: 0.0]
     * [Code Root Fixup: 0.0 ms]
     * [Code Root Purge: 0.0 ms]
     * [Clear CT: 0.3 ms]       清除CardTable(对象在多个区间引用的集合，避免出现全堆扫描的情况)时间
     * [Other: 0.7 ms]          其他任务的耗时时间
     * [Choose CSet: 0.0 ms]
     * [Ref Proc: 0.3 ms]
     * [Ref Enq: 0.0 ms]
     * [Redirty Cards: 0.2 ms]
     * [Humongous Register: 0.0 ms]
     * [Humongous Reclaim: 0.0 ms]
     * [Free CSet: 0.0 ms]
     * [Eden: 51.0M(51.0M)->0.0B(44.0M) Survivors: 0.0B->7168.0K Heap: 51.5M(1024.0M)->48.4M(1024.0M)]      堆信息
     * [Times: user=0.09 sys=0.05, real=0.02 secs]   垃圾回收的耗时信息
     * <p>
     * -XX:InitiatingHeapOccupancyPercent			堆到达指定使用率时触发GC，默认45 即达到45%，执行并发标记周期
     * -XX:MaxPauseMillis                           GC最大的停顿时间，设置的不合理，将会导致新生代老生代多次频繁GC
     */
    public static class Mythread extends Thread {
        private Map map = new HashMap<>();

        @Override
        public void run() {
            try {

                while (true) {
                    if (map.size() * 512 / 1024 / 1024 >= 900) {
                        map.clear();
                        System.out.println("clear map");
                    }
                    byte[] b1;
                    for (int i = 0; i < 100; i++) {
                        b1 = new byte[512];
                        map.put(System.nanoTime(), b1);
                    }
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PrintThread extends Thread {
        public static final long start = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - start;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        Mythread aa = new Mythread();
        PrintThread bb = new PrintThread();
        aa.start();
        bb.start();
    }

}
