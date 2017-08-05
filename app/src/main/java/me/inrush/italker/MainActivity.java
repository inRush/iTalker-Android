package me.inrush.italker;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;
import me.inrush.common.app.Activity;
import me.inrush.common.widget.PortraitView;
import me.inrush.italker.fragments.main.ActiveFragment;
import me.inrush.italker.fragments.main.ContactFragment;
import me.inrush.italker.fragments.main.GroupFragment;
import me.inrush.italker.helper.NavHelper;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        // 底部辅助工具类
        mNavHelper = new NavHelper<>(this, getSupportFragmentManager(), R.id.lay_container, this);
        // 添加Tab
        mNavHelper
                .add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

        // 设置底部导航栏的点击事件
        mNavigation.setOnNavigationItemSelectedListener(this);

        // 加载标题栏图片
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();
        // 从底部导航中接管Menu,手动触发第一次点击
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick(View view) {

    }

    @OnClick(R.id.btn_action)
    void onActionClick(View view) {

    }

    /**
     * 当底部导航栏被点击的时候执行
     *
     * @param item 点击的item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 转接事件流到工具中
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * NavHelper 处理后的回调
     *
     * @param newTab 新的Tab
     * @param oldTab 就得Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);

        // 对浮动按钮隐藏与显示的动画
        float transY = 0;
        float rotation = 0;

        if(newTab.extra.equals(R.string.title_home)){
            transY = Ui.dipToPx(getResources(), 76);
        }else if (newTab.extra.equals(R.string.title_group)) {
            mAction.setImageResource(R.drawable.ic_group_add);
            rotation = -360;
        }else{
            mAction.setImageResource(R.drawable.ic_contact_add);
            rotation = 360;
        }
        // 开始浮动按钮的动画
        // 旋转,Y轴位移,弹性插值器
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(500)
                .start();

    }

}
