package cn.topideal.com.design.factory.abstractfactory;

import cn.topideal.com.design.factory.entity.Chassis;
import cn.topideal.com.design.factory.entity.Engine;
import cn.topideal.com.design.factory.entity.IProduct;
import cn.topideal.com.design.factory.entity.Tyre;

/**
 * 抽象工厂
 */
public class BmAbstractFactory implements IAbstractFactory {

    public IProduct createTyre() {
        return new Tyre("宝马生产的轮胎");
    }

    public IProduct createEngine() {
        return new Engine("宝马生产的发动机");
    }

    public IProduct createChassis() {
        return new Chassis("宝马生产的底盘");
    }
}
