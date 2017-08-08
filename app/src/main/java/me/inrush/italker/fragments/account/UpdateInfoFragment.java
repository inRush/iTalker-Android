package me.inrush.italker.fragments.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import me.inrush.common.app.Application;
import me.inrush.common.app.Fragment;
import me.inrush.common.widget.PortraitView;
import me.inrush.italker.R;
import me.inrush.italker.fragments.media.GalleryFragment;

import static android.app.Activity.RESULT_OK;

/**
 * 更新用户Fragment
 */
public class UpdateInfoFragment extends Fragment {


    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void OnSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                // 设置图片处理的格式JPEG
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置压缩后的图片精度
                options.setCompressionQuality(96);
                File dPath = Application.getPortraitTmpFile();

                // 启动剪切Activity
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1, 1) // 1:1的比例,保证是正方形的
                        .withMaxResultSize(520, 520) // 设置大小为520 x 520
                        .withOptions(options) // 设置Option
                        .start(getActivity()); // 启动
            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 收到从Activity传过来的回调
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri到当前的头像中
     * @param uri 图片的Uri
     */
    private void loadPortrait(Uri uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }
}
