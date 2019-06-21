package cn.topideal.com.design.proxy.staticproxy;

/**
 * 静态代理
 * 组合：实现接口，代理类实现目标对象的接口并持有目标对象的引用
 * 聚合：继承父类，重写父类接口
 * 静态代理在业务功能不复杂，需求不大的情况下，这是最简单也是最容易实现的一种代理方式
 * 同时它也有局限性，那就是不论是聚合也好还是组合也好，都只能代理某一特定的对象，如果
 * 新增需求，就必须要新增相应的代理接口，一旦业务逻辑复杂的情况下，势必会增加类的管理
 * 负担
 */
public class StaticProxyTest {

    public static void main(String[] args) {

        HouseProxy houseProxy = new HouseProxy();
        houseProxy.rentHouse();

        CarProxy carProxy = new CarProxy(new Consumer());
        carProxy.shopCar();
    }
}
