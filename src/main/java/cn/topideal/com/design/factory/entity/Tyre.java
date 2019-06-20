package cn.topideal.com.design.factory.entity;

/**
 * 轮胎
 */
public class Tyre extends Product implements IProduct {

    public Tyre(String proName) {
        super(proName);
    }

    public String getName() {
        return proName;
    }
}
