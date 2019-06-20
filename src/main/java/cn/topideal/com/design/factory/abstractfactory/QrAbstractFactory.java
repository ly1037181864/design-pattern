package cn.topideal.com.design.factory.abstractfactory;

import cn.topideal.com.design.factory.entity.Chassis;
import cn.topideal.com.design.factory.entity.Engine;
import cn.topideal.com.design.factory.entity.IProduct;
import cn.topideal.com.design.factory.entity.Tyre;

public class QrAbstractFactory implements IAbstractFactory {

    public IProduct createTyre() {
        return new Tyre("奇瑞生产的轮胎");
    }

    public IProduct createEngine() {
        return new Engine("奇瑞生产的发动机");
    }

    public IProduct createChassis() {
        return new Chassis("奇瑞生产的底盘");
    }
}
