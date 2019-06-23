package cn.topideal.com.design.observer.v1;

import cn.topideal.com.design.observer.v3.Sellerg;
import com.google.common.eventbus.EventBus;

/**
 * 京东平台
 */
public class JdPlatform extends Observable {
    private String pName = "京东自营";
    private static JdPlatform platform;
    //消息总线
    private static final EventBus eventBus = new EventBus();

    private JdPlatform() {
        super();
    }

    public static JdPlatform getInstance() {
        if (platform == null) {
            platform = new JdPlatform();
        }
        return platform;
    }

    public void notifyIfHas(Consumer consumer) {
        System.out.println(consumer.getName() + "在" + pName + "设置了到货提醒");

        Order order = new Order();
        order.setCusName(consumer.getName());
        order.setPhone(consumer.getPhone());
        order.setAddress(consumer.getAddress());
        order.setGoodsList(consumer.getGoodsList());
        subscribe();
        notifyIfHas(order);
    }

    public void register(Sellerg seller) {
        eventBus.register(seller);
    }

    public void post(Consumer consumer) {
        System.out.println(consumer.getName() + "在" + pName + "设置了到货提醒");

        Order order = new Order();
        order.setCusName(consumer.getName());
        order.setPhone(consumer.getPhone());
        order.setAddress(consumer.getAddress());
        order.setGoodsList(consumer.getGoodsList());
        eventBus.post(order);
    }
}
