package cn.topideal.com.design.proxy.dynamicproxy;

public class ProxyTest {


    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
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
