package cn.topideal.com.design.adapter.c2.v3.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;

public interface IPayServiceForThird {

    /**
     * 扩展的支付宝支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForAliPay(double num);

    /**
     * 扩展的京东白条
     *
     * @param num
     * @return
     */
    public ResultMsg payForJdPay(double num);

    /**
     * 扩展微信支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForWechatPay(double num);

    /**
     * 扩展银联支付
     *
     * @param num
     * @return
     */
    public ResultMsg payForUnionPay(double num);
}
