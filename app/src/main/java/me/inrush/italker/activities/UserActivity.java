package me.inrush.italker.activities;

import android.content.Context;
import android.content.Intent;

import me.inrush.common.app.Activity;
import me.inrush.common.app.Fragment;
import me.inrush.italker.R;
import me.inrush.italker.fragments.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mCurrentFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurrentFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurrentFragment)
                .commit();
    }

    /**
     * 在这里收到图片剪切成功的回调
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurrentFragment.onActivityResult(requestCode, resultCode, data);
    }

}
