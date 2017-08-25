package me.inrush.italker;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import me.inrush.common.app.Activity;
import me.inrush.factory.persistence.Account;
import me.inrush.italker.activities.MainActivity;
import me.inrush.italker.fragments.assist.PermissionsFragment;

public class LaunchActivity extends Activity {

    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        View root = findViewById(R.id.activity_launch);
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        // 动画进入到50%等待pushId获取到
        startAnim(0.5f, new Runnable(){
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    /**
     * 等待个推框架对我们的PushId设置好值
     */
    private void waitPushReceiverId(){
        if(Account.isLogin()){
            // 已经登录情况下,判断是否已经绑定
            // 如果没有绑定则等待广播接收器进行绑定
            if(Account.isBind()){
                skip();
                return;
            }
        }else{
            // 没有登录是不能绑定PushId的
            if(!TextUtils.isEmpty(Account.getPushId())){
                skip();
                return;
            }
        }


        // 进行递归等待
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        },500);
    }

    /**
     * 在舔砖之前需要把剩下的50%进行完成
     */
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    /**
     * 真正进行跳转
     */
    private void reallySkip(){
        // 进行权限检测
        if (PermissionsFragment.havAllPermissions(this, getSupportFragmentManager())) {
//            if (Account.isLogin()) {
//
//            } else {
//                AccountActivity.show(this);
//            }
            MainActivity.show(this);
            finish();
        }
    }


    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画结束进度
     * @param callback    动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable callback) {
        // 获取一个借宿的颜色
        int finalColor = Resource.Color.WHITE;
        // 运算当前进度的颜色

        ArgbEvaluator evaluator = new ArgbEvaluator();

        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);

        // 构建一个属性动画
        @SuppressWarnings("unchecked")
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.run();
            }
        });
        valueAnimator.start();
    }


    private Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };
}


