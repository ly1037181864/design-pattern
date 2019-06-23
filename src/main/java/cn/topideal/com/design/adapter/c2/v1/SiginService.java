package cn.topideal.com.design.adapter.c2.v1;

import cn.topideal.com.design.adapter.c2.Member;
import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * 登陆逻辑
 */
public class SiginService {

    /**
     * 注册方法
     *
     * @param username
     * @param password
     * @return
     */
    public ResultMsg regist(String username, String password) {
        return new ResultMsg(200, "注册成功", new Member(username, password));
    }


    /**
     * 登录的方法
     *
     * @param username
     * @param password
     * @return
     */
    public ResultMsg login(String username, String password) {
        return new ResultMsg(200, "登陆成功", null);
    }

}
