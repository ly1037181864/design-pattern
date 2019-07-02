package cn.topideal.com.multithread.lock;

//Conditon源码分析
public class ConditionSourceAnalyse {
    public static void main(String[] args) {

//        Class<ConditionSourceAnalyse> aClass = ConditionSourceAnalyse.class;
//        ConditionSourceAnalyse conditionSourceAnalyse = new ConditionSourceAnalyse();
//
//        conditionSourceAnalyse.getClass();


        System.out.println(Thread.interrupted());
        System.out.println(Thread.currentThread().isInterrupted());

    }

}
