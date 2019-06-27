package cn.topideal.com.multithread.collection;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CyclicBarrier;

/**
 * set不安全
 * set和list的区别
 * <p>
 * list
 * 有序集合、允许多个元素重复、允许多个null
 * <p>
 * set
 * 无需，可以自己手动实现有序如TreeSet，不允许元素重复，不能出现多个null
 */
public class HashSetUnsafe {

    public static void main(String[] args) {
        //synchronizedAddSet();
        copyOnWriteArraySet();
    }

    public static void copyOnWriteArraySet() {
        Set<String> sets = new CopyOnWriteArraySet<>();
        addSet(sets, 5, 10);
    }

    public static void synchronizedAddSet() {
        Set<String> sets = new HashSet<>();
        addSet(Collections.synchronizedSet(sets), 5, 10);
    }

    public static void addSetsUnsafe() {
        Set<String> sets = new HashSet<>();
        addSet(sets, 5, 10);
    }

    public static void addSet(Set<String> sets, int cyc, int xun) {
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
                    sets.add(UUID.randomUUID().toString().substring(0, 8));
                }
                System.out.println(Arrays.toString(sets.toArray()) + "\t" + sets.size());
            }, String.valueOf(i)).start();
        }

        while (Thread.activeCount() > 2) ;
        System.out.println(Arrays.toString(sets.toArray()) + "\t" + sets.size());
    }
}
