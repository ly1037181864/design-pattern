package cn.topideal.com.design.observer.v1;

import lombok.Data;

import java.util.List;

/**
 * 消费者
 */
@Data
public class Consumer {
    private String name;
    private String address;
    private String phone;
    private List<Goods> goodsList;

    public Consumer(String name, String address, String phone, List<Goods> goodsList) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.goodsList = goodsList;
    }
}
