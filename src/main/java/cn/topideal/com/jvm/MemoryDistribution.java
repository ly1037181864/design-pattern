package cn.topideal.com.jvm;

/**
 * 对象的内存分配
 * 优先分配在Eden区
 * 大对象直接分配到老年代
 * 长期存活的对象分配到老年代
 * 空间分配担保
 * 年代对象的平均大小
 */
public class MemoryDistribution {
    public static void main(String[] args) {
        test3();
    }

    public static void test3() {
        try {
            byte[] aa = new byte[4 * 1024 * 1024];
//            byte[] bb = new byte[2 * 1024 * 1024];
//            byte[] cc = new byte[2 * 1024 * 1024];
//            byte[] dd = new byte[1 * 1024 * 1024];
            int i = 10;
            byte[] bb = null;
            while (--i > 0) {
                bb = new byte[1 * 1024 * 1024];
                bb = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * eden space 5632K, 30% used 1720K 注释aa
     * eden space 5632K, 66% used 3769K
     * ParOldGen       total 13824K, used 10240K
     * 大对象直接分配到老年代
     */
    public static void test2() {
        //byte[] aa = new byte[2 * 1024 * 1024];
        byte[] bb = new byte[10 * 1024 * 1024];
    }

    /**
     * 新生代内存 6144K  eden：5632K s0:512K s1:512K eden:s0:s1=8:1:1
     * 老年代内存 13824K
     * aa、bb说明对象的内存分配是优先在eden区域的
     * 当给cc分配内存时发现新生代的内存已经不够用了，这个时候就会由老年代进行内存担保，也就是出现了老年代内存占用4M
     * [GC (System.gc()) [PSYoungGen: 4680K->496K(6144K)] 8776K->7704K(19968K), 0.0027046 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * [PSYoungGen: 4680K->496K(6144K)] 说明在cc分配内存时发生了一次minorGC
     */
    public static void test() {
        byte[] aa = new byte[1 * 1024 * 1024];//eden space 5632K, 46% used 2632K
        byte[] bb = new byte[2 * 1024 * 1024];//eden space 5632K, 83% used 4680K ParOldGen object space 13824K, 0%, used 0K
        byte[] cc = new byte[4 * 1024 * 1024];//eden space 5632K, 85% used 4793K ParOldGen object space 13824K, 29% used 4096K
        System.gc();
    }
}
