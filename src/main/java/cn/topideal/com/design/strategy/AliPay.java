package cn.topideal.com.design.strategy;

/**
 * 支付宝
 */
public class AliPay implements IPay {

    @Override
    public String pay(Order order) {
        String name = order.getName();
        double num = order.getNum();
        System.out.println("订单日志:" + order.toString());
        if (num > 500)
            return "尊敬的用户【" + name + "】您的余额不足以支付订单金额，支付失败，本次交易结束，感谢您的购买";

        return "支付成功，尊敬的用户【" + name + "】我们已经收到您的订单了，会尽快处理的，感谢您的购买";
    }
}
