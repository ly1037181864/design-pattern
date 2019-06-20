package cn.topideal.com.design.factory.abstractfactory;

import cn.topideal.com.design.factory.entity.IProduct;

/**
 * 抽象工厂
 * 产品族：同一公司生产的各种产品，如格力生产的空调、热水器、冰箱等
 * 产品等级结构：不同公司生产的同一产品，如格力生产的空调、美的空调、小米空调等
 * 抽象工厂可以理解为同一个工厂生产各种各种的对象，同时这些对象也可以被别的工厂生产
 * 对于抽象工厂的理解，抽象工厂只是定义了产品的生产规范，也许这一产品还没有定义
 */
public interface IAbstractFactory {

    /**
     * 造轮胎
     *
     * @return
     */
    IProduct createTyre();


    /**
     * 造发动机
     *
     * @return
     */
    IProduct createEngine();

    /**
     * 造底盘
     *
     * @return
     */
    IProduct createChassis();
}
