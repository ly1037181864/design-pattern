package cn.topideal.com.design.observer.v3;

import cn.topideal.com.design.observer.v1.Order;
import com.google.common.eventbus.Subscribe;

public class Sellerg {
    private String name;

    public Sellerg(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Subscribe
    public void recieveMsg(Order order) {
        System.out.println("尊敬的[" + this.name + "]收到最新的订单：[" + order.toString() + "],请您尽快处理");
    }
}
