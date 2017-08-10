package me.inrush.factory.presenter;

import android.support.annotation.StringRes;

/**
 * MVP模式中公共的基本的契约
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter
 */

public class BaseContract {
    public interface View<T extends Presenter> {
        // 显示一个字符串错误
        void showError(@StringRes int str);

        // 显示一个进度条
        void showLoading();

        // 设置一个Presenter
        void setPresenter( T presenter);
    }

    public interface Presenter {

        // 启动方法
        void start();

        // 销毁方法
        void destroy();
    }
}
