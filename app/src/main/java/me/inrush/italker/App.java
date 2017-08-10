package me.inrush.italker;

import com.igexin.sdk.PushManager;

import me.inrush.common.app.Application;
import me.inrush.factory.Factory;

/**
 * @author inrush
 * @date 2017/8/7.
 * @package me.inrush.italker
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Factory
        Factory.setup();

        // 个推进行初始化
        PushManager.getInstance().initialize(this);
    }
}
