package cn.topideal.com.multithread.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池的原理分析
 * public ThreadPoolExecutor(
 * int corePoolSize,                    //核心线程数
 * int maximumPoolSize,                 //最大线程数
 * long keepAliveTime,                  //非核心线程存活时间
 * TimeUnit unit,                       //存活单位
 * BlockingQueue<Runnable> workQueue,   //阻塞队列
 * ThreadFactory threadFactory,         //线程池工厂
 * RejectedExecutionHandler handler     //拒绝策略
 * )
 * <p>7大参数
 * corePoolSize     //核心线程数--正式员工 线程池中的常驻核心线程数
 * maximumPoolSize  //最大线程数  线程池能够容纳同时执行的最大线程数，此值必须大于等于1 maximumPoolSize-corePoolSize=非核心线程数--临时工
 * keepAliveTime    //非核心线程存活时间  临时工的存活时间，过期后将自动销毁 多余的空闲线程的存活时间，当前线程池数量超过corePoolSize时，当空闲时间达到keepAliveTime值时，多余空闲线程会被销毁直到只剩下corePoolSize个线程为止
 * unit             //非核心线程存活时间单位
 * workQueue        //阻塞队列
 * threadFactory    //线程池工厂 表示生成线程池中工作线程的线程工厂，用于创建线程一般用默认的即可
 * handler          //拒绝策略 4种拒绝策略 表示当队列满了并且工作线程大于等于线程池的最大线程（maximumPoolSize）时如何来拒绝
 * <p>4大拒绝策略 (JDK默认的四种策略)
 * AbortPolicy(默认)      报异常RejectedExecutionException，阻止系统正常运行
 * DiscardOldestPolicy   抛弃队列中等待最久的任务，然后把当前任务加入队列中尝试再次提交当前任务
 * CallerRunsPolicy      用调用者所在的线程处理任务，调用者运行一种调节机制，该策略即不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新的任务流量
 * DiscardPolicy         直接丢弃任务，不予任何处理也不抛出异常，如果允许任务丢失，这是最好的一种方案。
 */
public class ThreadPoolsAnalyse {
    public static ExecutorService executorService;

    public static void main(String[] args) {
        int RUNNING = -1 << 29;

        System.out.println((RUNNING | 0));//1010 0000 0000 0000 0000 0000 0000 0000
        System.out.println((1 << 29));

        //1010 0000 0000 0000 0000 0000 0000 0000 源码
        //1101 1111 1111 1111 1111 1111 1111 1111 反码
        //1110 0000 0000 0000 0000 0000 0000 0000 补码

        int CAPACITY = (1 << 29) - 1;
        System.out.println((RUNNING & ~CAPACITY));

        //getFixedThreadPool(2);
        //getCachedThreadPool();
        getDefinedThreadPool(1, 3, 1, TimeUnit.SECONDS);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "\t" + UUID.randomUUID().toString().substring(0, 8));
//                try {
//                    TimeUnit.SECONDS.sleep(3600);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        };

        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.submit(runnable);

        executorService.isTerminated();

        executorService.shutdown();
        executorService.shutdownNow();
    }

    /**
     * 自定义线程池
     * ThreadPoolExecutor(int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler)
     */
    public static void getDefinedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                new ArrayBlockingQueue<>(2), new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build());
    }

    public static void getFixedThreadPool(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize == 0 ? 5 : poolSize);
    }

    public static void getCachedThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public static void getSingleThreadPool() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static void getScheduledThreadPool(int poolSize) {
        executorService = Executors.newScheduledThreadPool(5);
    }

    /**
     * 自定义线程工厂
     */
    static class DefinedThreadFactory implements ThreadFactory {

        private String namePrefix;
        private AtomicInteger integer = new AtomicInteger(1);

        public DefinedThreadFactory(String groupName) {
            this.namePrefix = "DefinedThreadFactory-" + groupName + "-Worker-";
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + integer.getAndIncrement());
        }
    }
}
