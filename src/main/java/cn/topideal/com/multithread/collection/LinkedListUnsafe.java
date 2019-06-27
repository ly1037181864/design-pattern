package cn.topideal.com.multithread.collection;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 针对LinkedListJUC并没有提供一个安全的类
 */
public class LinkedListUnsafe {

    public static void main(String[] args) {
        LinkedListUnsafe();
        System.out.println("============");
    }


    private static void LinkedListUnsafe() {
        List<String> list = new LinkedList<>();
        addLinkedList(list, 5, 10);
    }

    private static void addLinkedList(List<String> list, int cyc, int xn) {
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

                for (int j = 0; j < xn; j++) {
                    list.add(UUID.randomUUID().toString().substring(0, 8));
                }
                System.out.println(list.toArray().toString() + "\t" + list.size());
            }, String.valueOf(i)).start();
        }

        while (Thread.activeCount() > 2) ;
        System.out.println(list.toArray().toString() + "\t" + list.size());
    }
}
