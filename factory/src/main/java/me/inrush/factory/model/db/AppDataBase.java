package me.inrush.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库基本信息
 *
 * @author inrush
 * @date 2017/8/10.
 * @package me.inrush.factory.model.db
 */
@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME = "AppDataBase";
    public static final int VERSION = 1;
}
