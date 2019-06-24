package cn.topideal.com.design.observer.v4;

import java.lang.reflect.Method;

public class Mouse extends MouseEventListener {
    private MouseEventCallback callback;

    public Mouse(MouseEventCallback callback) {
        this.callback = callback;
    }

    public void onClick() {
        System.out.println("您单击了一下鼠标");
        trigger(getEvent("onClick"));
    }

    public void onDoubleClick() {
        System.out.println("您双击了一下鼠标");
        trigger(getEvent("onDoubleClick"));
    }

    public void addClickListener() {
        addListener(getEvent("onClick"));
    }

    public void addDoubleClickListener() {
        addListener(getEvent("onDoubleClick"));
    }

    private Event getEvent(String name) {
        try {
            Method onClick = callback.getClass().getMethod(name, Event.class);
            return new Event(callback, onClick);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
