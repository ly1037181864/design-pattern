package cn.topideal.com.multithread.threadsafe;

/**
 * 单例的线程安全问题--懒汉式单例问题（饿汉式不存在单例问题）
 */
public class SingletonThreadSafe {
    private static SingletonThreadSafe safe = null;

    private SingletonThreadSafe() {
        System.out.println(Thread.currentThread().getName() + "构造对象");
    }

//    public static SingletonThreadSafe getInstance() {
//        if (safe == null)
//            safe = new SingletonThreadSafe();
//        return safe;
//    }

    /**
     * double check 机制
     * <p>
     * Thread-0构造对象 解决了多线程环境下可能出现的多次重复创建对象的问题，但仍然会产生线程安全问题
     * new对象的过程是分配内存地址，初始化对象，将内存地址赋值给该对象，并返回给调用方，如果在多线程环
     * 境下遇到了指令重排序，那么就会将初始化对象和将内存地址赋值给该对象进行重排序，因为两者并没有数据
     * 依赖关系，可以进行指令重排序，因此就会造成返回给调用方的对象尽管已经有内存地址了，但仍然没有进行
     * 初始化，必须要配合volatile关键字让编译器禁止指令重排序
     *
     * @return
     */
    public static SingletonThreadSafe getInstance() {
        if (safe == null) {
            synchronized (SingletonThreadSafe.class) {
                if (safe == null)
                    safe = new SingletonThreadSafe();
            }
        }
        return safe;
    }

    public static void main(String[] args) {
        //test1();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                StaticSingletonThreadSafe.getInstance();
            }).start();
        }
    }

    /**
     * Thread-0构造对象
     * Thread-4构造对象
     * Thread-3构造对象
     * Thread-2构造对象
     * Thread-1构造对象
     */
    private static void test1() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonThreadSafe.getInstance();
            }).start();
        }
    }

}

/**
 * 静态内部类实现的懒汉式单例模式，通过内部类的加载机制巧妙的避免了线程安全问题
 */
class StaticSingletonThreadSafe {

    private StaticSingletonThreadSafe() {
        System.out.println(Thread.currentThread().getName() + "构造对象");
    }

    static class StaticSingleton {
        private static StaticSingletonThreadSafe safe = new StaticSingletonThreadSafe();
    }

    public static StaticSingletonThreadSafe getInstance() {
        return StaticSingleton.safe;
    }
}