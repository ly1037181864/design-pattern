package cn.topideal.com.design.decorator.v2;

/**
 * @program: design-pattern
 * @description
 * @author: liuyou
 * @create: 2020-03-17 23:00
 **/
public class Decorator extends Drink {
    private Drink drink;

    public Decorator(Drink drink) {
        this.drink = drink;
    }

    @Override
    protected float cost() {
        return super.getPrice() + drink.getPrice();
    }

    @Override
    protected String desc() {
        return super.getDesc() + drink.getDesc();
    }
}
