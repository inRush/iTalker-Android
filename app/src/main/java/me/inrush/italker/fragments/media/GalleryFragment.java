package me.inrush.italker.fragments.media;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.inrush.common.widget.GalleryView;
import me.inrush.common.widget.TransStatusBottomSheetDialog;
import me.inrush.italker.R;

/**
 * Gallery图片选择器
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectChangedListener {

    private GalleryView mGalleryView;
    private OnSelectedListener mListener;

    public GalleryFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 返回一个复写的
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        this.mGalleryView = (GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleryView.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectCountChanged(int count) {
        // 选中了一张图片就隐藏
        if (count > 0) {
            dismiss();
            if (mListener != null) {
                String[] paths = mGalleryView.getSelectedPath();
                mListener.OnSelectedImage(paths[0]);
                // 取消和唤起者的引用,加快内存回收
                mListener = null;
            }
        }
    }


    /**
     * 设置选择事件监听
     *
     * @param listener 监听器
     * @return 返回自身
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void OnSelectedImage(String path);
    }
}
