package com.zhangmh.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by coins on 2016/3/29.
 */
public class QueryTeleNumLocationDao {
    public static String getTeleNumLocation(Context context,String telenum){
        String location="";
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(context.getFilesDir() + "/naddress.db", null, 0);
        //筛选所输入号码的合法性
        if(telenum.matches("1[34578]\\d{9}")){
            //查询数据库
            telenum=telenum.substring(0, 7);
            Cursor numinfo = sqLiteDatabase.query("numinfo", new String[]{"outkey"}, "mobileprefix=?", new String[]{telenum}, null, null, null);
            if(numinfo.moveToNext()){
                int outkey = numinfo.getColumnIndex("outkey");
                String outkeys = numinfo.getString(outkey);
                Cursor address_tb = sqLiteDatabase.query("address_tb", new String[]{"city", "cardtype"}, "_id=?", new String[]{outkeys}, null, null, null);
                address_tb.moveToNext();
                int city = address_tb.getColumnIndex("city");
                String citys = address_tb.getString(city);
                int cardtype = address_tb.getColumnIndex("cardtype");
                String cardtypes = address_tb.getString(cardtype);
                if(citys.lastIndexOf("市")==(citys.length()-1)){
                    citys=citys.substring(0,citys.length()-1);
                }
                if(citys.indexOf("省")>-1){
                    String province=citys.substring(0,citys.indexOf("省"));
                    citys=citys.replace('省', ' ');
                    cardtypes=cardtypes.substring(cardtypes.indexOf(province) + province.length(), cardtypes.length() - 1);

                }else {
                    String _city = citys.substring(0, citys.indexOf("市"));
                    citys=_city;
                    cardtypes=cardtypes.substring(cardtypes.indexOf(_city)+_city.length(),cardtypes.length()-1);
                }



                location=citys+" "+cardtypes;
                //此处将数据做处理后拿出
            }else {
                location="数据库数据不足";
            }

        }else if(telenum.matches("\\d{3,}")){
            //处理那些非手机号
            switch (telenum.length()){
                case 3:
                    //一般为紧急电话
                    location="特殊电话";
                    break;
                case 4:
                    //虚拟机电话
                    location="虚拟电话";
                    break;
                case 5:
                    //三大运营商电话
                    if("10086".equals(telenum)){
                        location="中国移动客服";
                    }else if("10010".equals(telenum)){
                        location="中国联通客服";
                    }else if("10086".equals(telenum)){
                        location="中国电信客服";
                    }
                    break;
                case 7:
                case 8:
                    //本地座机
                    location="本地座机";
                    break;
                default:
                    //长途座机号码
                    if(telenum.length()>=10&&telenum.startsWith("0")){
                        //区号为3位数的
                        //区号为四位数的
                        Cursor address_tb = sqLiteDatabase.query("address_tb", new String[]{"city"}, "area=?", new String[]{telenum.substring(0, 3)}, null, null, null);
                       if(address_tb.moveToNext()){
                           String citys=address_tb.getString(0);
                           location=citys;
                       }else {
                           Cursor address_tb2 = sqLiteDatabase.query("address_tb", new String[]{"city"}, "area=?", new String[]{telenum.substring(0, 4)}, null, null, null);
                           if(address_tb.moveToNext()){
                               String citys=address_tb.getString(0);
                               location=citys;
                           }
                       }

                    }
            }
        }

        return location;

    }
}
