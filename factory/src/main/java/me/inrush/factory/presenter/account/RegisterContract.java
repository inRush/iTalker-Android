package me.inrush.factory.presenter.account;

import me.inrush.factory.presenter.BaseContract;

/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter.account
 */

public class RegisterContract {
    public interface View extends BaseContract.View<Presenter> {
        // 注册成功
        void registerSuccess();
    }

    public interface Presenter extends BaseContract.Presenter {
        // 发起注册
        void register(String phone, String name, String password);

        // 检查手机号是否正确
        boolean checkMobile(String phone);
    }

}
