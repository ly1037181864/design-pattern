package cn.topideal.com.design.adapter.c2.v3.adapters;

public class PayServiceAdapterTest {
    /**
     * 可以理解为向下兼容，也即意味着新扩展的功能也必须要兼容旧有的功能，如支付案例，原有的现金交易功能已经不适应
     * 时代的需要，必须要扩展第三方支付方式，如微信，支付宝，银联等，但这些扩展的功能又不能影响旧有的功能，因此采
     * 用适配器模式可以很好的去扩展旧有的功能
     *
     * @param args
     */
    public static void main(String[] args) {
        IPayServiceForThird payServiceForThird = new PayServiceForThirdAdapter();
        System.out.println(payServiceForThird.payForAliPay(30));
        System.out.println(payServiceForThird.payForJdPay(50));
        System.out.println(payServiceForThird.payForWechatPay(90));
    }
}
