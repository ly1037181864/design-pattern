package cn.topideal.com.jvm.classanalyse;

public enum ConstantType {
    Constant_Utf8(1, "CONSTANT_Utf8"),
    Constant_Integer(3, "CONSTANT_Integer"),
    Constant_Float(4, "CONSTANT_Float"),
    Constant_Long(5, "CONSTANT_Long"),
    Constant_Double(6, "CONSTANT_Double"),
    Constant_Class(7, "CONSTANT_Class"),
    Constant_String(8, "CONSTANT_String"),
    Constant_Fieldref(9, "CONSTANT_Fieldref"),
    Constant_Methodref(10, "CONSTANT_Methodref"),
    Constant_InterfaceMethodref(11, "CONSTANT_InterfaceMethodref"),
    Constant_NameAndType(12, "CONSTANT_NameAndType"),
    Constant_MethodHandle(15, "CONSTANT_MethodHandle"),
    Constant_MethodType(16, "CONSTANT_MethodType"),
    Constant_InvokeDynamic(18, "CONSTANT_InvokeDynamic");

    int flag;
    String type;

    ConstantType(int flag, String type) {
        this.flag = flag;
        this.type = type;
    }

    public static ConstantType valueOf(int flag) {
        ConstantType[] types = ConstantType.values();
        for (ConstantType type : types) {
            if (flag == type.flag)
                return type;
        }
        return null;

    }
}
