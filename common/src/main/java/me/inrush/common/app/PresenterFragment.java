package me.inrush.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import me.inrush.factory.presenter.BaseContract;

/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.common.app
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends Fragment
        implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    /**
     * 初始化一个Presenter
     * @return 新建一个Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化Presenter
        initPresenter();
    }

    @Override
    public void showError(@StringRes int str) {
        Application.showToast(str);
    }

    @Override
    public void showLoading() {
        // TODO 显示一个loading
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
