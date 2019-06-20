package cn.topideal.com.design.factory.entity;

public abstract class Product {
    protected String proName;

    protected Product(String proName) {
        this.proName = proName;
    }
}
