package cn.topideal.com.design.decorator;

public class Person {

    public String getMsg() {
        return "我的责任是建设社会主义中国";
    }

    /**
     * 装扮
     *
     * @return
     */
    public String disguise() {
        return "我是一名普通的社会主义劳动者";
    }
}
