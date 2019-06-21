package cn.topideal.com.design.prototype;

import lombok.Data;
import lombok.ToString;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * 订单信息
 */
@Data
@ToString
public class Order implements Cloneable, Prototype {
    private String name;//购买人姓名
    private String phone;//手机号
    private String addrss;//地址
    private double num;//价格
    private Date createTime;//订单日期
    private List<Goods> goods;//购买商品信息

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 可以抽象父类采用模版方法
     *
     * @return
     */
    @Override
    public Object deepClone() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream outputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(this);

            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
