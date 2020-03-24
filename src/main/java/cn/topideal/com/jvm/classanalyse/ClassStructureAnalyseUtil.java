package cn.topideal.com.jvm.classanalyse;

import java.io.*;

public class ClassStructureAnalyseUtil {
    public static final int ENCODING_16 = 16;
    public static final int DEFAULT_FLAG = 2;

    public static void test() {
        int len = 0;//指针下标
        String str = loadClass().replaceAll(" ", "");
        ClassDefintion classDefintion = new ClassDefintion();
        classDefintion.setMagic(str.substring(0, len += ClassFileType.magic.len));//设置魔数
        classDefintion.setMinor_version(str.substring(len, len += ClassFileType.minor_version.len));//设置低版本号
        classDefintion.setMagic(str.substring(len, len += ClassFileType.major_version.len));//设置高版本号
        //解析常量池
        int consPoolsize = Integer.valueOf(str.substring(len, len += ClassFileType.constant_pool_count.len), ENCODING_16);
        classDefintion.setConsPoolsize(consPoolsize);//设置常量池的大小
        analyseConstantPool(consPoolsize, str.substring(len));//解析常量池的内容
        //解析字段
        //解析方法定义
        //描述解析


    }

    private static void analyseConstantPool(int consPoolsize, String substr) {
        if (consPoolsize > 0) {
            int flag = Integer.valueOf(substr.substring(0, DEFAULT_FLAG), ENCODING_16);//常量池类型
            IConstantPoolHandle handle = ConstantPoolHandleFactory.getHandle(flag);
            int nextBegin = handle.handlePool(flag, substr);
            analyseConstantPool(consPoolsize--, substr.substring(DEFAULT_FLAG + nextBegin));
        }
    }


    public static void main(String[] args) {
        String str = "636E2F74 6F706964 65616C2F 636F6D2F 6A766D2F 436C6173 73537472 75637475 7265416E 616C7973 65";
        str = str.replaceAll(" ", "");
        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        while (len < str.length()) {

            String asci = str.substring(len, len += 2);
            int acii = Integer.valueOf(asci, 16);
            //char chari = (char) acii;
            stringBuilder.append((char) acii);
            //System.out.print(chari);

        }
        System.out.print(stringBuilder.toString());
    }

    private static String loadClass() {
        String path = "/Users/liuyou/topideal/design-pattern/src/main/java/cn/topideal/com/jvm/classAnalyse.txt";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {

            File file = new File(path);
            bufferedReader = new BufferedReader(new FileReader(file));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read + "\n");
            }
            System.out.println(stringBuilder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }
}




