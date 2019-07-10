package cn.topideal.com.jvm;

/**
 * 使用finalize会导致严重的内存消耗和性能损失
 * 堆内存中会多出很多java.lang.ref.Finalizer对象
 * JVM不确保finalize一定会被执行，而且执行finalize的时间也不确定
 */
public class FinalizeDemo {
    public static void main(String[] args) throws InterruptedException {

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
