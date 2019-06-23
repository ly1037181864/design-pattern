package cn.topideal.com.design.decorator;

public class FatherDis extends PersonDecorator {

    public FatherDis(Person person) {
        super(person);
    }

    @Override
    public void doSomeOhther() {

    }

    @Override
    public String getMsg() {
        return super.getMsg() + "\t我的责任是教育好孩子";
    }

    @Override
    public String disguise() {
        return super.disguise() + "\t我是一个父亲";
    }
}
