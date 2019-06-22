package cn.topideal.com.design.strategy;

import java.util.ArrayList;
import java.util.List;

public class StrategyTest {

    /**
     * 策略模式即根据不同的策略采用不同的算法逻辑，而具体的算法逻辑用户是不需要关心的，
     * 这里需要注意区别策略模式和委派模式的区别，其实委派模式很好理解，最好的例子就是
     * 双亲委派机制，在类的加载过程中，去子类先不加载而是委托父类加载，而父类又将类的
     * 加载请求转发给父类，直到类的加载请求被转发给启动类加载器。策略模式强调的是根据
     * 不同的策略采用不同的处理方式，两者着眼的角度不同。
     *
     * @param args
     */
    public static void main(String[] args) {

        Goods gsA = new Goods();
        gsA.setName("球鞋");
        gsA.setNum(303.34);

        Goods gsB = new Goods();
        gsB.setName("小米平板4");
        gsB.setNum(1099);

        List<Goods> goodsA = new ArrayList<>(2);
        goodsA.add(gsA);
        goodsA.add(gsB);
        Consumer consumerA = new Consumer("Tom", "13375678979", "广东深圳", Seller.AL_PAY, goodsA);
        Seller seller = new Seller();
        String resultA = seller.sellGoods(consumerA);
        System.out.println(resultA);


        Goods gsC = new Goods();
        gsC.setName("华为mate30");
        gsC.setNum(5499);
        List<Goods> goodsB = new ArrayList<>(2);

        Consumer consumerB = new Consumer("Mac", "13475678988", "广东深圳", Seller.UL_PAY, goodsB);
        String resultB = seller.sellGoods(consumerB);
        System.out.println(resultB);
    }
}
