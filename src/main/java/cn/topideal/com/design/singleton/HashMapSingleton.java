package cn.topideal.com.design.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapSingleton {

    private HashMapSingleton() {
    }

    private final static Map<String, HashMapSingleton> map = new ConcurrentHashMap<>();

    /**
     * 容器注册单例同样出现了线程安全问题是因为ConcurrentHashMap是线程安全的，但调用getInstance方法不是线程安全的
     * 采用加锁的方式
     *
     * @param name
     * @return
     */
    public static HashMapSingleton getInstance(String name) {
        if (!map.containsKey(name)) {
            //这种加锁的性能问题？本身ConcurrentHashMap内部就是加锁实现的，如何能通过不加锁的方式实现线程安全 思考
            synchronized (map) {
                if (!map.containsKey(name)) {
                    try {
                        Class clazz = Class.forName(name);
                        HashMapSingleton singleton = (HashMapSingleton) clazz.newInstance();
                        map.put(name, singleton);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return map.get(name);
    }
}
