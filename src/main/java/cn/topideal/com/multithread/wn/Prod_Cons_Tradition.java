package cn.topideal.com.multithread.wn;

/**
 * 消费这生产者传统版模式
 * 三个线程
 * AA线程打印5遍 BB线程打印10遍 CC线程打印15遍
 * 然后依次循环5遍
 */
public class Prod_Cons_Tradition {
    private static final int XHC = 5;//总的循环次数
    private static final int XHC_A = 5;//线程A总的循环次数
    private static final int XHC_B = 10;//线程B总的循环次数
    private static final int XHC_C = 15;//线程C总的循环次数
    private int printNum = 1;// 1 AA线程工作 2 BB线程工作 3 CC线程工作

    /**
     * @param num
     */
    public synchronized void print(int num) {
        try {
            /**
             * 此种写法的错误之处在于while (!needWorking)是去判断needWorking这个变量，而不是printNum == num
             * 因为在多线程环境中printNum是在随时发生变化的，当某个线程被阻塞后再次唤醒时needWorking值是不变的，也就会导致它继续
             * 阻塞，而实际情况是printNum这个值已经发生了变化，那么printNum == num也会发生变化，因此needWorking这个值也必须要
             * 发生变化，否则就会出现，某个线程会唤醒，但needWorking没变，导致继续阻塞，从而发生死锁
             */
//            boolean needWorking = printNum == num;
//            while (!needWorking) {
//                wait();
//            }

            //判断当前是否需要阻塞
            while (printNum != num) {
                wait();
            }

            //打印
            printWork(num);

            printNum = num == 1 ? 2 : num == 2 ? 3 : 1;

            //唤醒其他工作线程
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printWork(int num) {
        int count = num == 1 ? XHC_A : num == 2 ? XHC_B : XHC_C;
        for (int i = 1; i <= count; i++) {
            System.out.println(Thread.currentThread().getName() + "\t 打印第" + i + "次");
        }
        if (num == 3)
            System.out.println("=================================");
    }

    public static void main(String[] args) {

        Prod_Cons_Tradition prod_cons_tradition = new Prod_Cons_Tradition();
        new Thread(() -> {
            for (int i = 0; i < XHC; i++) {
                prod_cons_tradition.print(1);
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < XHC; i++) {
                prod_cons_tradition.print(2);
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 0; i < XHC; i++) {
                prod_cons_tradition.print(3);
            }
        }, "CC").start();
    }
}
