package com.zhangmh.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by coins on 2016/4/1.
 */
public class PackageDbDao {
    public static boolean insertApp(Context context,String packagename,String lable){
        boolean flag=false;
        ContentValues values=new ContentValues();
        values.put("lockedpackagename", packagename);
        values.put("lockedpackagelabel", lable);
        Uri insert = context.getContentResolver().insert(Uri.parse("content://com.zhangmh.lockedappmanager"), values);
        if(insert!=null){
            flag=true;
        }
        return flag;
    }

    public static boolean deleteApp(Context context,String packagename){
        boolean flag=false;
        int delete = context.getContentResolver().delete(Uri.parse("content://com.zhangmh.lockedappmanager"), "lockedpackagename=?", new String[]{packagename});
        if(delete ==1){
            flag=true;
        }
        return flag;
    }

    public static String queryApp(Context context,String packagename){
        String flag="";
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.zhangmh.lockedappmanager"), new String[]{"lockedpackagelabel"}, "lockedpackagename=?", new String[]{packagename}, null);
        if(query.moveToNext()){
            flag=query.getString(0);
        }
        return flag;
    }


}
