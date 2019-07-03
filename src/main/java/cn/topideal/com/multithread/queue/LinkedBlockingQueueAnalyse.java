package cn.topideal.com.multithread.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LinkedBlockingQueue源码分析
 * 由链表结构组成的有界(但大小默认值为Integer.MAX_VALUE)阻塞队列
 */
public class LinkedBlockingQueueAnalyse {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(3);

        //System.out.println(blockingQueue.add("a"));
        //System.out.println(blockingQueue.add("b"));
        //System.out.println(blockingQueue.add("c"));
        //blockingQueue.add("d");

//        System.out.println(blockingQueue.offer("a"));
//        System.out.println(blockingQueue.offer("b"));
//        System.out.println(blockingQueue.offer("c"));
//        System.out.println(blockingQueue.offer("d"));


        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());

        //System.out.println(blockingQueue.remove()); //java.util.NoSuchElementException


        blockingQueue.put("e");
        blockingQueue.put("f");
        blockingQueue.put("g");
        //blockingQueue.put("h");//队列满时会阻塞

        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());//队列空时会阻塞


    }
}
