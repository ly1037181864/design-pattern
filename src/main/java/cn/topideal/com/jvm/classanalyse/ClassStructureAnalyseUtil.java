package cn.topideal.com.jvm;

import cn.topideal.com.jvm.classanalyse.ClassDefintion;

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
        analyseConstantPool(str.substring(len));//解析常量池的内容

        if (!"CAFEBABE".equals(str.substring(0, len += ClassFileType.magic.len)))
            throw new RuntimeException("文件类型错误");
        String lowVersion = str.substring(len, len += ClassFileType.minor_version.len);
        String highVersion = str.substring(len, len += ClassFileType.major_version.len);
        System.out.println(Integer.valueOf(lowVersion, ENCODING_16));
        int javaVersion = Integer.valueOf(highVersion, ENCODING_16);
        if (52 == javaVersion)
            System.out.println("java version 1.8." + Integer.valueOf(lowVersion, ENCODING_16));

        int consPoolsize = Integer.valueOf(str.substring(len, len += ClassFileType.constant_pool_count.len), ENCODING_16);
        System.out.println("常量池大小" + consPoolsize);

        int flag = Integer.valueOf(str.substring(len, len + DEFAULT_FLAG), ENCODING_16);
        System.out.println(flag);


    }

    private static void analyseConstantPool(String substring) {
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

enum ClassFileType {
    magic(8),
    minor_version(4),
    major_version(4),
    constant_pool_count(4),
    access_flags(4),
    this_class(4),
    super_class(4),
    interface_count(4),
    fields_count(4),
    method_count(4),
    attributes_count(4);

    int len;

    ClassFileType(int len) {
        this.len = len;
    }
}

enum ConstantType {
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


}
