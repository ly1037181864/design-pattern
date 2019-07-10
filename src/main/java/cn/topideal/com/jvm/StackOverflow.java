package cn.topideal.com.jvm;

/**
 * java.lang.StackOverflowError 栈空间溢出
 */
public class StackOverflow {
    private static int count;

    /**
     * -Xss128k The stack size specified is too small, Specify at least 160k
     * -Xss256k 2589
     * -Xss180k 1103
     * -Xss256k 2078
     * 一个栈桢包含局部变量表，操作数栈，栈帧数据
     * 一个栈对应就是一个方法，局部变量表记录了入参，返回值，局部变量等，操作数栈记录的是计算的中间结果，栈帧数据保存的是方法区的指针信息，即常量池的指针
     * StackOverflowError有可能是栈的内存不够，也又可能是栈的深度太深
     * <p>
     * public static void test();
     * descriptor: ()V
     * flags: ACC_PUBLIC, ACC_STATIC
     * Code:
     * stack=2, locals=6, args_size=0 栈深2 局部变量6 入参0 栈深即一个方法一个栈帧，当虚拟机运行时会把test()压栈，操作完count++后又会把下一个test()压栈，所以栈深2
     * 0: iconst_2
     * 1: istore_0
     * 2: iconst_4
     * 3: istore_1
     * 4: bipush        10
     * 6: istore_2
     * 7: bipush        12
     * 9: istore_3
     * 10: ldc           #2                  // int 524288
     * 12: newarray       byte
     * 14: astore        4
     * 16: iload_0
     * 17: iload_1
     * 18: iadd
     * 19: iload_2
     * 20: iadd
     * 21: istore        5
     * 23: getstatic     #3                  // Field count:I
     * 26: iconst_1
     * 27: iadd
     * 28: putstatic     #3                  // Field count:I
     * 31: invokestatic  #4                  // Method test:()V
     * 34: return
     * LineNumberTable:
     * line 20: 0
     * line 21: 2
     * line 22: 4
     * line 23: 7
     * line 24: 10
     * line 25: 16
     * line 26: 23
     * line 27: 31
     * line 28: 34
     * LocalVariableTable: 局部变量表
     * Start  Length  Slot  Name   Signature  Slot槽位
     * 2      33     0     a   I              第0号槽位a int型
     * 4      31     1     b   I              第1号槽位b int型
     * 7      28     2     c   I              第2号槽位c int型
     * 10      25     3     d   I             第3号槽位d int型
     * 16      19     4     e   [B            第4号槽位e byte型
     * 23      12     5     f   I             第5号槽位f int型
     */
    public static void test() {//栈深 第一个把自己押入栈底
        int a, b, c, d;
        a = 2;
        b = 4;
        c = 10;
        d = 12;
        byte[] e = new byte[512 * 1024];
        int f = a + b + c;
        count++;
        test();//最后把下一个test压入栈顶
    }

    public static void main(String[] args) {
        try {
            test();
        } catch (Throwable e) {//这里不能用Exception捕获，是因为Error和Exception都是继承自Throwable，
            // 但是两者又有所区别，Error更倾向于系统底层级别的错误，如果操作系统异常，虚拟机异常等，造成程序不可能通过恢复来继续运行，只能退出运行
            // 而Exception只是因为程序在设计上或数据等不符合要求而造成的异常，这种异常可以通过排查和数据校验，甚至异常捕获做对应的逻辑处理，更偏向
            // 于软件设计层的异常，所以这里要用它的父类Throwable来捕获
            System.out.println(count);
            e.printStackTrace();
        }
    }
}
