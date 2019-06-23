package cn.topideal.com.design.template;

/**
 * 火车旅行出行
 */
public abstract class TrainTraveler {

    protected void travel() {
        //1 购票
        buyTickets();
        //2 验票进站
        gotoStationAndCheckRailwayTicket();
        //3 候车
        waitInStation();
        //4 上车
        begintravel();
    }

    /**
     * 上车开始旅行，不同乘客由于目的地不同，上车时间车次等不尽相同
     */
    protected abstract void begintravel();

    private void waitInStation() {
        System.out.println("KXXX 开点 XX:XX 正在候车");
    }

    private void gotoStationAndCheckRailwayTicket() {
        System.out.println("请出示您的有效证件及车票信息以便查验，谢谢您的配合");

        System.out.println("证件通过，可以过安检了");

        System.out.println("请将您的行李箱让乳X光机，过机安检，谢谢您的配合");
    }

    /**
     * 不同的人买票时间，身份信息，出发地目的地都不尽相同，所以交给子类实现
     */
    protected abstract void buyTickets();
}
