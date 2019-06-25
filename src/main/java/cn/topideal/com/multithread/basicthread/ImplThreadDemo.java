package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.*;

/**
 * 实现多线程的方式
 * 1 extend Thread
 * 2 implements Runnable
 * 3 implements Callable
 * 4 ThreadPools
 */
public class ImplThreadDemo {

    /**
     * 实现多线程的几种方式
     *
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //继承Thread类
        ThreadDemo demo = new ThreadDemo("AA");
        demo.start();

        //实现Runnable接口
        Thread thread = new Thread(new RunnableDemo());
        thread.start();

        //实现Callable接口
        CallableDemo callableDemo = new CallableDemo();
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<String> future = service.submit(callableDemo);
        service.shutdown();

        System.out.println(future.get());
    }
}

class ThreadDemo extends Thread {

    public ThreadDemo(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("通过继承Thread实现多线程");
    }
}

class RunnableDemo implements Runnable {

    @Override
    public void run() {
        System.out.println("通过实现Runnable接口实现多线程");
    }
}

class CallableDemo implements Callable<String> {

    @Override
    public String call() throws Exception {
        return "通过实现Callable接口实现多线程";
    }
}
