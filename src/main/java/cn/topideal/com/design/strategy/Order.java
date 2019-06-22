package cn.topideal.com.design.strategy;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 订单信息
 */
@Data
@ToString
public class Order {
    String name;//用户姓名
    String address;//用户地址
    String phone;//用户手机号
    String type;//支付类型
    double num;//订单总额
    Date creatime;//下单日期
    List<Goods> goodsList;//商品信息
}
