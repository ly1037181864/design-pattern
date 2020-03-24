package cn.topideal.com.jvm.classanalyse;

import java.util.HashMap;
import java.util.Map;

public class ConstantPoolHandleFactory {
    private static Map<ConstantType, ConstantPoolHandle> map = new HashMap<>();

    static {
        map.put(ConstantType.Constant_Utf8, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Class, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Double, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Fieldref, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Float, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Integer, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_InterfaceMethodref, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_InvokeDynamic, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Long, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_MethodHandle, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_Methodref, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_NameAndType, new ConstantPoolUtf8Handle());
        map.put(ConstantType.Constant_String, new ConstantPoolUtf8Handle());
    }

    public static IConstantPoolHandle getHandle(int flag) {
        ConstantType type = ConstantType.valueOf(flag);
        return map.get(type);
    }
}
