package cn.topideal.com.design.observer.v4;

public class MouseEventCallback {
    public void onClick(Event e) {
        System.out.println("===========触发鼠标单击事件==========" + "\n" + e);
    }

    public void onDoubleClick(Event e) {
        System.out.println("===========触发鼠标双击事件==========" + "\n" + e);
    }
}
