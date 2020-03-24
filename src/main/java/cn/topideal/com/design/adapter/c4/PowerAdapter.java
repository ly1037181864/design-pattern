package cn.topideal.com.design.adapter.c4;

import cn.topideal.com.design.adapter.c1.AC5;

/**
 * @program: design-pattern
 * @description
 * @author: liuyou
 * @create: 2020-03-17 20:52
 **/
public class PowerAdapter extends AC220 implements Ac5Handler {
    @Override
    public AC5 changePower() {
        int power = getPower();
        return new AC5(power / 44);
    }
}
