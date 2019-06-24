package cn.topideal.com.design.observer.v4;

public class MouseEventTest {
    public static void main(String[] args) {
        MouseEventCallback callback = new MouseEventCallback();
        Mouse mouse = new Mouse(callback);
        mouse.addClickListener();
        mouse.addDoubleClickListener();

        mouse.onClick();
        mouse.onDoubleClick();
    }
}
