package me.inrush.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import me.inrush.common.Common;
import me.inrush.factory.R;
import me.inrush.factory.data.DataSource;
import me.inrush.factory.data.helper.AccountHelper;
import me.inrush.factory.model.api.account.RegisterModel;
import me.inrush.factory.model.db.User;
import me.inrush.factory.presenter.BasePresenter;

/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter.account
 */

public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.CallBack<User> {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        // 启动Loading
        start();

        RegisterContract.View view = getView();

        // 对参数进行校验
        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            // 进行构造请求
            RegisterModel model = new RegisterModel(phone, name, password);
            // 进行网络请求
            AccountHelper.register(model, this);
        }


    }

    @Override
    public boolean checkMobile(String phone) {
        // 手机号不能为空,并且符合一定的规则
        return !TextUtils.isEmpty(phone) &&
                Pattern.matches(Common.Constance.REGES_PHONE, phone);
    }

    /**
     * 失败回调
     *
     * @param strRes 失败信息
     */
    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final RegisterContract.View view = getView();
        if(view == null) return;

        // 线程切换,切换到主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 调用主界面的注册成功
                view.showError(strRes);
            }
        });
    }

    /**
     * 成功回调
     *
     * @param user 注册成功用户
     */
    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if(view == null) return;

        // 线程切换,切换到主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 调用主界面的注册成功
                view.registerSuccess();
            }
        });
    }
}
