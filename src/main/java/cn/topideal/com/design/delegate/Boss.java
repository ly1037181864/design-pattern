package cn.topideal.com.design.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * 老板
 */
public class Boss {
    public static final String XZ_WORK = "XZ";//行政工作
    public static final String JS_WORK = "JS";//技术工作
    public static final String CW_WORK = "CW";//财务工作
    private static final String DEFAULT_WORK = JS_WORK;//默认是技术相关问题
    private static Map<String, Manager> managerMap = new HashMap<>(3);

    static {
        managerMap.put(XZ_WORK, new AdministrationManager());
        managerMap.put(JS_WORK, new TechnologyManager());
        managerMap.put(CW_WORK, new FinanceManager());
    }

    public void allocateWork(WorkTask task) {
        //分配工作
        String type = task.getType();
        if (!managerMap.containsKey(type))
            type = DEFAULT_WORK;
        Manager m = managerMap.get(type);
        System.out.println(m.doWork(task));
    }
}
