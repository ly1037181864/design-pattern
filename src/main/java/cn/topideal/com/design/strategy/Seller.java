package cn.topideal.com.design.strategy;

import java.util.Date;
import java.util.List;

/**
 * 商家
 */
public class Seller implements IShop {

    public static final String AL_PAY = "AliPay";
    public static final String JD_PAY = "JdPay";
    public static final String UL_PAY = "UnPay";

    @Override
    public String sellGoods(Consumer consumer) {
        Order order = new Order();
        order.setPhone(consumer.getPhone());
        order.setName(consumer.getName());
        order.setAddress(consumer.getAddress());
        String type = consumer.getType();
        order.setType(type);
        order.setCreatime(new Date());
        List<Goods> goodsList = consumer.getGoodsList();
        order.setGoodsList(goodsList);
        double num = 0;
        for (Goods goods : goodsList) {
            num += goods.getNum();
        }
        order.setNum(num);

        IPay pay = null;
        //支付逻辑
        if (UL_PAY.equals(type))
            pay = new UnionPay();
        else if (JD_PAY.equals(type))
            pay = new JdPay();
        else
            pay = new AliPay();
        return pay.pay(order);
    }
}
