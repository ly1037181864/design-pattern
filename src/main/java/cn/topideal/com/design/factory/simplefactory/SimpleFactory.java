package cn.topideal.com.design.factory.simplefactory;

import cn.topideal.com.design.factory.entity.BmCar;
import cn.topideal.com.design.factory.entity.ICar;
import cn.topideal.com.design.factory.entity.QrCar;

/**
 * 简单工厂
 */
public class SimpleFactory {

    /**
     * 根据品牌生产汽车
     * 有点逻辑简单 适合简单业务代码
     * 缺点不符合开闭原则
     * @param brandName
     * @return
     */
    public static ICar createCar(String brandName){
        if("Bm".equals(brandName))
            return new BmCar();

        if("Qr".equals(brandName))
            return new QrCar();
        return null;
    }

    /**
     * 简单工厂
     * @param clazz
     * @return
     */
    public static ICar createCar(Class clazz){
        ICar car = null;
        if(clazz != null){

            try {
                car = (ICar)clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return car;
        }
        return null;
    }
}
