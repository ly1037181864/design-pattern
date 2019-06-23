package cn.topideal.com.design.adapter.c2.v3;

import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * 新的交易方式，要支持原有的现金交易同时还要支持支付宝、银联、微信等交易方式
 */
public class PayServiceForThird extends PayService {

    @Override
    public ResultMsg payForCash(double num) {
        return super.payForCash(num);
    }

    /**
     * 扩展的支付宝支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForAliPay(double num) {
        return new ResultMsg(200, "支付成功，感谢使用支付宝", num);
    }

    /**
     * 扩展的京东白条
     *
     * @param num
     * @return
     */
    public ResultMsg payForJdPay(double num) {
        return new ResultMsg(200, "支付成功，感谢使用京东白条", num);
    }

    /**
     * 扩展微信支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForWechatPay(double num) {
        return new ResultMsg(200, "支付成功，感谢使用微信支付", num);
    }

    /**
     * 扩展银联支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForUnionPay(double num) {
        return new ResultMsg(200, "支付成功，感谢使用银联支付", num);
    }
}
