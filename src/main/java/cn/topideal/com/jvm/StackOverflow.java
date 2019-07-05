package cn.topideal.com.jvm;

/**
 * java.lang.StackOverflowError 栈空间溢出
 */
public class StackOverflow {
    public static void test() {
        test();
    }

    public static void main(String[] args) {
        test();
    }
}
