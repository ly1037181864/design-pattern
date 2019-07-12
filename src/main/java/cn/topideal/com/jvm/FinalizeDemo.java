package cn.topideal.com.jvm;

/**
 * 使用finalize会导致严重的内存消耗和性能损失
 * 堆内存中会多出很多java.lang.ref.Finalizer对象
 * JVM不确保finalize一定会被执行，而且执行finalize的时间也不确定
 */
public class FinalizeDemo {

    public static class LF {
        private byte[] aa = new byte[512];

        /**
         * 当注释此段代码后发现并不会造成内存溢出
         * 注释睡眠代码  java.lang.OutOfMemoryError: GC overhead limit exceeded
         * 不注释此代码造成内存溢出
         * -Xmx10m
         * -Xms10m
         * -XX:+PrintGCDetails
         * -XX:+HeapDumpOnOutOfMemoryError
         * -XX:HeapDumpPath=/Users/liuyou/b.dump
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            System.out.println(Thread.currentThread().getName() + "\t执行finalize方法");
            //TimeUnit.SECONDS.sleep(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            LF lf = new LF();
        }
        System.out.println(System.currentTimeMillis() - b);
    }

    /**
     *
     */
    private static void test() {
        FinalizeDemo demo;
        long total = Runtime.getRuntime().totalMemory();
        double free;
        while (true) {
            demo = new FinalizeDemo();
            free = Runtime.getRuntime().freeMemory();
            if (free < total) {
                System.gc();
            }
            //TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        //System.out.println("拜拜");
    }
}
