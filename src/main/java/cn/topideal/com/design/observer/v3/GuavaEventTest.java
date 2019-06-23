package cn.topideal.com.design.observer.v3;

import cn.topideal.com.design.observer.v1.Consumer;
import cn.topideal.com.design.observer.v1.Goods;
import cn.topideal.com.design.observer.v1.JdPlatform;
import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 2019/3/17.
 */
public class GuavaEventTest {

    public static void main(String[] args) {
        //test();
        test2();
    }

    public static void test2() {
        Goods goods = new Goods();
        goods.setName("小米9");
        goods.setNum(1999);
        List<Goods> goodsList = new ArrayList<>(1);
        goodsList.add(goods);

        Consumer consumer = new Consumer("Tom", "广东深圳", "13367549898", goodsList);

        JdPlatform jdPlatform = JdPlatform.getInstance();
        jdPlatform.register(new Sellerg("小米自营店"));
        jdPlatform.post(consumer);
    }

    private static void test() {
        //消息总线
        EventBus eventBus = new EventBus();
        GuavaEvent guavaEvent = new GuavaEvent();
        eventBus.register(guavaEvent);
        eventBus.post("Tom");

        //从Struts到SpringMVC的升级
        //因为Struts面向的类，而SpringMVC面向的是方法

        //前面两者面向的是类，Guava面向是方法

        //能够轻松落地观察模式的一种解决方案
        //MQ
    }
}
