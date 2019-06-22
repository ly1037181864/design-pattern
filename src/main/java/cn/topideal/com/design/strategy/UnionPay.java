package cn.topideal.com.design.strategy;

/**
 * 银联支付
 */
public class UnionPay implements IPay {

    @Override
    public String pay(Order order) {
        String name = order.getName();
        System.out.println("订单日志:" + order.toString());
        return "支付成功，尊敬的用户【" + name + "】我们已经收到您的订单了，会尽快处理的，感谢您的购买";
    }
}
