package cn.topideal.com.design.delegate;

import lombok.Data;

/**
 * 工作任务
 */
@Data
public class WorkTask {
    String type;//任务类型
    String name;//任务名称
    int day;//完成天数

    public WorkTask(String type, String name, int day) {
        this.type = type;
        this.name = name;
        this.day = day;
    }
}
