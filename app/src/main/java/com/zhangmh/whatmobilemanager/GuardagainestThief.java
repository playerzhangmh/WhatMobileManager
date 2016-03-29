package com.zhangmh.whatmobilemanager;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.application.Myapplication;

public class GuardagainestThief extends ActionBarActivity {

    private SharedPreferences setting_sp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1 判断是第一次进来，则直接进入设置步骤
        //2 若不是第一次进来，则直接进入当前布局
        //我们可以在sp中设置一个值来判定是否第一次进来，假设true是第一次进来，false是后面进来
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        edit = setting_sp.edit();
        boolean firstComing = setting_sp.getBoolean("firstComing", true);
        if(firstComing){
            startActivity(new Intent(this,GuardatSetup1.class));
            edit.putBoolean("firstComing", false);
            edit.commit();
            finish();
        }else {
            setContentView(R.layout.activity_guardat_setup);
            //从sp中取出安全号码，并读取防盗是否开启，
            TextView tv_Guardat_safeNum= (TextView) findViewById(R.id.tv_Guardat_safeNum);
            TextView tv_Guardat_startGuard= (TextView) findViewById(R.id.tv_Guardat_startGuard);
            ImageView iv_guardat_gps= (ImageView) findViewById(R.id.iv_guardat_gps);
            ImageView iv_guardat_alarm= (ImageView) findViewById(R.id.iv_guardat_alarm);
            ImageView iv_guardat_wipedata= (ImageView) findViewById(R.id.iv_guardat_wipedata);
            ImageView iv_guardat_lockscreen= (ImageView) findViewById(R.id.iv_guardat_lockscreen);
            //是否完成开启功能所用图片
            Drawable lock=getResources().getDrawable(R.drawable.lock);
            lock.setBounds(0, 0, lock.getMinimumWidth()/2, lock.getMinimumHeight()/2);
            Drawable unlock=getResources().getDrawable(R.drawable.unlock);
            unlock.setBounds(0, 0, unlock.getMinimumWidth()/2, unlock.getMinimumHeight()/2);
            Drawable btn_check_buttonless_on=getResources().getDrawable(R.drawable.btn_check_buttonless_on);
            btn_check_buttonless_on.setBounds(0, 0, btn_check_buttonless_on.getMinimumWidth() / 2, btn_check_buttonless_on.getMinimumHeight() / 2);
            Drawable btn_check_buttonless_off=getResources().getDrawable(R.drawable.btn_check_buttonless_off);
            btn_check_buttonless_off.setBounds(0, 0, btn_check_buttonless_off.getMinimumWidth() / 2, btn_check_buttonless_off.getMinimumHeight() / 2);


            String safeNum = setting_sp.getString("safeNum", "");
            boolean startGaurdat = setting_sp.getBoolean("startGaurdat", false);

            if(safeNum.isEmpty()){
                tv_Guardat_safeNum.setText("请设置安全号码");
            }else {
                tv_Guardat_safeNum.setText(safeNum);
            }
            if(startGaurdat){

                //用代码更换tv中的图片
                /*Drawable nav_up=getResources().getDrawable(R.drawable.button_nav_up);
                  nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                  textview1.setCompoundDrawables(null, null, nav_up, null); */


                tv_Guardat_startGuard.setCompoundDrawables(null, null, lock, null);


                iv_guardat_gps.setImageDrawable(btn_check_buttonless_on);
                iv_guardat_alarm.setImageDrawable(btn_check_buttonless_on);

                //添加一个管理员权限是否开启了的flag，后面两个功能只有开启才为真
                boolean startAministrators = setting_sp.getBoolean("startAministrators", false);
                if(startAministrators){
                    iv_guardat_wipedata.setImageDrawable(btn_check_buttonless_on);
                    iv_guardat_lockscreen.setImageDrawable(btn_check_buttonless_on);
                }else {
                    iv_guardat_wipedata.setImageDrawable(btn_check_buttonless_off);
                    iv_guardat_lockscreen.setImageDrawable(btn_check_buttonless_off);
                }

            }else {
                iv_guardat_gps.setImageDrawable(btn_check_buttonless_off);
                iv_guardat_alarm.setImageDrawable(btn_check_buttonless_off);
                tv_Guardat_startGuard.setCompoundDrawables(null, null, unlock, null);
                iv_guardat_wipedata.setImageDrawable(btn_check_buttonless_off);
                iv_guardat_lockscreen.setImageDrawable(btn_check_buttonless_off);
                Toast.makeText(this,"请开启防盗，以获得下面功能",Toast.LENGTH_LONG).show();
            }

            //重新设置
           TextView tv_Guardat_setupAgain= (TextView) findViewById(R.id.tv_Guardat_setupAgain);
            tv_Guardat_setupAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GuardagainestThief.this,GuardatSetup1.class));
                    finish();
                }
            });
        }
    }
}
