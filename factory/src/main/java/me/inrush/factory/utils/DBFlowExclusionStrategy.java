package me.inrush.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * DBFlow数据库字段过滤
 * @author inrush
 * @date 2017/8/10.
 * @package me.inrush.factory.utils
 */

public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // 被跳过的字段
        // 只要是DBFlow的字段class
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        // 被跳过的class
        return false;
    }
}
