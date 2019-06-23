package cn.topideal.com.design.adapter.c1;

public class PowerAdapter {

    public AC5 changePower(AC220 ac220) {
        System.out.println("开始电压转换");
        int power = ac220.getPower() / 44;
        AC5 ac5 = new AC5(power);
        System.out.println("电压转换结束");
        return ac5;
    }
}
