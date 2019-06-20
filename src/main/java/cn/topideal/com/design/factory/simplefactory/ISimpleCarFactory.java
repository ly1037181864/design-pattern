package cn.topideal.com.design.factory.simplefactory;


import cn.topideal.com.design.factory.entity.ICar;

/**
 * 简单工厂的升级版
 * 通过定义接口，定义规范，由具体的实现自类来实现规范定义，最终获得实现逻辑
 */
public interface ISimpleCarFactory {

    /**
     * 生产汽车
     * @return
     */
    ICar createCar();
}
