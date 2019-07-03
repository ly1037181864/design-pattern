package cn.topideal.com.multithread.threadpool;

public interface RejectedExecutionHandlerAnalyse {
    void rejectedExecution(Runnable r, ThreadPoolExecutorAnalyse executor);
}
