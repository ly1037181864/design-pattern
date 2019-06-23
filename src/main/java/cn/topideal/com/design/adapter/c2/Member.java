package cn.topideal.com.design.adapter.c2;

import lombok.Data;

/**
 * 用户信息
 */
@Data
public class Member {
    private String username;
    private String password;
    private String mid;
    private String info;

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
