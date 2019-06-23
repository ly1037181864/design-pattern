package cn.topideal.com.design.adapter.c2.v3;

import cn.topideal.com.design.adapter.c2.ResultMsg;

public class PayService {

    /**
     * 旧有的逻辑是现金交易
     *
     * @param num
     * @return
     */
    public ResultMsg payForCash(double num) {
        System.out.println("交易金额：" + num);
        return new ResultMsg(200, "交易成功", num);
    }
}
