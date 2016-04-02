package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Utils.Md5Utils;
import com.zhangmh.application.Myapplication;

public class PackageLockSpace extends ActionBarActivity {

    private EditText et_applockspace_psw;
    private String packagename;
    private SharedPreferences tempPackage_sp;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor tempedit;
    private SharedPreferences setting_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_lock_space);
        Myapplication application = (Myapplication) getApplication();
        tempPackage_sp = application.getTempPackage_sp();
        tempedit = tempPackage_sp.edit();
        setting_sp = application.getSetting_sp();
        editor = application.getSetting_sp().edit();

        et_applockspace_psw = (EditText) findViewById(R.id.et_applockspace_psw);
        TextView tv_applockspace_label = (TextView) findViewById(R.id.tv_applockspace_label);
        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        packagename = intent.getStringExtra("packagename");
        tv_applockspace_label.setText(label);

    }
    public void makeTemp(View v){

        //密码还是保存在最初的config中，而临时应用的保存在tempPackage_sp中
        String s = et_applockspace_psw.getText().toString();
        if(setting_sp.getString("applockpsw","").isEmpty()){

           setPasswordforGuardatDialog("applockpsw", R.layout.tempapp_setpwd);
        }else {
            if(setting_sp.getString("applockpsw","").equals(Md5Utils.encrypt(s))){
                Log.v("hw2","tempPackage_spapplockpsw");

                tempPackage_sp.edit()
                       .putBoolean(packagename,true)
                       .commit();

                finish();
            }else {
                Toast.makeText(PackageLockSpace.this,"密码错误，请重输",Toast.LENGTH_LONG).show();
            }
        }
    }


    //抄自Home
    public void setPasswordforGuardatDialog(String GuardAgainstThief,int Rlayoutid) {

        final String TAG=GuardAgainstThief;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout guardat_setpwd = (LinearLayout) View.inflate(this,Rlayoutid, null);
        final EditText et_guardat_setpwd = (EditText) guardat_setpwd.findViewById(R.id.et_guardat_setpwd);
        final EditText et_guardat_confirmpwd = (EditText) guardat_setpwd.findViewById(R.id.et_guardat_confirmpwd);
        Button bt_guardat_validate = (Button) guardat_setpwd.findViewById(R.id.bt_guardat_validate);
        Button bt_guardat_cancel = (Button) guardat_setpwd.findViewById(R.id.bt_guardat_cancel);
        builder.setView(guardat_setpwd);
        //把控件加入中后创建一个对话框并弹出来
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        bt_guardat_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String setpwd = et_guardat_setpwd.getText().toString();
                String confirmpwd = et_guardat_confirmpwd.getText().toString();
                if (!setpwd.isEmpty() && !confirmpwd.isEmpty()) {
                    if (setpwd.equals(confirmpwd)) {
                        editor.putString(TAG, Md5Utils.encrypt(setpwd));
                        editor.commit();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(PackageLockSpace.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PackageLockSpace.this, "输入不得为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_guardat_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }
}
