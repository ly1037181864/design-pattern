package cn.topideal.com.design.factory.simplefactory;

import cn.topideal.com.design.factory.entity.BmCar;
import cn.topideal.com.design.factory.entity.ICar;

/**
 * 由BmSimpleCarFactory实现ISimpleCarFactory具体的代码逻辑 专业的人干专业的事
 */
public class BmSimpleCarFactory implements ISimpleCarFactory{
    public ICar createCar() {
        return new BmCar();
    }
}
