package cn.topideal.com.design.adapter.c3;

import java.util.concurrent.Callable;

public class RunnableAbleTest {
    public static void main(String[] args) {

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("我是call方法，我也能适配Thread调用");
                return null;
            }
        };

        Runnable task = ThreadAdapterFactory.getRunnableTask(callable);

        Thread aa = new Thread(task);

        aa.start();
    }

}
