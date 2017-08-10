package me.inrush.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 *
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.data
 */

public class DataSource {

    /**
     * 包括了成功与失败的接口
     * @param <T> 泛型
     */
    public interface CallBack<T> extends SuccessCallBack<T>,FailedCallBack{

    }

    // 网络请求成功回调
    public interface SuccessCallBack<T> {
        void onDataLoaded(T t);
    }

    // 网络请求失败回调
    public interface FailedCallBack {
        void onDataNotAvailable(@StringRes int strRes);
    }
}
