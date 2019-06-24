package cn.topideal.com.design.observer.v4;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Event {
    private MouseEventCallback callback;//回调对象
    private Method method;//回调方法

    public Event(MouseEventCallback callback, Method method) {
        this.callback = callback;
        this.method = method;
    }
}
