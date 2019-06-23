package cn.topideal.com.design.adapter.c2.v2.adapters;

import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * Created by Tom on 2019/3/16.
 */
public interface RegistAdapter {
    boolean support(Object adapter);

    ResultMsg login(String id, Object adapter);
}
