package cn.topideal.com.design.adapter.c2.v3.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;

public class UnionPayAdapter implements IPayAdapter {

    @Override
    public boolean support(Object adapter) {
        return adapter instanceof UnionPayAdapter;
    }

    @Override
    public ResultMsg pay(IPayAdapter payAdapter, double num) {
        return new ResultMsg(200, "支付成功，感谢使用银联支付", num);
    }
}
