package cn.topideal.com.design.adapter.c2.v3.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;
import cn.topideal.com.design.adapter.c2.v3.PayService;

public class PayServiceForThirdAdapter extends PayService implements IPayServiceForThird {

    @Override
    public ResultMsg payForAliPay(double num) {
        return processPay(num, AliPayAdapter.class);
    }

    @Override
    public ResultMsg payForJdPay(double num) {
        return processPay(num, JdPayAdapter.class);
    }

    @Override
    public ResultMsg payForWechatPay(double num) {
        return processPay(num, WechatPayAdapter.class);
    }

    @Override
    public ResultMsg payForUnionPay(double num) {
        return processPay(num, UnionPayAdapter.class);
    }

    private ResultMsg processPay(double num, Class<? extends IPayAdapter> clazz) {
        try {
            IPayAdapter iPayAdapter = clazz.newInstance();
            if (iPayAdapter.support(iPayAdapter)) {
                return iPayAdapter.pay(iPayAdapter, num);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ResultMsg(400, "交易失败", null);
    }
}
