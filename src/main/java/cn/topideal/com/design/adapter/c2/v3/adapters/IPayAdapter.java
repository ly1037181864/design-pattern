package cn.topideal.com.design.adapter.c2.v3.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;

public interface IPayAdapter {
    boolean support(Object adapter);

    ResultMsg pay(IPayAdapter payAdapter, double num);
}
