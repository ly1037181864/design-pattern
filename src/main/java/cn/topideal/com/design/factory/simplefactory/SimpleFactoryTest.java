package cn.topideal.com.design.factory.simplefactory;

import cn.topideal.com.design.factory.entity.BmCar;
import cn.topideal.com.design.factory.entity.ICar;
import cn.topideal.com.design.factory.entity.QrCar;

public class SimpleFactoryTest {

    public static void main(String[] args){
        //test1();
        //test2();
        test3();
    }

    /**
     * 简单工厂测试
     * 缺点 需要传入名称，无法做到人为出错 不符合开闭原则
     */
    public static void test1(){
        ICar bm = SimpleFactory.createCar("Bm");
        System.out.println(bm.getName());

        ICar qr = SimpleFactory.createCar("Qr");
        System.out.println(qr.getName());
    }

    /**
     * 简单工厂测试
     * 缺点 需要传入类，无法做到人为出错 不符合开闭原则
     */
    public static void test2(){
        ICar bm = SimpleFactory.createCar(BmCar.class);
        System.out.println(bm.getName());

        ICar qr = SimpleFactory.createCar(QrCar.class);
        System.out.println(qr.getName());
    }

    /**
     * 简单工厂升级版 符合面向接口编程的逻辑，业务需求增加时，只需要增加对应的接口实现即可完成需求
     * 缺点需要增加相应的类实现 类过多也不易维护 最终升级成为我们的抽象工厂模式
     */
    public static void test3(){
        ISimpleCarFactory carFactory = new BmSimpleCarFactory();
        ICar bm = carFactory.createCar();
        System.out.println(bm.getName());

        carFactory = new QrSimpleCarFactory();
        ICar qr = carFactory.createCar();
        System.out.println(qr.getName());
    }
}
