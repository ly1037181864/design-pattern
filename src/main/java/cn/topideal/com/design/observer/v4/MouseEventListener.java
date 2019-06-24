package cn.topideal.com.design.observer.v4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MouseEventListener {

    public Map<Method, Event> methodEventMap = new HashMap<>();


    /**
     * 添加事件源监听
     *
     * @param event
     */
    public void addListener(Event event) {
        Method method = event.getMethod();
        if (!methodEventMap.containsKey(event))
            methodEventMap.put(method, event);
    }

    /**
     * 触发事件
     *
     * @param event
     */
    public void trigger(Event event) {
        Method method = event.getMethod();
        if (methodEventMap.containsKey(method)) {
            MouseEventCallback callback = event.getCallback();
            try {
                method.invoke(callback, event);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
