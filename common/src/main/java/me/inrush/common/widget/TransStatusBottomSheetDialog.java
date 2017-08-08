package me.inrush.common.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;
import android.view.Window;

import me.inrush.common.tools.UiTool;

/**
 * 解决弹出时顶部状态栏变黑问题
 * @author inrush
 * @date 2017/8/7.
 * @package me.inrush.common.widget
 */

public class TransStatusBottomSheetDialog extends BottomSheetDialog {
    public TransStatusBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        if (window == null) {
            return;
        }

        // 拿到屏幕的高度
        int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
        int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());

        int dialogHeight = screenHeight - statusHeight;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }
}
