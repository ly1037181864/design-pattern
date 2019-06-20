package cn.topideal.com.design.singleton;

import java.io.Serializable;

/**
 * 懒汉式单例模式
 */
public class LazySingleton implements Serializable {

    private static LazySingleton singleton;

    private LazySingleton() {
    }

    /**
     * 存在线程安全问题
     *
     * @return
     */
    public static LazySingleton getInstance() {
        if (singleton == null)
            singleton = new LazySingleton();
        return singleton;
    }

    /**
     * double check机制
     * 尽管JDK1.6以后对synchronized关键字进行了优化，但synchronized毕竟是重锁
     * 且加锁的对象是类，某种情况下也会影响性能
     * 另一个需要注意的是对new关键字虚拟机通常是三步处理，第一步在内存中开辟内存空间 第二步对对象进行初始化 最后将内存地址赋值给该对象
     * 但是在多线程环境中，jvm会对程序进行优化，对程序指令进行重排序，导致第二步可能会晚于第三步，这就造成了尽管全局范围内该对象是单例
     * 的，但该对象却未初始化。因此需要在变量申明式加上volatile关键字，禁止程序指令重排序
     *
     * @return
     */
    public static LazySingleton getInstance2() {
        if (singleton == null) {
            synchronized (LazySingleton.class) {
                if (singleton == null)
                    singleton = new LazySingleton();
            }
        }

        return singleton;
    }

    //重写该方法ObjectStreamClass构造函数初始化的
    private Object readResolve() {
        return singleton;
    }
}
