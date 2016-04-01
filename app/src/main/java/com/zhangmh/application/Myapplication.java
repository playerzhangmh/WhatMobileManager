package com.zhangmh.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zhangmh.service.MytelenumLoctionService;

/**
 * Created by coins on 2016/3/25.
 */
public class Myapplication extends Application {



    private SharedPreferences setting_sp;

    @Override
    public void onCreate() {
        super.onCreate();
        setting_sp=getSharedPreferences("config",MODE_PRIVATE);
        boolean autoshowLocation = setting_sp.getBoolean("autoshowLocation", true);
        if(autoshowLocation){
            startService(new Intent(this, MytelenumLoctionService.class));
        }
    }


    public SharedPreferences getSetting_sp() {
        return setting_sp;
    }
}
