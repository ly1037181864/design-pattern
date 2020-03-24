package cn.topideal.com.jvm;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class VolatileDemo {
    private int i = 0;
    private boolean flags = false;

    public void write() {
        i = 1;
        flags = true;
    }

    public void read() {
        if (flags)
            i += 1;
        System.out.println(i);
    }

    public static void main(String[] args) {

        VolatileDemo demo = new VolatileDemo();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            demo.read();
        }).start();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            demo.write();
        }).start();
    }
}
