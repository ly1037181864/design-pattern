package cn.topideal.com.jvm;


/**
 * class类的结构分析
 */
public class ClassStructureAnalyse {
    private ClassStructureAnalyse() {
        System.out.println(Thread.currentThread().getName() + "\t初始化");
    }

    private static class HoldClassStructureAnalyse {
        private static final ClassStructureAnalyse analyse = new ClassStructureAnalyse();
    }

    public static ClassStructureAnalyse getInstance() {
        return HoldClassStructureAnalyse.analyse;
    }

    private static void test() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                ClassStructureAnalyse.getInstance();
            }, String.valueOf(i)).start();
        }
    }

    public static void main(String[] args) {
        test();
    }

}
