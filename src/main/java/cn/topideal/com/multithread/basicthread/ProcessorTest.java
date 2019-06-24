package cn.topideal.com.multithread.basicthread;

public class ProcessorTest {

    /**
     * 责任链
     *
     * @param args
     */
    public static void main(String[] args) {
        Request request1 = new Request("订单入库");
        Request request2 = new Request("支付单入库");
        Request request3 = new Request("运单入库");
        Request request4 = new Request("清单入库");
        Request request5 = new Request("装载单入库");
        SaveProcessor saveProcessor = new SaveProcessor(null);
        saveProcessor.start();

        PrintProcessor printProcessor = new PrintProcessor(saveProcessor);
        printProcessor.start();

        PreProcessor preProcessor = new PreProcessor(printProcessor);
        preProcessor.start();

        preProcessor.processRequest(request1);
        preProcessor.processRequest(request2);
        preProcessor.processRequest(request3);
        preProcessor.processRequest(request4);
        preProcessor.processRequest(request5);
    }
}
