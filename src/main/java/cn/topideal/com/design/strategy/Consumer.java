package cn.topideal.com.design.strategy;

import lombok.Data;

import java.util.List;

/**
 * 消费者
 */
@Data
public class Consumer {
    private String name;
    private String phone;
    private String address;
    private String type;
    private List<Goods> goodsList;

    public Consumer(String name, String phone, String address, String type, List<Goods> goodsList) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = type;
        this.goodsList = goodsList;
    }
}
