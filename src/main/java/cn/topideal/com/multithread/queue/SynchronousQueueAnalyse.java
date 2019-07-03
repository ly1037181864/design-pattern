package cn.topideal.com.multithread.queue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * SynchronousQueue源码分析
 * 不存储元素的阻塞队列，也即单个元素的队列
 * <p>
 * 必须搭配put和take使用。put操作必须要配合一个take操作，否则会出现阻塞
 */
public class SynchronousQueueAnalyse {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        //System.out.println(blockingQueue.size());
        //System.out.println(blockingQueue.poll());
        //System.out.println(blockingQueue.add("a"));
        //System.out.println(blockingQueue.offer("b"));
        //System.out.println(blockingQueue.add("b"));

        new Thread(() -> {
            while (true) {
                try {
                    String str = UUID.randomUUID().toString().substring(0, 8);
                    blockingQueue.put(str);
                    Random random = new Random();
                    int sleep = random.nextInt(5);
                    TimeUnit.SECONDS.sleep(sleep + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //System.out.println(blockingQueue.take());
    }
}
