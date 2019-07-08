package cn.topideal.com.jvm;

import java.lang.reflect.Field;

public class ReflectDemo {
    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person("liuyou", 27, "湖南衡阳");
        Field[] fields = person.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //设置是否允许访问，不是修改原来的访问权限修饰词。
            fields[i].setAccessible(true);
            System.out.println(fields[i].getName() + ":" + fields[i].get(person));
        }
    }
}

class Person {
    private String name;
    private int age;
    private String address;

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
