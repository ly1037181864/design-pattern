package cn.topideal.com.jvm;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * 沙箱安全机制
 * 添加启动参数
 * -Djava.security.manager
 * -Djava.security.policy=/Users/liuyou/topideal/design-pattern/src/main/java/cn/topideal/com/jvm/custom.policy
 * <p>
 * permission java.io.FilePermission "/Users/liuyou/*", "read";
 */
public class PolicyTest {

    static {
        setHasAllPerm0();
        System.setSecurityManager(null);
    }

    private static String path;

    public static void main(String[] args) {
        open();
    }

    private static void open() {
        path = "/Users/liuyou/b.txt";
        File f = new File(path);
        InputStream is;
        try {
            is = new FileInputStream(f);
            byte[] content = new byte[10];
            while (is.read(content) != -1) {
                System.out.println(new String(content));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setHasAllPerm0() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        //遍历栈帧
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            try {
                Class clz = Class.forName(stackTraceElement.getClassName());
                //利用反射调用getProtectionDomain0方法
                Method getProtectionDomain = clz.getClass().getDeclaredMethod("getProtectionDomain0", null);
                getProtectionDomain.setAccessible(true);
                ProtectionDomain pd = (ProtectionDomain) getProtectionDomain.invoke(clz);

                if (pd != null) {
                    Field field = pd.getClass().getDeclaredField("hasAllPerm");
                    field.setAccessible(true);
                    field.set(pd, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //exec("calc");
    }

    public static void setSecurityByReflection() {
        try {
            Class clz = Class.forName("java.lang.System");
            Field field = clz.getDeclaredField("security");
            field.setAccessible(true);
            field.set(System.class, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


