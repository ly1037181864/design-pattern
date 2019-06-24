package cn.topideal.com.multithread.basicthread;

import java.util.concurrent.LinkedBlockingDeque;

public class PrintProcessor extends Thread implements RequestProcessor {
    private static LinkedBlockingDeque<Request> linkedBlockingDeque = new LinkedBlockingDeque();
    private RequestProcessor nextProcessor;
    private volatile boolean flags = false;

    public PrintProcessor(RequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public void run() {
        while (!flags) {
            try {
                Request request = linkedBlockingDeque.take();
                System.out.println("print data:" + request.getName());
                if (nextProcessor != null) {
                    nextProcessor.processRequest(request);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processRequest(Request request) {
        if (request != null)
            linkedBlockingDeque.add(request);
    }

    public void shutDown() {
        flags = true;
    }
}
