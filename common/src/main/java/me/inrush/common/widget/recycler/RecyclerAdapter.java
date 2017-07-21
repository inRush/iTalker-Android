package me.inrush.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.inrush.common.R;

/**
 * @author inrush
 * @date 2017/7/21.
 * @package me.inrush.common.widget.recycler
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback {

    private final List<T> mDataList;
    private AdapterListener<T> mListener = null;

    /**
     * 构成函数
     * @param dataList 数据
     * @param listener 监听器
     */
    public RecyclerAdapter(List<T> dataList, AdapterListener<T> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<T>(), listener);
    }

    public RecyclerAdapter(List<T> dataList) {
        this.mDataList = dataList;
    }

    public RecyclerAdapter() {
        this(new ArrayList<T>());
    }

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,约定为XML布局的Id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于初始化XML布局为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把XML ID 为viewType的文件初始化为一个root view
        View root = inflater.inflate(viewType, parent, false);
        //  通过子类必须实现的方法,得到一个ViewHolder
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);

        // 设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 设置View的Tag为ViewHolder,进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        // 进行界面布局绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        holder.callback = this;
        return holder;
    }

    /**
     * 复写默认布局类型返回
     *
     * @param position 位置
     * @return 类型, 其实复写返回的都是XML布局文件
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 位置
     * @param data     当前的数据
     * @return XML文件的ID, 用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, T data);

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局Id
     * @return ViewHolder
     */
    protected abstract ViewHolder<T> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定一个数据到holder上
     *
     * @param holder   ViewHolder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        T data = mDataList.get(position);
        // 触发Holder的绑定方法
        holder.bind(data);
    }

    /**
     * 得到当前集合的数据量
     *
     * @return 数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据,并通知更新
     *
     * @param data 插入的数据
     */
    public void add(T data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 批量插入数据,并通知更新
     *
     * @param dataList 插入的数据
     */
    @SafeVarargs
    public final void add(T... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 批量插入数据,并通知更新
     *
     * @param dataList 插入的数据
     */
    public void add(Collection<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合,其中包括了清空
     *
     * @param dataList 新的数据集合
     */
    public void replace(Collection<T> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到ViewHolder在当前对应的Adapter中的位置
            int pos = viewHolder.getAdapterPosition();
            // 触发监听器点击事件
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听事件
     *
     * @param listener 监听器
     */
    public void setListener(AdapterListener<T> listener) {
        this.mListener = listener;
    }

    /**
     * 自定义监听器
     *
     * @param <T> 泛型
     */
    public interface AdapterListener<T> {
        // 当Cell点击时触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, T data);

        // 当Cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, T data);
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <T> 泛型数据
     */
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        protected T mData;
        private AdapterCallback<T> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(T data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候触发,必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(T data);

        /**
         * Holder自己对自己对应的Data进行更新操作
         *
         * @param data Data数据
         */
        public void updateData(T data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}