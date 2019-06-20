package cn.topideal.com.design.factory.abstractfactory;

import cn.topideal.com.design.factory.entity.IProduct;

/**
 * 抽象工厂测试
 * 产品族
 * 产品等级
 */
public class AbstractFactoryTest {


    /**
     * 抽象工厂优点是将所有对象的创建都几种到一个工厂类中，避免了重复的造轮子
     * 但是同时也带来了弊端，那就是如果业务需求增加，必然导致整个工厂类的子类都要相应的修改
     * 这一修改也就违背了开闭原则，为后续的代码增加了不稳定因素
     * 可以看出抽象工厂也运用了简单工厂的逻辑思维
     * 对于实际的业务中是使用抽象工厂还是简单工厂，则需要取决与实际的业务需求等
     *
     * @param args
     */
    public static void main(String[] args) {
        IAbstractFactory bmFactory = new BmAbstractFactory();
        IProduct bmTyre = bmFactory.createTyre();
        System.out.println(bmTyre.getName());

        IProduct bmChassis = bmFactory.createChassis();
        System.out.println(bmChassis.getName());

        IProduct bmEngine = bmFactory.createEngine();
        System.out.println(bmEngine.getName());


        IAbstractFactory qrFactory = new QrAbstractFactory();
        IProduct QrTyre = qrFactory.createTyre();
        System.out.println(QrTyre.getName());

        IProduct QrChassis = qrFactory.createChassis();
        System.out.println(QrChassis.getName());

        IProduct QrEngine = qrFactory.createEngine();
        System.out.println(QrEngine.getName());

    }

}
