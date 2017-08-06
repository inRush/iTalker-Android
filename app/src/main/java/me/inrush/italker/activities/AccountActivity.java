package me.inrush.italker.activities;

import android.content.Context;
import android.content.Intent;

import me.inrush.common.app.Activity;
import me.inrush.italker.R;
import me.inrush.italker.fragments.account.UpdateInfoFragment;

/**
 * @author inrush
 * @date 2017/8/6.
 * @package me.inrush.italker.activities
 */

public class AccountActivity extends Activity {
    /**
     *
     * 账户Activity显示的入口
     * @param context 上下文
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,new UpdateInfoFragment())
                .commit();
    }
}
