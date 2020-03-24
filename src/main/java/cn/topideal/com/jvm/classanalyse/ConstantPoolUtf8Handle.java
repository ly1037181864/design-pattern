package cn.topideal.com.jvm.classanalyse;

public class ConstantPoolUtf8Handle extends ConstantPoolHandle {

    @Override
    protected int doHandle(String substr) {
        return 0;
    }

    @Override
    protected boolean preHandle(int flag) {
        if (ConstantType.valueOf(flag) == null)
            return false;
        return true;
    }
}
