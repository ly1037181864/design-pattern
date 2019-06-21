package cn.topideal.com.design.proxy.dynamicproxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB代理
 */
public class CglibProxyFactory implements MethodInterceptor {


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("我们的平台上有很多信息！！！");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("你的信息我们已经收到了！！！");
        return result;
    }

    /**
     * 生产代理类
     *
     * @return
     */
    public Object getCglibProxy() {
        String pcgName = "/Users/liuyou/topideal/design-pattern/target/classes";
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, pcgName);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Employee.class);
        enhancer.setCallback(this);
        return enhancer.create();
    }
}
