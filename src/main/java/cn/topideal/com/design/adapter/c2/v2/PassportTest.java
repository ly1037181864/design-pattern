package cn.topideal.com.design.adapter.c2.v2;

/**
 * Created by Tom.
 */
public class PassportTest {

    /**
     * 可以理解为向下兼容，也即意味着新扩展的功能也必须要兼容旧有的功能，如登陆案例，原有的登陆功能已经不适应
     * 时代的需要，必须要扩展第三方登陆方式，如微信，QQ，微博等，但这些扩展的功能又不能影响旧有的功能，因此采
     * 用适配器模式可以很好的去扩展旧有的功能
     * @param args
     */
    public static void main(String[] args) {

        IPassportForThird passportForThird = new PassportForThirdAdapter();

        passportForThird.loginForQQ("");

    }

}