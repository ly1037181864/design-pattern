package cn.topideal.com.design.observer.v1;

import java.util.Vector;

public class Observable {
    private boolean sub;
    private Vector<Observer> observers;

    protected Observable() {
        observers = new Vector<>();
    }

    public void notifyIfHas(Order order) {
        if (!sub)
            return;

        unSubscribe();
        observers.forEach(observer -> {
            observer.recieveMsg(order);
        });
    }

    public void addObserver(Observer order) {
        observers.add(order);
    }

    private void unSubscribe() {
        sub = false;
    }

    public void subscribe() {
        sub = true;
    }

}
