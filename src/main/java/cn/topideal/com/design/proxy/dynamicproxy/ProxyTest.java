package cn.topideal.com.design.proxy.dynamicproxy;

public class ProxyTest {


    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        Class<?>[] interfaces = Employee.class.getInterfaces();
        test5(interfaces);
    }

    public static void test5(Class<?>... ints) {
        System.out.println(ints.length);
    }


    /**
     * if (interfaces.length > 65535) {
     * throw new IllegalArgumentException("interface limit exceeded");
     * }
     * <p>
     * 编译代码后找到 public static void test4();
     * 13: invokevirtual #6                  // Method java/io/PrintStream.println:(I)V
     * #6 = Methodref          #69.#70       // java/io/PrintStream.println:(I)V
     * #69 = Class              #92           // java/io/PrintStream
     * #70 = NameAndType        #93:#94       // println:(I)V
     * #92 = Utf8               java/io/PrintStream
     * #93 = Utf8               println
     * #94 = Utf8               (I)V
     * 其中常量池中94位置的常量即我们申明的length，在字节码中它的类型是Utf8的即该变量的长度为2个字节，那么对于内存而言
     * 它最大能保存的数据是2<<16即65536，而常量池的下标又是从1开始的，因此数据最大能保存的数就是65535，这就解释了在JDK
     * 代理中为什么要求目标代理类实现的接口数不能超过65535了。
     * <p>
     * 这里需要特别注意的是区分int类型的数据长度，因为int类型的数据在内存中占据了4个字节即2<<32,而此处又说2<<16，启不相
     * 互矛盾，这里不是矛盾一说，对于计算机或者对于内存而言，没有数据类型一说，是我们的程序在运行时告诉我们的计算机需要多大
     * 的内存空间，那么计算机便在内存中开辟多大的内存空间，我们在程序设计时，会在类加载时将类的元信息加载进入内存，而这些元
     * 信息如常量池它对应了我们计算机内存中某块内存具体的信息，即加入常量池中4号常量是int类型，当我们在做入栈操作的时候，就
     * 要告诉计算机我这是int型数据，需要2<<32这么大内存，并告诉计算即我在常量池中的4号位置，那么计算机在运行的过程中就能轻
     * 松通过获取常量池中的位置来判定当前这块内存代表的数据信息，具体是哪一类型的数据。而常量池的内存也是有大小的，不可能无限
     * 制增长，也就是我们虚拟机为常量池定义了14中类型，分别是uft8_info,Integer_info,Class_info,Field_info等定义信
     * 息,这些类型同样跟我们的Java基本类型一样，同样存在大小，而uft8_info类型在内存中只占据2个字节，即2<<16。
     *
     * 在java类中存储接口数量用两个字节，一个字节占8个位置，两个字节就占了16位，
     * 二进制16位数最大是1111  1111  1111 1111 ，换算成10进制就是65535。
     */
    public static void test4() {
        Class<?>[] interfaces = Employee.class.getInterfaces();
        int length = interfaces.length;
        System.out.println(length);
    }

    /**
     * 此处还要研究
     * CGLIB的实现原理
     */
    public static void test3() {
        try {
            CglibEmployeeProxy proxy = new CglibEmployeeProxy(new Employee());
            proxy.setCallback(new CglibProxyFactory());
            proxy.findJobProxy();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * CGLIB代理
     * 底层是由ASM框架动态生成代理类，
     */
    public static void test2() {
        try {
            CglibProxyFactory factory = new CglibProxyFactory();
            Employee emProxy = (Employee) factory.getCglibProxy();
            emProxy.findJob();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * JDK动态代理
     * 底层是由Proxy实现运行是动态生产代理类
     * 通过分析动态生产代理类$Proxyo.class字节码文件
     * 我们可以得出JDK动态代理继承了Proxy类，实现了目标代理类的接口定义，并重写了接口类方法
     * 通过调用代理类的方法，而代理类又是通过super.h.invoke方法实现调用，这个super.h即
     * Proxy类中持有的InvocationHandler对象，所以也就为什么JDK动态代理要是实现
     * InvocationHandler类并重写invoke方法的原因，通过这样的方式，来是实现对目标代理类的
     * 功能增强。JDK动态代理JDK层生产Java字节码并动态加载到内存中去，提高了类的加载效率，但
     * 这也有一个缺点那就是动态反射的应用。
     */
    public static void test1() {
        try {
            //JDK动态代理
            JobProxyFactory proxy = new JobProxyFactory(new Employee());
            Work workProxy = (Work) proxy.getTargetProxy();
            workProxy.findJob();

            //生产代理类
            proxy.createProxyFile();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
