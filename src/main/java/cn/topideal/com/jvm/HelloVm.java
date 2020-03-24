package cn.topideal.com.jvm;

public class HelloVm {
    private int a = 2;
    private int b = 3;
    private static int c;
    private static int d = 12;
    private static final String str = "abc";
    private static String name;

    public void print() {
        //int w = a + b;
        //int n = c + d;

        //System.out.println(str + name);
        name = "cde";
        System.out.println(e);

    }

    public void add() {
        int k = 2;
        int l = 5;
        System.out.println(k + l);
    }

    /**
     * public void print();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC
     *     Code:
     *       stack=2, locals=1, args_size=1
     *          0: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          3: getstatic     #4                  // Field e:I
     *          6: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
     *          9: return
     *       LineNumberTable:
     *         line 16: 0
     *         line 18: 9
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0      10     0  this   Lcn/topideal/com/jvm/HelloVm;
     */


    static {
        e = 20;
        //System.out.println(e);//报错的原因在于没有getstatic指令
        //指令分析 将20压栈 然后putstatic  #15 即将常量池15号为止的常量值设置为20
        //调用 getstatic #6  PrintStream获取我们的PrintStream对象
        //调用PrintStream对象的println方法，打印变量
        //但是到这一步没有getstatic将#15的常量放入栈中，导致无法找到要打印的对象
        //System.out.println(e);
    }

    public static int e;

    /**
     * static {};
     *     descriptor: ()V
     *     flags: ACC_STATIC
     *     Code:
     *       stack=2, locals=0, args_size=0
     *          0: bipush        12
     *          2: putstatic     #5                  // Field d:I
     *          5: bipush        20
     *          7: putstatic     #15                 // Field e:I
     *         10: getstatic     #6                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *         13: getstatic     #15                 // Field e:I
     *         16: invokevirtual #16                 // Method java/io/PrintStream.println:(I)V
     *         19: return
     *       LineNumberTable:
     *         line 7: 0
     *         line 22: 5
     *         line 23: 10
     *         line 24: 19
     */

    /**
     * static {};
     *     descriptor: ()V
     *     flags: ACC_STATIC
     *     Code:
     *       stack=1, locals=0, args_size=0
     *          0: bipush        12
     *          2: putstatic     #5                  // Field d:I
     *          5: bipush        20
     *          7: putstatic     #15                 // Field e:I
     *         10: return
     *       LineNumberTable:
     *         line 7: 0
     *         line 21: 5
     *         line 23: 10
     */


}
