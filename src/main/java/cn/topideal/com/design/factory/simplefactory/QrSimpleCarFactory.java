package cn.topideal.com.design.factory.simplefactory;

import cn.topideal.com.design.factory.entity.ICar;
import cn.topideal.com.design.factory.entity.QrCar;

public class QrSimpleCarFactory implements ISimpleCarFactory{
    public ICar createCar() {
        return new QrCar();
    }
}
