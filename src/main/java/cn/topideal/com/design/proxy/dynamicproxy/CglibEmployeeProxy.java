package cn.topideal.com.design.proxy.dynamicproxy;

import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibEmployeeProxy extends Employee implements Factory {
    private boolean bound;
    private static final ThreadLocal threadLocal;
    private static final Callback[] callbacks;
    private MethodInterceptor interceptor;
    private static final Method findJob;
    private static final MethodProxy findJobProxy;
    private static final Object[] emptyArgs;

    public CglibEmployeeProxy(Object target) {
        this.target = target;
    }

    private Object target;

    static {
        threadLocal = new ThreadLocal();
        callbacks = null;
        Class clazzProxy = CglibEmployeeProxy.class;
        Class clazz = Employee.class;
        findJob = ReflectUtils.findMethods(new String[]{"findJob", "()V"}, clazz.getDeclaredMethods())[0];
        findJobProxy = MethodProxy.create(clazz, clazzProxy, "()V", "findJob", "findJobProxy");
        emptyArgs = new Object[0];
    }

    /**
     * 重写目标代理类的方法
     *
     * @throws Throwable
     */
    @Override
    public void findJob() throws Throwable {
        super.findJob();
    }

    /**
     * 代理类的方法
     *
     * @throws Throwable
     */
    public void findJobProxy() throws Throwable {
        MethodInterceptor var10000 = this.interceptor;
        if (var10000 == null) {
            bindCallbacks(this);
            var10000 = this.interceptor;
        }

        if (var10000 != null) {
            var10000.intercept(this, findJob, emptyArgs, findJobProxy);
        } else {
            super.findJob();
        }
    }

    private static void bindCallbacks(Object proxy) {
        CglibEmployeeProxy var1 = (CglibEmployeeProxy) proxy;
        if (!var1.bound) {
            var1.bound = true;
            Object object = threadLocal.get();
            if (object == null) {
                object = callbacks;
                if (object == null) {
                    return;
                }
            }

            var1.interceptor = (MethodInterceptor) ((Callback[]) object)[0];
        }

    }

    public void setCallback(Callback callback) {

        this.interceptor = (MethodInterceptor) callback;
    }

    @Override
    public Object newInstance(Callback callback) {
        return null;
    }

    @Override
    public Object newInstance(Callback[] callbacks) {
        return null;
    }

    @Override
    public Object newInstance(Class[] classes, Object[] objects, Callback[] callbacks) {
        return null;
    }

    @Override
    public Callback getCallback(int i) {
        return null;
    }

    @Override
    public void setCallback(int i, Callback callback) {

    }

    @Override
    public void setCallbacks(Callback[] callbacks) {

    }

    @Override
    public Callback[] getCallbacks() {
        return new Callback[0];
    }
}
