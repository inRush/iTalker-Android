package me.inrush.factory.model.api.account;


import com.google.gson.annotations.Expose;

import me.inrush.factory.model.db.User;

/**
 * 账户部分返回的Model
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class AccountRspModel {
    // 用户基本信息
    @Expose
    private User user;
    // 当前登录的账号
    @Expose
    private String account;
    // 当前登录成功后获取的Token,
    // 可以通过Token获取用户的所有信息
    @Expose
    private String token;
    // 标示是否已经绑定到了设备PushId
    @Expose
    private boolean isBind;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
