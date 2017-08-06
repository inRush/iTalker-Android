package me.inrush.italker.fragments.account;


import butterknife.BindView;
import butterknife.OnClick;
import me.inrush.common.app.Fragment;
import me.inrush.common.widget.PortraitView;
import me.inrush.italker.R;
import me.inrush.italker.fragments.media.GalleryFragment;

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

            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

}
