package cn.topideal.com.design.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.concurrent.CyclicBarrier;

public class SingletonTest {

    public static void main(String[] args) {
        test1();
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
                HungerSingleton instance = HungerSingleton.getInstance();
                System.out.println(instance);

            }, String.valueOf(i)).start();
        }

    }


}