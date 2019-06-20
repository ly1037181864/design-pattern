package cn.topideal.com.design.singleton;

import java.io.Serializable;

/**
 * 饿汉式单例 饿汉式单例天生就是线程安全的
 * 但是也会出现序列化和反序列化以及反射破坏单例的问题
 */
public class HungerSingleton implements Serializable {

    //final关键字表示变量不能被修改
    public static final HungerSingleton singleton = new HungerSingleton();

    //反射破坏单例时
    private HungerSingleton() {
        if (singleton != null)
            throw new RuntimeException("单例以存在！");
    }

    /**
     * 饿汉式单例的另一种写法
     */
//    static {
//        singleton = = new HungerSingleton();
//    }
    public static HungerSingleton getInstance() {
        return singleton;
    }

    //重写该方法ObjectStreamClass构造函数初始化的
    private Object readResolve() {
        return singleton;
    }

}
