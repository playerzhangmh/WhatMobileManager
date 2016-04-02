package com.zhangmh.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zhangmh.service.MytelenumLoctionService;
import com.zhangmh.service.Mywatchappstart;

/**
 * Created by coins on 2016/3/25.
 */
public class Myapplication extends Application {



    private SharedPreferences setting_sp;
    private SharedPreferences tempPackage_sp;


    @Override
    public void onCreate() {
        super.onCreate();
        setting_sp=getSharedPreferences("config",MODE_PRIVATE);
        tempPackage_sp=getSharedPreferences("tempPackage",MODE_PRIVATE);//用来存放软件管理中临时可用软件
        boolean autoshowLocation = setting_sp.getBoolean("autoshowLocation", true);
        boolean startlockpackage = setting_sp.getBoolean("startlockpackage", true);

        if(autoshowLocation){
            startService(new Intent(this, MytelenumLoctionService.class));
        }
        if(startlockpackage){
            startService(new Intent(this, Mywatchappstart.class));
        }
    }


    public SharedPreferences getSetting_sp() {
        return setting_sp;
    }

    public SharedPreferences getTempPackage_sp() {
        return tempPackage_sp;
    }

    public boolean querytempApp(String packagename){
        boolean aBoolean = tempPackage_sp.getBoolean(packagename, false);//false表明不是临时可用app，实际上只是为了证明存在而已，因为一旦其临时用完后就会从sp中删除
        if(aBoolean){
            return true;
        }
        return false;
    }
    public void addtempApp(String packagename){
        tempPackage_sp.edit()
                .putBoolean(packagename,true)
                .commit();
    }
}
