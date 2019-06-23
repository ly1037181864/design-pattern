package cn.topideal.com.design.observer.v1;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Order {
    private String cusName;
    private String phone;
    private String address;
    private List<Goods> goodsList;
}
