package cn.topideal.com.design.proxy.staticproxy;

/**
 * 静态代理-组合模式
 */
public class CarProxy implements CarShop {
    private Consumer consumer;

    public CarProxy(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void shopCar() {
        System.out.println("请告诉我你的需求");
        consumer.shopCar();
        System.out.println("我已了解你的购车需求，你看看下面的几款车型怎么样");
    }
}
