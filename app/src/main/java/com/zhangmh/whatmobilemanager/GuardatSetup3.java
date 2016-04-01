package com.zhangmh.whatmobilemanager;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangmh.application.Myapplication;
import com.zhangmh.bean.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class GuardatSetup3 extends MybaseSetup {
    private SharedPreferences setting_sp;
    private SharedPreferences.Editor edit;
    private EditText et_guardatSP3_safeNum;
    private Button bt_guardatSP3_selectContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardat_setup3);
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        edit = setting_sp.edit();
        et_guardatSP3_safeNum = (EditText) findViewById(R.id.et_guardatSP3_safeNum);
        bt_guardatSP3_selectContact = (Button) findViewById(R.id.bt_guardatSP3_selectContact);

        bt_guardatSP3_selectContact.setOnClickListener(onClickListener);

    }

    public void pre(View v){
        startActivity(new Intent(this, GuardatSetup2.class));
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }
    public void next(View v){

        String safeNum = et_guardatSP3_safeNum.getText().toString();
        String[] split = safeNum.split("");
        int flag=1;
        for(int i=0;i<split.length;i++){
            if(split[i].matches("\\d")){
                flag++;
            }
        }
        Log.v("hw2",flag+"bbbbbbbbbbbbbbbb");
        if(flag==split.length&&(split.length>=3)){
            edit.putString("safeNum",safeNum);
            edit.commit();
            startActivity(new Intent(this, GuardatSetup4.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();
        } else {
            Toast.makeText(this, "请输入合法的安全号码", Toast.LENGTH_LONG).show();
        }
    }


    //安全号码的获得除了通过手动输入还可通过去通讯录或数据库选择。有两种方法
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         /*   //一种是直接用intent去拿
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickContactIntent,100);*/



            //一种是直接访问其数据库

            Intent intent=new Intent(GuardatSetup3.this,ContactsList.class);
            startActivityForResult(intent, 200);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if(requestCode==200&&data!=null){
            Uri data1 = data.getData();
            Cursor cursor = getContentResolver().query(data1, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToNext();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column);
            et_guardatSP3_safeNum.setText(number);
        }*/
        Log.v("hw2","我也在这里");

        if(requestCode==200&&data!=null){
            if(resultCode==1000){
                Log.v("hw2","我在这里");
                String num = data.getStringExtra("num");
                et_guardatSP3_safeNum.setText(num);

            }
        }

    }
}
