package cn.topideal.com.design.proxy.dynamicproxy;

/**
 * 员工
 */
public class Employee implements Work {

    @Override
    public void findJob() throws Throwable {
        System.out.println("我要找工作");
    }
}
