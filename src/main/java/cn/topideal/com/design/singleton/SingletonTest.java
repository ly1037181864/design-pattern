package cn.topideal.com.design.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CyclicBarrier;

public class SingletonTest {

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        //test4();
        //test5();
        test6();
    }

    /**
     * 容器式单例
     * ThreadLocal 线程范围内的线程安全
     */
    public static void test6() {
        ThreadLocalSingleton instance = ThreadLocalSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + "\t" + instance);
        System.out.println(Thread.currentThread().getName() + "\t" + instance);
        System.out.println(Thread.currentThread().getName() + "\t" + instance);
        SingletonThread.excute(10);
    }

    /**
     * 容器注册单例
     * 容器注册单例同样出现了线程安全问题是因为ConcurrentHashMap是线程安全的，但调用getInstance方法不是线程安全的
     */
    public static void test5() {
        SingletonThread.excute(10);
    }

    /**
     * 枚举实现单例
     * 枚举实现单例解决了序列化和反射带来的问题
     * 序列化和反序列化时枚举是通过类对象和类名来定位类的实例
     * 反射时JDK直接检测到如果时枚举类反射直接抛出异常，从JDK底层就避免了反射实例化枚举类
     */
    public static void test4() {
        EnumSingleton instance = EnumSingleton.getInstance();
        instance.setObj(new Object());

        EnumSingleton instance2 = (EnumSingleton) serializableTest(instance);
        System.out.println(instance2 == instance);

//        EnumSingleton instance3 = (EnumSingleton) invokeTest(EnumSingleton.class);
//        System.out.println(instance == instance3);

        try {
            Constructor<EnumSingleton> constructor = EnumSingleton.class.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            EnumSingleton instance3 = constructor.newInstance("tom", 666);
            System.out.println(instance == instance3);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 静态内部类实现单例
     */
    public static void test3() {
        LazyStaticSingleton instance = LazyStaticSingleton.getInstance();
        SingletonThread.excute(10);

        //反射破坏单例
        LazyStaticSingleton instance2 = (LazyStaticSingleton) invokeTest(LazyStaticSingleton.class);
        System.out.println(instance == instance2);

        //序列化破坏单例
        LazyStaticSingleton instance3 = (LazyStaticSingleton) serializableTest(instance);
        System.out.println(instance3 == instance);
    }

    /**
     * 懒汉式单例模式
     */
    public static void test2() {
        LazySingleton instance = LazySingleton.getInstance();
        SingletonThread.excute(10);

        //反射破坏单例
        LazySingleton instance2 = (LazySingleton) invokeTest(LazySingleton.class);
        System.out.println(instance == instance2);

        //序列化破坏单例
        LazySingleton instance3 = (LazySingleton) serializableTest(instance);
        System.out.println(instance3 == instance);

    }

    /**
     * 饿汉式单例
     */
    public static void test1() {
        //线程安全测试
        SingletonThread.excute(10);

        //反射破坏单例
        HungerSingleton obj = (HungerSingleton) invokeTest(HungerSingleton.class);
        System.out.println(Thread.currentThread().getName() + "\t obj=:" + obj);
        HungerSingleton singleton = HungerSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + "\t singleton=:" + singleton);
        System.out.println(obj == singleton);

        //序列化破坏单例
        HungerSingleton serobj = (HungerSingleton) serializableTest(singleton);
        System.out.println(Thread.currentThread().getName() + "\t serobj=:" + serobj);
        System.out.println(serobj == singleton);
    }

    public static Object invokeTest(Class clazz) {
        Object obj = null;
        if (clazz != null) {
            try {
                Constructor constructor = clazz.getDeclaredConstructor(null);
                constructor.setAccessible(true);
                obj = constructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static Object serializableTest(Object o) {
        if (o != null) {
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream outputStream = null;
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;

            try {

                //写入文件
                File file = new File("/Users/liuyou/topideal/a.txt");
                fileOutputStream = new FileOutputStream(file);
                outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(o);

                //读取文件
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                return objectInputStream.readObject();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return null;

    }
}

class SingletonThread {

    public static void excute(int threadNum) {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "开始运行：\t" + System.currentTimeMillis());
                //HungerSingleton instance = HungerSingleton.getInstance();
                //LazySingleton instance = LazySingleton.getInstance2();
                //HashMapSingleton instance = HashMapSingleton.getInstance(HashMapSingleton.class.getName());
                ThreadLocalSingleton instance = ThreadLocalSingleton.getInstance();
                System.out.println(Thread.currentThread().getName() + "\t" + instance);
                System.out.println(Thread.currentThread().getName() + "\t" + instance);
                System.out.println(Thread.currentThread().getName() + "\t" + instance);

            }, String.valueOf(i)).start();
        }

    }


}