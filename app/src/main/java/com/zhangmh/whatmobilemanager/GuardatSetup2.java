package com.zhangmh.whatmobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhangmh.application.Myapplication;
import com.zhangmh.view.SettingItems;

public class GuardatSetup2 extends MybaseSetup {
    private SharedPreferences setting_sp;
    private SharedPreferences.Editor edit;
    private String simSerialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardat_setup2);
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        edit = setting_sp.edit();
        SettingItems si_guardatSP2_bind = (SettingItems) findViewById(R.id.si_guardatSP2_bind);
        TelephonyManager systemService = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        simSerialNumber = systemService.getSimSerialNumber();
        si_guardatSP2_bind.getMyOnclickListener(new SettingItems.MyOnclickListener() {
            @Override
            public void bindSIMOnclick() {
                //获取SIM卡的ismi

                Log.v("hw2", simSerialNumber +"aaaaaaaaaaaaaaaaaaaaaaa");
                edit.putString("ismi", simSerialNumber);
                edit.commit();
            }

            @Override
            public void unbindSIMOnclick() {
                edit.putString("ismi","");
                edit.commit();

            }
        });
    }
    public void pre(View v){
        startActivity(new Intent(this, GuardatSetup1.class));
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }
    public void next(View v){

        String ismi = setting_sp.getString("ismi", "");
        if(!ismi.isEmpty()){
            startActivity(new Intent(this, GuardatSetup3.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();
        }else {
            boolean checked = setting_sp.getBoolean("bindSIM", true);
            if(checked){
                edit.putString("ismi", simSerialNumber);
                edit.commit();
                startActivity(new Intent(this, GuardatSetup3.class));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
            }else {
                Toast.makeText(this,"请绑定SIM卡，否则防盗功能无法实现",Toast.LENGTH_LONG).show();
            }
        }

    }
}
