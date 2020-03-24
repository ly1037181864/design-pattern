package cn.topideal.com.jvm.classanalyse;

public enum ClassFileType {
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
