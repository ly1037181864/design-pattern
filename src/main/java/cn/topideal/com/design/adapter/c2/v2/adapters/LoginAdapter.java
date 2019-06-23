package cn.topideal.com.design.adapter.c2.v2.adapters;


import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * 在适配器里面，这个接口是可有可无，不要跟模板模式混淆
 * 模板模式一定是抽象类，而这里仅仅只是一个接口
 * Created by Tom on 2019/3/16.
 */
public interface LoginAdapter {
    boolean support(Object adapter);

    ResultMsg login(String id, Object adapter);

}
