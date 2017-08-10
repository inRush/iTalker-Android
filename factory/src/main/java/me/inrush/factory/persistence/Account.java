package me.inrush.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import me.inrush.factory.Factory;
import me.inrush.factory.model.api.account.AccountRspModel;
import me.inrush.factory.model.db.User;
import me.inrush.factory.model.db.User_Table;

import static me.inrush.factory.Factory.app;

/**
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.persistence
 */

public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    private static String pushId;
    private static boolean isBind;
    private static String token;
    private static String userId;
    private static String account;

    /**
     * 存储数据的XML文件中,进行持久化
     */
    private static void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_ACCOUNT, account)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_TOKEN, token)
                .apply();
    }


    /**
     * 进行数据加载
     *
     * @param context 上下文
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
    }

    /**
     * 设置并存储设备的Id
     *
     * @param pushId 设备Id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(app());
    }

    /**
     * 获取推送Id
     *
     * @return pushId
     */
    public static String getPushId() {
        return pushId;
    }


    /**
     * 返回当前账户是否已经登录
     *
     * @return 返回登录状态
     */
    public static boolean isLogin() {
        // 判断用户Id,Token是否为空
        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);
    }

    /**
     * 是否完善了用户的信息
     *
     * @return true就是完成了
     */
    public static boolean isComplete() {
        return isLogin();

    }


    /**
     * 返回是否已经绑定PushId
     *
     * @return true就是绑定了
     */
    public static boolean isBind() {
        return isBind;
    }


    /**
     * 设置绑定状态
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }


    /**
     * 保存自己的信息到持久化XML文件中
     *
     * @param model AccountRspModel
     */
    public static void login(AccountRspModel model) {
        // 存储用户的Token,用户Id
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();

        save(Factory.app());
    }

    /**
     * 获取当前登录的用户信息
     * @return User
      */
    public static User getUser() {
        return TextUtils.isEmpty(userId) ? new User() :
                SQLite.select()
                        .from(User.class)
                        .where(User_Table.id.eq(userId))
                        .querySingle();
    }


}
