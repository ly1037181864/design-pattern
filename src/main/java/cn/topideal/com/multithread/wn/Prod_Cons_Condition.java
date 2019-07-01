package cn.topideal.com.multithread.wn;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消费这生产者传统版模式
 * 三个线程
 * AA线程打印5遍 BB线程打印10遍 CC线程打印15遍
 * 然后依次循环5遍
 */
public class Prod_Cons_Condition {
    private static final int XHC = 5;//总的循环次数
    private static final int XHC_A = 5;//线程A总的循环次数
    private static final int XHC_B = 10;//线程B总的循环次数
    private static final int XHC_C = 15;//
    private static ThreadLocal<Note> local = new ThreadLocal();

    private Lock lock = new ReentrantLock();
    private Condition cA = lock.newCondition();
    private Condition cB = lock.newCondition();
    private Condition cC = lock.newCondition();
    private int printNum = 1;// 1 AA线程工作 2 BB线程工作 3 CC线程工作

    public void print() {
        Note note = getNote();
        lock.lock();
        try {
            //判断当前线程是否需要阻塞
            while (printNum != note.getNum()) {
                note.getCunC().await();
            }

            //干活
            printWork();

            //改变条件
            printNum = note.getNextNum();

            //唤醒
            note.getConN().signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private Note getNote() {
        Note note = local.get();
        if (note == null) {
            String name = Thread.currentThread().getName();
            note = "AA".equals(name) ? new Note(cA, cB, 1, 2, XHC_A) : "BB".equals(name) ? new Note(cB, cC, 2, 3, XHC_B) : new Note(cC, cA, 3, 1, XHC_C);
            local.set(note);
        }
        return note;
    }

    public void printWork() {
        Note note = local.get();
        for (int i = 1; i <= note.getPrintCount(); i++) {
            System.out.println(Thread.currentThread().getName() + "\t 打印第" + i + "次");
        }
        if (note.getNum() == 3)
            System.out.println("=================================");
    }

    public static void main(String[] args) {
        Prod_Cons_Condition cons_condition = new Prod_Cons_Condition();

        new Thread(() -> {
            for (int i = 1; i <= XHC; i++) {
                cons_condition.print();
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 1; i <= XHC; i++) {
                cons_condition.print();
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 1; i <= XHC; i++) {
                cons_condition.print();
            }
        }, "CC").start();
    }

}

class Note {
    Condition cunC;//当前
    Condition conN;//下一个
    int num;
    int nextNum;
    int printCount;

    public Note(Condition cunC, Condition conN, int num, int nextNum, int printCount) {
        this.cunC = cunC;
        this.conN = conN;
        this.num = num;
        this.nextNum = nextNum;
        this.printCount = printCount;
    }

    public Condition getCunC() {
        return cunC;
    }

    public Condition getConN() {
        return conN;
    }

    public int getNum() {
        return num;
    }

    public int getNextNum() {
        return nextNum;
    }

    public int getPrintCount() {
        return printCount;
    }
}
