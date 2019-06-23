package cn.topideal.com.design.adapter.c2.v3.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * 支付宝支付
 */
public class AliPayAdapter implements IPayAdapter {

    @Override
    public boolean support(Object adapter) {
        return adapter instanceof AliPayAdapter;
    }

    @Override
    public ResultMsg pay(IPayAdapter payAdapter, double num) {
        return new ResultMsg(200, "交易成功，感谢使用支付宝", num);
    }
}
