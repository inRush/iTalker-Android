package me.inrush.italker.fragments.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.genius.ui.widget.Button;

import java.util.List;

import me.inrush.common.app.Application;
import me.inrush.common.widget.TransStatusBottomSheetDialog;
import me.inrush.italker.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {

    // 权限回调标识
    private static final int RC = 0x8888;

    private Button mSubmit;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        mSubmit = (Button) root.findViewById(R.id.btn_submit);

        mSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击按钮时申请权限
                        requestPerm();
                        mSubmit.setText(getString(R.string.label_permission_process));
                    }
                }
        );
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 界面显示的时候进行刷新
        refreshState(getView());
    }

    /**
     * 刷新布局中的图片的状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (root == null) {
            return;
        }
        Context context = getContext();

        boolean haveNet = haveNetworkPerm(context);
        boolean haveRead = haveReadPerm(context);
        boolean haveWrite = haveWritePerm(context);
        boolean haveRecord = haveRecordAudioPerm(context);
        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNet ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveRead ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWrite ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_record_audio)
                .setVisibility(haveWrite ? View.VISIBLE : View.GONE);

        if (haveNet && haveRead && haveWrite && haveRecord) {
            mSubmit.setText(getString(R.string.label_permission_complete));
            mSubmit.setClickable(false);
        }else{
            mSubmit.setText(getString(R.string.label_permission_submit));
        }
    }

    /**
     * 获取是否有网络权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveNetworkPerm(Context context) {
        // 准备需要检查的网络权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储读取权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveReadPerm(Context context) {
        // 准备需要检查的读取权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储写入权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveWritePerm(Context context) {
        // 准备需要检查的写入权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有录音权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveRecordAudioPerm(Context context) {
        // 准备需要检查的录音权限
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 私有的show方法
     *
     * @param manager FragmentManager
     */
    private static void show(FragmentManager manager) {
        // 显示BottomSheetDialog
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }


    /**
     * 检查是否具有所有的权限
     *
     * @param context 上下文
     * @param manager FragmentManager
     * @return 是否有所有的权限
     */
    public static boolean havAllPermissions(Context context, FragmentManager manager) {
        boolean haveAll = haveNetworkPerm(context) &&
                haveReadPerm(context) &&
                haveWritePerm(context) &&
                haveRecordAudioPerm(context);
        if (!haveAll) {
            show(manager);
        }
        return haveAll;
    }

    /**
     * 申请权限
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Application.showToast(R.string.label_permission_ok);
            // Fragment 中调用getView获取根布局,前提是正在onCreateView之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    RC, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果有没有申请成功的权限存在,则弹出弹出框,用户点击去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build().show();
        }

    }


    /**
     * 权限申请回调的方法
     * 在这个方法中把对应的申请状态交给EasyPermissions框架
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数,并且告知接受权限处理者是this
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
