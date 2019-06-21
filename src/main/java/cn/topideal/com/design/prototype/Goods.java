package cn.topideal.com.design.prototype;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品信息
 */
@Data
public class Goods implements Serializable {
    private String name;//商品名称
    private double num;//商品价格
}
