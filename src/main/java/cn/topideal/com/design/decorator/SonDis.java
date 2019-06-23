package cn.topideal.com.design.decorator;

public class SonDis extends PersonDecorator {

    public SonDis(Person person) {
        super(person);
    }

    @Override
    public void doSomeOhther() {

    }

    @Override
    public String getMsg() {
        return super.getMsg() + "\t我的责任是赡养父母";
    }

    @Override
    public String disguise() {
        return super.disguise() + "\t我是父母的儿子";
    }
}
