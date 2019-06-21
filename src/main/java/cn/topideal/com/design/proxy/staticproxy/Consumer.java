package cn.topideal.com.design.proxy.staticproxy;

/**
 * 汽车消费者
 */
public class Consumer implements CarShop {
    @Override
    public void shopCar() {
        System.out.println("我要买车，我的需求是。。。");
    }
}
