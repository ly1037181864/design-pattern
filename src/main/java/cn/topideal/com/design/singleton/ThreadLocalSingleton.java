package cn.topideal.com.design.singleton;

/**
 * 注册式单例
 * 实现线程范围内的线程安全
 */
public class ThreadLocalSingleton {

    private final static ThreadLocal<ThreadLocalSingleton> singletonThreadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new ThreadLocalSingleton();
        }
    };

    private ThreadLocalSingleton() {
    }

    public static ThreadLocalSingleton getInstance() {
        return singletonThreadLocal.get();
    }
}
