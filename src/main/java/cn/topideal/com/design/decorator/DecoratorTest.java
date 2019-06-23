package cn.topideal.com.design.decorator;

import java.io.*;

public class DecoratorTest {
    public static void main(String[] args) {
        //test1();
        //test2();
    }

    public static void test2() {
        Person p = new Person();
        String path = "/Users/liuyou/topideal/design-pattern/src/main/java/cn/topideal/com/design/decorator/a.txt";
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void test1() {
        Person p = new Person();
        System.out.println(p.disguise() + "\n" + p.getMsg());
        TecherDis dis = new TecherDis(p);
        System.out.println(dis.disguise() + "\n" + dis.getMsg());

        SonDis sonDis = new SonDis(dis);

        System.out.println(sonDis.disguise() + "\n" + sonDis.getMsg());
    }
}
