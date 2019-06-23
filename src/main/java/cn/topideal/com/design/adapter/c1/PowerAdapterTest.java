package cn.topideal.com.design.adapter.c1;

public class PowerAdapterTest {

    public static void main(String[] args) {
        AC220 ac220 = new AC220(220);
        System.out.println("我是" + ac220.getPower() + "V电压");
        PowerAdapter powerAdapter = new PowerAdapter();
        AC5 ac5 = powerAdapter.changePower(ac220);
        System.out.println("我是" + ac5.getPower() + "V电压");
    }
}
