package com.zhangmh.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coins on 2016/4/5.
 */
public class Myblacknumdbhelper extends SQLiteOpenHelper{

    public Myblacknumdbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create="create table blacknum (_id Integer primary key autoincrement,_blacknum varchar(14),mode tinyint);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
