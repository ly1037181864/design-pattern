package cn.topideal.com.multithread.collection;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 * List之线程不安全
 * 处理方式
 * Vector
 * Collections.synchronizedList
 * CopyOnWriteArrayList
 */
public class ArrayListUnsafe {

    public static void main(String[] args) {
        arrayListUnsafe();
        System.out.println("=========");
        synchronizedList();
        System.out.println("=========");
        vectorList();
        System.out.println("=========");
        copyOnWriteList();
    }

    public static void copyOnWriteList() {
        List<String> list = new CopyOnWriteArrayList();
        addList(list, 5, 10);
    }

    public static void vectorList() {
        List<String> list = new Vector<>(10);
        addList(list, 5, 10);
    }

    public static void synchronizedList() {
        List<String> list = Collections.synchronizedList(new ArrayList<>(10));
        addList(list, 5, 10);
    }

    private static void arrayListUnsafe() {
        List<String> list = new ArrayList<>(2);
        addList(list, 20, 10);
    }

    public static void addList(List<String> list, int cyc, int xun) {
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
                    list.add(UUID.randomUUID().toString().substring(0, 8));
                }
                System.out.println(Arrays.toString(list.toArray()) + "\t" + list.size());
            }, String.valueOf(i)).start();
        }

        while (Thread.activeCount() > 2) ;
        System.out.println(Arrays.toString(list.toArray()) + "\t" + list.size());
    }
}
