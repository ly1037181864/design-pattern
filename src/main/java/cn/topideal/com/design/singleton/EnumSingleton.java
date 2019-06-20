package cn.topideal.com.design.singleton;

/**
 * 枚举实现单例 不存在线程安全问题也不存在序列化问题
 * 从JDK层面就避免了序列化问题和反射问题
 */
public enum EnumSingleton {

    SINGLETON;

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public static EnumSingleton getInstance() {
        return SINGLETON;
    }
}
