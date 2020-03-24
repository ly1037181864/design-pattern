package cn.topideal.com.jvm.classanalyse;

import java.util.List;

public class ClassDefintion {
    private String magic;
    private String minor_version;
    private String major_version;
    private int consPoolsize;
    private List<ConsantInfo> consantInfoList;

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public String getMinor_version() {
        return minor_version;
    }

    public void setMinor_version(String minor_version) {
        this.minor_version = minor_version;
    }

    public String getMajor_version() {
        return major_version;
    }

    public void setMajor_version(String major_version) {
        this.major_version = major_version;
    }

    public int getConsPoolsize() {
        return consPoolsize;
    }

    public void setConsPoolsize(int consPoolsize) {
        this.consPoolsize = consPoolsize;
    }

    public List<ConsantInfo> getConsantInfoList() {
        return consantInfoList;
    }

    public void setConsantInfoList(List<ConsantInfo> consantInfoList) {
        this.consantInfoList = consantInfoList;
    }
}
