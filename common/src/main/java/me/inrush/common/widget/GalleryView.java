package me.inrush.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.inrush.common.R;
import me.inrush.common.widget.recycler.RecyclerAdapter;


public class GalleryView extends RecyclerView {
    private final static int LOADER_ID = 0x8888;
    private final static int MIN_IMAGE_SIZE = 10 * 1024;// 最小的图片大小限制


    private Adapter mAdapter = new Adapter();
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectChangedListener mListener;
    private int maxImageCount = 3;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // Cell点击操作
                if (onItemSelectClick(image)) {
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 点击Cell的具体逻辑
     *
     * @param image 选择的图片
     * @return 返回是否需要刷新界面
     */
    private boolean onItemSelectClick(Image image) {
        // 是否需要进行刷新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() >= maxImageCount) {
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, maxImageCount);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }

        if (notifyRefresh) {
            notifySelectChanged();
        }
        return notifyRefresh;
    }


    /**
     * 得到全部选中的图片的地址
     *
     * @return 返回图片地址
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 对选中数组进行清空
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化
     *
     * @param loaderManager loaderManager
     * @return 返回一个loaderId可用于销毁loader
     */
    public int setup(LoaderManager loaderManager, SelectChangedListener listener) {
        mListener = listener;
        // 初始化一个loader
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * 设置最大的选中数量,默认 3 张
     *
     * @param count 最大选中数量
     */
    public void setMaxImageCount(int count) {
        if (count <= 0) {
            return;
        }
        maxImageCount = count;
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        if (mListener != null) {
            mListener.onSelectCountChanged(mSelectedImages.size());
        }
    }

    public interface SelectChangedListener {
        void onSelectCountChanged(int count);
    }

    public void setSelectChangedListener(SelectChangedListener listener) {
        mListener = listener;
    }

    /**
     * 通知Adapter更新数据
     *
     * @param images 新的数据
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }


    /**
     * 用于实际数据加载的loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID, // 图片Id
                MediaStore.Images.Media.DATA, // 图片路劲
                MediaStore.Images.Media.DATE_ADDED // 图片的创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 创建一个loader
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC"); // 倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 当loader完成时
            List<Image> images = new ArrayList<>();
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    // 移动游标到开始
                    data.moveToFirst();
                    int idIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int pathIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int dateIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = data.getInt(idIndex);
                        String path = data.getString(pathIndex);
                        long date = data.getLong(dateIndex);

                        // 判断文件存不存在
                        File file = new File(path);
                        // 图片没有或者图片太小就跳过
                        if (!file.exists() || file.length() < MIN_IMAGE_SIZE) {
                            continue;
                        }

                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = date;
                        images.add(image);
                    } while (data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // 当销毁或者重置了,进行界面清空操作
            updateSource(null);
        }
    }

    /**
     * 内部的数据结构
     */
    private static class Image {
        int id; // 数据的Id
        String path; // 图片的路径
        long date; // 图片的创建日期
        boolean isSelect; // 是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image data) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelect;


        public ViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mSelect = (CheckBox) itemView.findViewById(R.id.cb_select);
            mShade = itemView.findViewById(R.id.view_shade);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不适用缓存,直接从路径中加载原图
                    .centerCrop() // 居中剪切
                    .placeholder(R.color.grey_200) // 默认颜色
                    .into(mPic);
            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelect.setChecked(image.isSelect);
            mSelect.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
        }
    }
}
