package me.inrush.italker.fragments.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import me.inrush.common.app.PresenterFragment;
import me.inrush.factory.presenter.account.LoginContract;
import me.inrush.factory.presenter.account.LoginPresenter;
import me.inrush.italker.R;
import me.inrush.italker.activities.MainActivity;

/**
 * 登录界面
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
implements LoginContract.View{
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 拿到Activity的AccountTrigger接口,实现切换Fragment
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick() {
        mAccountTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        // 调用P层进行注册
        mPresenter.login(phone, password);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        // 当需要显示错误的时候触发,一定就是结束了
        mLoading.stop();
        // 让控件可以进行输入
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        // 让提交按钮可以继续使用
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        // 开始loading
        mLoading.start();
        // 让控件可以进行输入
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        // 让提交按钮可以继续使用
        mSubmit.setEnabled(false);
    }
}
