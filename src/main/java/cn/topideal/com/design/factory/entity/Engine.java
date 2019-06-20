package cn.topideal.com.design.factory.entity;

/**
 * 发动机
 */
public class Engine extends Product implements IProduct {
    public Engine(String proName) {
        super(proName);
    }

    public String getName() {
        return proName;
    }
}
