package cn.topideal.com.design.decorator.v2;

/**
 * @program: design-pattern
 * @description
 * @author: liuyou
 * @create: 2020-03-17 22:44
 **/
public abstract class Drink {
    protected String desc;
    private float price;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    protected abstract float cost();

    protected abstract String desc();

}
