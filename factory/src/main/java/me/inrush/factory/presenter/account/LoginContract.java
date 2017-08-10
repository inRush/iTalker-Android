package me.inrush.factory.presenter.account;

import me.inrush.factory.presenter.BaseContract;

/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.persenter.account
 */

public class LoginContract {
    public interface View extends BaseContract.View<Presenter> {
        // 登录成功
        void loginSuccess();

    }

    public interface Presenter extends BaseContract.Presenter {
        // 发起注册
        void login(String phone, String password);

    }

}
