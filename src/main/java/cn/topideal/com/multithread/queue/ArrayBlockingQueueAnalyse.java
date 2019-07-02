package cn.topideal.com.multithread.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue数组行阻塞队列
 * <p>
 * add
 * Exception in thread "main" java.lang.IllegalStateException: Queue full
 * <p>
 * offer
 * true -- false
 * <p>
 * element 队首元素
 * <p>
 * size 队列中元素的个数
 * <p>
 * take 取出元素 队列为空时，会阻塞
 * <p>
 * remove 移除队首元素 队列为空则报Exception in thread "main" java.util.NoSuchElementException
 * <p>
 * poll 队首弹出元素 队列为空返回null
 * <p>
 * peek 返回队首元素
 */
public class ArrayBlockingQueueAnalyse {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

//        blockingQueue.add("liuyou");
//        blockingQueue.add("liuyou");
//        blockingQueue.add("liuyou");
//        blockingQueue.add("liuyou");


        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d"));

        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.size());

        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.peek());

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());

        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());


        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());

    }
}
