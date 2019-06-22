package cn.topideal.com.design.delegate;

/**
 * 委派模式
 */
public class DelegateTest {

    /**
     * 委派模式跟策略模式非常的相像，但是委派模式强调的是任务的派发，也就是说，老板根据不同的任务来安排
     * 不同的部门经理去处理，强调的是任务分派，而策略模式是根据不同的策略用不同的方案去实现，两者着重的
     * 角度不同
     *
     * @param args
     */
    public static void main(String[] args) {
        Boss boss = new Boss();
        //制定任务
        WorkTask task = new WorkTask(Boss.JS_WORK, "MQ出问题了", 1);

        boss.allocateWork(task);


        //过一会财务又有事
        task = new WorkTask(Boss.CW_WORK, "这两天把这个月的账单信息弄好", 3);

        boss.allocateWork(task);
    }
}
