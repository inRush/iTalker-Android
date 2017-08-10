package me.inrush.factory.presenter.account;

import me.inrush.factory.presenter.BasePresenter;

/**
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.presenter.account
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
