package cn.topideal.com.design.delegate;

/**
 * 技术主管
 */
public class TechnologyManager implements Manager {

    @Override
    public String doWork(WorkTask task) {
        System.out.println("老板交代给我的任务是[" + task.getName() + "]要求我们[" + task.getDay() + "]天完成！！！");
        System.out.println(task.getDay() + "天过去了，这个任务有难度暂时可能还需要几天才能完成");
        return "老板这个任务有难度，需要过两天才能解决";
    }
}
