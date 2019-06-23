package cn.topideal.com.design.decorator;

public class TecherDis extends PersonDecorator {

    public TecherDis(Person person) {
        super(person);
    }

    @Override
    public void doSomeOhther() {

    }

    @Override
    public String getMsg() {
        return super.getMsg() + "\t我的责任是传道授业解惑";
    }

    @Override
    public String disguise() {
        return super.disguise() + "\t我是一名人民教师";
    }
}
