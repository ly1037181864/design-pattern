package cn.topideal.com.design.adapter.c3;

import java.util.concurrent.Callable;

public class ThreadAdapterFactory {

    public static Runnable getRunnableTask(Callable callable) {
        return new RunnableAdapter(callable);
    }
}
