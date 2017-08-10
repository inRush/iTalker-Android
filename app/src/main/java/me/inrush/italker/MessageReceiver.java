package me.inrush.italker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import me.inrush.factory.Factory;
import me.inrush.factory.data.helper.AccountHelper;
import me.inrush.factory.persistence.Account;

/**
 * 个推消息接收器
 *
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.italker
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        // 判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                Log.i(TAG, "onReceive: " + "GET_CLIENTID: " + bundle.toString());
                // 当Id初始化的时候
                // 获取设备Id
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA:

                // 常规的消息送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.i(TAG, "onReceive: " + "GET_MSG_DATA: " + message);
                    onMessageArrived(message);
                }
                break;
            default:
                Log.i(TAG, "onReceive: " + "OTHER: " + bundle.toString());

                break;
        }
    }

    /**
     * 初始化Id
     *
     * @param cId 设备ID
     */
    private void onClientInit(String cId) {
        Account.setPushId(cId);
        if (Account.isLogin()) {
            // 账户登录状态,进行一次pushId的绑定
            // 没有登录是不能绑定PushId的
            AccountHelper.bindPushId(null);
        }
    }

    /**
     * 消息送达
     *
     * @param message 常规消息
     */
    private void onMessageArrived(String message) {
        // 交给Factory进行处理
        Factory.dispatchPush(message);
    }
}
