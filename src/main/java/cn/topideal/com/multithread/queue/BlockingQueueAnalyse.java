package cn.topideal.com.multithread.queue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列分析
 * ArrayBlockingQueue       由数组组成的有界阻塞队列
 * LinkedBlockingQueue      由链表结构组成的有界(但大小默认值为Integer.MAX_VALUE)阻塞队列
 * SynchronousQueue         不存储元素的阻塞队列，也即单个元素的队列
 * PriorityBlockingQueue    支持优先级排序的无界阻塞队列
 * DelayQueue               使用优先级队列实现的延迟误解阻塞队列
 * LinkedTransferQueue      由链表结构组成的无界阻塞队列
 * LinkedBlockingDeque      由链表结构组成的双向阻塞队列
 * <p>
 * add(e):添加元素,成功返回true,当队列已满时报异常IllegalStateException("Queue full")
 * remove():从队首移除元素，按先进先出，如果队列已空报NoSuchElementException
 * element():检查队首元素
 * <p>
 * offer(e):添加元素,成功返回true,失败返回false
 * poll():从队首开时移除元素,队首已空时返回null
 * peek():检查队首元素
 * <p>
 * put(e):添加元素，当队列已满时阻塞
 * take():从队首移除元素，当队列为空时阻塞
 * <p>
 * offer(e, time,unit):添加元素,当队列已满时,阻塞指定时间后退出
 * poll(time, unit):从队首移除元素,当队列已空时,阻塞指定时间后退出
 */
public class BlockingQueueAnalyse {
    public static void main(String[] args) {
        //BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        //BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(10);
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
        blockingQueueTest(blockingQueue);
    }

    private static void blockingQueueTest(BlockingQueue<String> blockingQueue) {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String str = UUID.randomUUID().toString().substring(0, 8);
                blockingQueue.offer(str);
                try {
                    Random random = new Random();
                    int sleep = random.nextInt(5);
                    TimeUnit.MILLISECONDS.sleep((sleep + 1) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println(blockingQueue.take() + "\t" + blockingQueue.size());
                    Random random = new Random();
                    int sleep = random.nextInt(5);
                    TimeUnit.MILLISECONDS.sleep((sleep + 1) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
