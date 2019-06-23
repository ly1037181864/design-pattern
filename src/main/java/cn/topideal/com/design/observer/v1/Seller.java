package cn.topideal.com.design.observer.v1;

/**
 * 商家
 */
public class Seller implements Observer {
    private String name;

    public Seller(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void recieveMsg(Order order) {
        System.out.println("尊敬的[" + this.name + "]收到最新的订单：[" + order.toString() + "],请您尽快处理");
    }
}
