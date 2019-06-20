package cn.topideal.com.design.singleton;

import java.io.Serializable;

/**
 * 静态内部类实现单例模式 史上最安全的单例
 */
public class LazyStaticSingleton implements Serializable {

    private LazyStaticSingleton() {
    }

    public static class StaticSingleton {
        private static final LazyStaticSingleton singleton = new LazyStaticSingleton();

    }

    public static LazyStaticSingleton getInstance() {
        return StaticSingleton.singleton;
    }

    //重写该方法ObjectStreamClass构造函数初始化的
    private Object readResolve() {
        return StaticSingleton.singleton;
    }
}
