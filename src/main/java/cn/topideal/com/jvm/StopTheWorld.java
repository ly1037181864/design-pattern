package cn.topideal.com.jvm;

import java.util.HashMap;

public class StopTheWorld {
    public static class Mythread extends Thread {
        HashMap map = new HashMap();

        @Override
        public void run() {
            try {
                while (true) {
                    if (map.size() * 512 / 1024 / 1024 >= 900) {
                        map.clear();
                        System.out.println("clear map");
                    }
                    byte[] b1;
                    for (int i = 0; i < 100; i++) {
                        b1 = new byte[512];
                        map.put(System.nanoTime(), b1);
                    }
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PrintThread extends Thread {
        public static final long start = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - start;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        Mythread aa = new Mythread();
        PrintThread bb = new PrintThread();
        aa.start();
        bb.start();
    }
}
