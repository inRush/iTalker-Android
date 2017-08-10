package me.inrush.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;
import me.inrush.common.app.Activity;
import me.inrush.common.app.Fragment;
import me.inrush.italker.R;
import me.inrush.italker.fragments.account.AccountTrigger;
import me.inrush.italker.fragments.account.LoginFragment;
import me.inrush.italker.fragments.account.RegisterFragment;

/**
 * @author inrush
 * @date 2017/8/6.
 * @package me.inrush.italker.activities
 */

public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurrentFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;

    /**
     * 账户Activity显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化登录界面
        mCurrentFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurrentFragment)
                .commit();

        // 背景初始化
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();
                        // 使用适配包给drawable进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        // 给drawable进行着色,蒙版模式
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                            PorterDuff.Mode.SCREEN);
                        // 设置背景图片
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    @Override
    public void triggerView() {
        Fragment fragment = null;
        if (mCurrentFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                // 默认mRegisterFragment为null
                // 初始化RegisterFragment
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            // 不需要进行判空,因为默认的情况下已经进行了赋值
            fragment = mLoginFragment;
        }

        // 重新赋值现在显示的Fragment
        mCurrentFragment = fragment;
        // 切换显示的Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, mCurrentFragment)
                .commit();
    }
}
