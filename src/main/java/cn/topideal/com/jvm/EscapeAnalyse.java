package cn.topideal.com.jvm;

/**
 * 对象逃逸分析与栈上分配
 */
public class EscapeAnalyse {
    public static Object obj;

    public static void main(String[] args) {
        //DOTO
    }

    /**
     * 未逃逸
     */
    public void test() {
        Object object = new Object();
    }

    /**
     * 逃逸
     */
    public void test2() {
        obj = new Object();
    }

    /**
     * 逃逸
     *
     * @return
     */
    public Object test3() {
        return new Object();
    }
}
