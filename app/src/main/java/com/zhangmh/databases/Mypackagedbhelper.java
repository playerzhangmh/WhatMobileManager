package com.zhangmh.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coins on 2016/4/1.
 */
public class Mypackagedbhelper extends SQLiteOpenHelper {
    public Mypackagedbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create="create table packageInfo (_id Integer primary key autoincrement,lockedpackagename varchar(100),lockedpackagelabel varchar(100));";
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
