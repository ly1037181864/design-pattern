package cn.topideal.com.design.template;

/**
 * 旅客
 * 该例子可能举的不好，造成了Passenger变成一个个对象，而不是某一类具有相同特征的类
 * 可以参考顾客到商店购买东西这一流程，每一位顾客到商店去购买商品，都是从挑选、下单、支付、提货完成购物，
 * 那么就可以抽象一个父类，定义这个流程，至于不同的客户有不同的支付方法，可以交由子类去完成
 */
public class Passenger extends TrainTraveler {

    @Override
    protected void begintravel() {
        System.out.println("现在是中午12点了，开往北京方向的K9094已经开始检票了");
        System.out.println("我准备上车出发了，北京见");
    }

    @Override
    protected void buyTickets() {
        System.out.println("我是旅客A");
        System.out.println("我的出发地是广东广州");
        System.out.println("我要去的地方是北京");
        System.out.println("我的车次是K9094 中午12点发车，后天中午12点到北京西站");
    }
}
