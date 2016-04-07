package com.zhangmh.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhangmh.databases.Myblacknumdbhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coins on 2016/4/5.
 */
public class BlacknumDbDao {

    private final SQLiteDatabase blacknumdb;

    public BlacknumDbDao(Context context) {
        Myblacknumdbhelper helper=new Myblacknumdbhelper(context,"blacknum.db",null,1);
        blacknumdb = helper.getReadableDatabase();
    }
    //增删改查
    public int isBlacknum(String _blacknum){
        int mode=-1;
        Cursor cursor = blacknumdb.query("blacknum", new String[]{"mode"}, "_blacknum=?", new String[]{_blacknum}, null, null, null);
        if(cursor.moveToNext()){
            mode=cursor.getInt(0);
        }
        cursor.close();
        return mode;
    }
    public int getBlacknumlistcount(){
        Cursor cursor = blacknumdb.rawQuery("select count(*) from blacknum", null);
        cursor.moveToNext();
        int anInt = cursor.getInt(0);
        return anInt;
    }
    public List<String> getAllblacklist(){
        List<String> list=new ArrayList<>();
        Cursor cursor = blacknumdb.rawQuery("select * from blacknum", null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(1)+"@"+cursor.getInt(2));
        }
        return list;
    }

    public List<String> getPartblacklist(int offset,int limit){
        List<String> list=new ArrayList<>();
        //游标放在后面
       // Cursor cursor = blacknumdb.rawQuery("select * from blacknum limit ? offset ? ", new String[]{limit+"",offset+""});
        Cursor cursor = blacknumdb.query("blacknum",null,null,null,null,null,null,offset+","+limit);
        while (cursor.moveToNext()){
            list.add(cursor.getString(1)+"@"+cursor.getInt(2));
        }
        return list;
    }
    public int updatedb(int mode,String num){
        ContentValues values=new ContentValues();
        values.put("mode", mode);
        int blacknum = blacknumdb.update("blacknum", values, "_blacknum=?", new String[]{num});
        return blacknum;
    }
    public long insertblacknum(int mode,String num){
        long blacknum=-1;
        if(num.matches("1[34578]\\d{9}")||num.matches("^1\\d{2}$|\\d{5}$|^55\\d{2}$|^\\d{7}|\\d{8}$")){
            ContentValues values=new ContentValues();
            values.put("mode", mode);
            values.put("_blacknum", num);
            blacknum = blacknumdb.insert("blacknum", null, values);
        }
        return blacknum;
    }

    public int deleteblack(String num){
        int blacknum = blacknumdb.delete("blacknum", "_blacknum=?", new String[]{num});
        return blacknum;
    }

}
