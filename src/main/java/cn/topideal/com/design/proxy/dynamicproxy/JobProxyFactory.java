package cn.topideal.com.design.proxy.dynamicproxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 工作信息平台
 */
public class JobProxyFactory implements InvocationHandler {

    private static final String proxyClassNamePrefix = "$Proxy";
    private static final AtomicLong nextUniqueNumber = new AtomicLong();
    private Object target;

    public JobProxyFactory(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我们的平台上有很多信息！！！");
        Object result = method.invoke(target, args);
        System.out.println("你的信息我们已经收到了！！！");
        return result;
    }

    /**
     * 获取代理类
     *
     * @return
     */
    public Object getTargetProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    /**
     * 生成代理对象class文件
     * JDK自带
     */
    public void createProxyFile() {
        Class<?> clazz = target.getClass();
        String proxyPkg = clazz.getPackage().getName() + ".";
        String proxyName = proxyPkg + proxyClassNamePrefix + nextUniqueNumber.getAndIncrement();
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces(), Modifier.PUBLIC);
        String fileName = "/Users/liuyou/topideal/design-pattern/target/classes/" + proxyName.replaceAll("\\.", "\\/") + ".class";
        File file = new File(fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(proxyClassFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
