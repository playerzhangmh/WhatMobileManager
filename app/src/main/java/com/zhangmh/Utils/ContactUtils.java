package com.zhangmh.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zhangmh.bean.ContactItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coins on 2016/3/28.
 */
public class ContactUtils {

    public static List<ContactItem> getContactItemList(Context context){
        List<ContactItem> contactItems=new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),new String[]{"contact_id"}, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int contact_id = cursor.getInt(0);
                Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contact_id + ""}, null);

                ContactItem contactItem=new ContactItem();
                while (cursor2.moveToNext()){
                    String data1=cursor2.getString(0);
                    String mimetype=cursor2.getString(1);
                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        contactItem.setTeleNum(data1);

                    }
                    else if ("vnd.android.cursor.item/name".equals(mimetype)){

                        contactItem.setName(data1);
                    }
                }
                contactItems.add(contactItem);

            }
        }
        return contactItems;
    }
}
