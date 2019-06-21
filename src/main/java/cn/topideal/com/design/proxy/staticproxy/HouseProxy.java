package cn.topideal.com.design.proxy.staticproxy;

/**
 * 房屋中介 聚合模式
 */
public class HouseProxy extends Renter {

    @Override
    public void rentHouse() {
        System.out.println("我这有几套房子，你有什么需求");
        super.rentHouse();
        System.out.println("觉得合适就租下来吧，过后又得涨价了");
    }
}
