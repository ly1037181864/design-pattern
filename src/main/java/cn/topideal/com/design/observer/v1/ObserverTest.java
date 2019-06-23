package cn.topideal.com.design.observer.v1;

import java.util.ArrayList;
import java.util.List;

public class ObserverTest {
    public static void main(String[] args) {
        Goods goods = new Goods();
        goods.setName("小米9");
        goods.setNum(1999);
        List<Goods> goodsList = new ArrayList<>(1);
        goodsList.add(goods);


        JdPlatform jdPlatform = JdPlatform.getInstance();

        Seller seller = new Seller("小米自营店");

        Consumer consumer = new Consumer("Tom", "广东深圳", "13367549898", goodsList);

        jdPlatform.addObserver(seller);


        jdPlatform.notifyIfHas(consumer);

    }
}
