package cn.topideal.com.design.decorator;

public abstract class PersonDecorator extends Person {
    protected Person person;

    public PersonDecorator(Person person) {
        this.person = person;
    }

    public abstract void doSomeOhther();

    @Override
    public String getMsg() {
        return person.getMsg();
    }

    @Override
    public String disguise() {
        return person.disguise();
    }
}
