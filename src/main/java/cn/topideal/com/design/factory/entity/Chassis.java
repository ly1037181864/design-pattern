package cn.topideal.com.design.factory.entity;

/**
 * 底盘
 */
public class Chassis extends Product implements IProduct {

    public Chassis(String proName) {
        super(proName);
    }

    public String getName() {
        return proName;
    }
}
