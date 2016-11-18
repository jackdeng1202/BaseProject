package com.lgm.baseframe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/5/11.
 */
public  class FrameDataBaseHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名称
     */

    public FrameDataBaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }


}
