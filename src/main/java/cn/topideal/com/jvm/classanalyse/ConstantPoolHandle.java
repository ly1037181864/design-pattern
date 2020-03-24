package cn.topideal.com.jvm.classanalyse;

public abstract class ConstantPoolHandle implements IConstantPoolHandle {

    @Override
    public int handlePool(int flag, String substr) {
        if (preHandle(flag))
            return -1;

        return doHandle(substr);
    }

    protected abstract int doHandle(String substr);

    protected abstract boolean preHandle(int flag);

    protected String chageAscll(String str) {
        str = str.replaceAll(" ", "");
        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        while (len < str.length()) {

            String asci = str.substring(len, len += 2);
            int acii = Integer.valueOf(asci, 16);
            stringBuilder.append((char) acii);

        }
        return stringBuilder.toString();
    }
}
