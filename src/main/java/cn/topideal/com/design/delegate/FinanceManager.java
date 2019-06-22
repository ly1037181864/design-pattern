package cn.topideal.com.design.delegate;

/**
 * 财务主管
 */
public class FinanceManager implements Manager {

    @Override
    public String doWork(WorkTask task) {
        System.out.println("老板交代给我的任务是[" + task.getName() + "]要求我们[" + task.getDay() + "]天完成！！！");
        System.out.println(task.getDay() + "天过去了，我们已经完成了老板交代的任务了");
        return "老板任务完成了";
    }
}
