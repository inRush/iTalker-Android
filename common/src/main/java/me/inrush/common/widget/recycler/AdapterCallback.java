package me.inrush.common.widget.recycler;

/**
 * @author inrush
 * @date 2017/7/21.
 * @package me.inrush.common.widget.recycler
 */

public interface AdapterCallback<T> {
    void update(T data, RecyclerAdapter.ViewHolder<T> holder);
}
