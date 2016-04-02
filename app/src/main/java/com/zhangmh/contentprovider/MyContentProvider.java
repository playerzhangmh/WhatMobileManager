package com.zhangmh.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zhangmh.databases.Mypackagedbhelper;

/**
 * Created by coins on 2016/4/1.
 */
public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        Mypackagedbhelper mypackagedbhelper=new Mypackagedbhelper(getContext(),"whatmobilemanager",null,1);
        db = mypackagedbhelper.getReadableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor packageInfo = db.query("packageInfo", projection, selection, selectionArgs, null, null, null);
        return packageInfo;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert("packageInfo",null,values);
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.zhangmh.lockedappmanager"),null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db.delete("packageInfo",selection,selectionArgs);
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.zhangmh.lockedappmanager"), null);
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db.update("packageInfo",values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.zhangmh.lockedappmanager"), null);
        return 1;
    }
}
