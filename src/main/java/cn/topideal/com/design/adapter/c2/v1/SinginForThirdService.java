package cn.topideal.com.design.adapter.c2.v1;

import cn.topideal.com.design.adapter.c2.ResultMsg;

/**
 * 支持第三方登陆
 * 适配器模式指的是新的功能要对原有功能的兼容
 * 这个例子是通过继承的方式即保留了原有功能，同时又扩展了新的功能
 */
public class SinginForThirdService extends SiginService {

    /**
     * 兼容原有注册逻辑
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultMsg regist(String username, String password) {
        return super.regist(username, password);
    }

    /**
     * 兼容原有登陆逻辑
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultMsg login(String username, String password) {
        return super.login(username, password);
    }

    /**
     * 新增微信登陆
     *
     * @param openId
     * @return
     */
    public ResultMsg loginForWechat(String openId) {
        return new ResultMsg(200, "登陆成功", null);
    }

    /**
     * 新增token登陆
     *
     * @param token
     * @return
     */
    public ResultMsg loginForToken(String token) {
        return new ResultMsg(200, "登陆失败", null);
    }

    /**
     * 新增手机号登陆
     *
     * @param telphone
     * @param code
     * @return
     */
    public ResultMsg loginForTelphone(String telphone, String code) {
        return new ResultMsg(200, "登陆成功", null);
    }

    /**
     * 支持注册后马上登录
     *
     * @param username
     * @param password
     * @return
     */
    public ResultMsg loginForRegist(String username, String password) {
        super.regist(username, null);
        return super.login(username, null);
    }
}
