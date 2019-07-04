package cn.topideal.com.design.adapter.c3;

import java.util.concurrent.Callable;

public class RunnableAdapter implements Runnable {

    private Callable callable;

    public RunnableAdapter(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        if (callable != null) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
