package com.zhangmh.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by coins on 2016/4/4.
 */
public class VirusDao {
    public static boolean queryIsVirus(Context context,String apkmd5s){
        boolean isVirus=false;
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(context.getFilesDir() + "/antivirus.db", null, 0);
        Cursor datable = sqLiteDatabase.query("datable", new String[]{"_id"}, "md5=?", new String[]{apkmd5s}, null, null, null);
        if(datable.moveToNext()){
            isVirus=true;
        }
        sqLiteDatabase.close();
        return isVirus;
    }
}
