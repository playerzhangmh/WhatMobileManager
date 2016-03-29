package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.zhangmh.application.Myapplication;
import com.zhangmh.broadcastReceiver.MydeviceAdministrator;

public class GuardatSetup4 extends MybaseSetup {

    private CheckBox cb_guardatSP4_startProtect;
    private SharedPreferences setting_sp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardat_setup4);
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        edit = setting_sp.edit();
        cb_guardatSP4_startProtect = (CheckBox) findViewById(R.id.cb_guardatSP4_startProtect);
        cb_guardatSP4_startProtect.setChecked(false);
        cb_guardatSP4_startProtect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_guardatSP4_startProtect.isChecked()){
                    //弹一个对话框,告诉用户去激活管理员权限，代码待补充
                    cb_guardatSP4_startProtect.setText("成功开启防盗保护");
                    boolean startAministrators = setting_sp.getBoolean("startAministrators", false);
                    Log.v("hw2","startAministrators"+startAministrators);
                    if(!startAministrators){
                        new AlertDialog.Builder(GuardatSetup4.this)
                                .setTitle("激活超级管理员权限")
                                .setMessage("你将获得更为凶残的防盗功能，嘿嘿")
                                .setPositiveButton("I want it ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //去管理员权限页面
                                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

                                        ComponentName mDeviceAdminSample=new ComponentName(GuardatSetup4.this, MydeviceAdministrator.class);

                                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);

                                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                                "激活后才可以锁屏,如果不激活罚2000块钱");
                                        startActivity(intent);
                                        dialog.dismiss();


                                    }
                                })
                                .setNegativeButton("no way", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }

                }else {
                    cb_guardatSP4_startProtect.setText("你没有开启防盗保护");

                }
            }
        });

    }
    public void pre(View v){
        startActivity(new Intent(this,GuardatSetup3.class));
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }
    public void next(View v){
        boolean checked = cb_guardatSP4_startProtect.isChecked();
        if(checked){
            startActivity(new Intent(this, GuardagainestThief.class));

            cb_guardatSP4_startProtect.setText("成功开启防盗保护");
            edit.putBoolean("startGaurdat", true);
            edit.commit();
            finish();
        }else {
            Toast.makeText(this,"请开启防盗保护",Toast.LENGTH_LONG).show();
           new AlertDialog.Builder(this)
                   .setTitle("开启防盗")
                   .setMessage("可选择下次再开启，但需重新设置")
                   .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           startActivity(new Intent(GuardatSetup4.this, GuardagainestThief.class));

                           cb_guardatSP4_startProtect.setText("成功开启防盗保护");
                           edit.putBoolean("startGaurdat", true);
                           edit.commit();
                           finish();
                       }
                   })
                   .setNegativeButton("离开", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           //返回Home界面，将sp中所有跟防盗有关的数据重置
                           edit.putBoolean("startGaurdat",false);
                           edit.putString("safeNum", "");
                           edit.putString("ismi", "");
                           edit.putBoolean("bindSIM",false);
                           edit.commit();
                           finish();
                       }
                   })
                   .show();

        }
    }
}
