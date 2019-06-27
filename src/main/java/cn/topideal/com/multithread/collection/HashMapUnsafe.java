package cn.topideal.com.multithread.collection;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * HashMap、HashTable、ConcurrentHashMap的区别
 * HashMap
 * 非线程安全的
 * 允许key、value为null
 * <p>
 * HashTable
 * 线程安全的
 * 不允许key、value为null
 * <p>
 * ConcurrentHashMap
 * 对并发做了优化
 */
public class HashMapUnsafe {
    public static void main(String[] args) {
        //addMapUnsafe();
        //synchronizedAddMap();
        //concurrentHashMap();
        hashTable();
    }

    public static void hashTable() {
        Map<String, String> map = new Hashtable<>();
        addMap(map, 20, 50);
    }


    public static void concurrentHashMap() {
        Map<String, String> map = new ConcurrentHashMap<>();
        addMap(map, 20, 50);
    }

    public static void synchronizedAddMap() {
        Map<String, String> map = new HashMap<>();
        addMap(Collections.synchronizedMap(map), 20, 50);
    }

    public static void addMapUnsafe() {
        Map<String, String> map = new HashMap<>();
        addMap(map, 20, 50);
    }

    public static void addMap(Map<String, String> map, int cyc, int xun) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(cyc);
        for (int i = 0; i < cyc; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < xun; j++) {
                    map.put(UUID.randomUUID().toString().substring(0, 8), UUID.randomUUID().toString().substring(0, 8));
                }

//                System.out.print("[");
//                for (Map.Entry entry : map.entrySet())
//                    System.out.print(entry.getKey() + "=" + entry.getValue() + "\t");
//
//                System.out.print("]");
//                System.out.print("");
            }, String.valueOf(i)).start();
        }

        while (Thread.activeCount() > 2) ;
        System.out.println(map.size());
    }
}
