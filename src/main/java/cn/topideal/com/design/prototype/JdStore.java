package cn.topideal.com.design.prototype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 原型模式
 * 购物平台
 * <p>
 * 用户A购买了京东的一些商品，觉得好用，过一点时间又来购买了
 * 并推荐了客户B购买这些商品
 */
public class JdStore {

    public Order shop(Consumer consumer) {
        if (consumer != null) {
            Order order = new Order();
            order.setName(consumer.getName());
            order.setAddrss(consumer.getAddress());
            order.setPhone(consumer.getPhone());
            order.setGoods(consumer.getGoods());
            order.setNum(200);
            order.setCreateTime(new Date());
            System.out.println("用户已下单，请尽快处理订单信息！" + order.toString());
            return order;
        }
        return null;
    }

    public static void main(String[] args) throws CloneNotSupportedException, InterruptedException {
        //DOTO
        Consumer consumerA = new Consumer();
        consumerA.setName("liuyou");
        consumerA.setAddress("广东深圳");
        consumerA.setPhone("13433678790");
        Goods good = new Goods();
        good.setName("Mac");
        good.setNum(20048);
        List<Goods> goods = new ArrayList<>(1);
        goods.add(good);
        consumerA.setGoods(goods);

        //京东商城购买商品
        JdStore store = new JdStore();
        Order orderA = store.shop(consumerA);

        //模拟过一段时间
        TimeUnit.SECONDS.sleep(3);

        //过一段时间A推荐B去购买了
        Consumer consumerB = (Consumer) consumerA.clone();

        consumerB.setName("moqingqing");
        consumerB.setPhone("13345768976");
        Order orderB = store.shop(consumerB);
        System.out.println(orderA.getGoods() == orderB.getGoods());

        Consumer consumerC = (Consumer) consumerB.deepClone();
        consumerC.setName("zhansan");
        consumerC.setPhone("13246786768");
        System.out.println(consumerC.getGoods() == consumerB.getGoods());

    }
}
