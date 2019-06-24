package cn.topideal.com.multithread.basicthread;

import lombok.Data;

@Data
public class Request {
    String name;

    public Request(String name) {
        this.name = name;
    }
}
