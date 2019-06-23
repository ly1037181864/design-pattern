package cn.topideal.com.design.adapter.c2;

import lombok.Data;
import lombok.ToString;

/**
 * 返回结果
 */
@Data
@ToString
public class ResultMsg {

    private int code;
    private String msg;
    private Object data;

    public ResultMsg(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
