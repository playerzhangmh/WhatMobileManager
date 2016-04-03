package com.zhangmh.application;

import android.app.ActivityManager;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.zhangmh.Utils.MyProcessUtils;
import com.zhangmh.service.MytelenumLoctionService;
import com.zhangmh.service.MywatchRunningProcess;
import com.zhangmh.service.Mywatchappstart;
import com.zhangmh.whatmobilemanager.R;
import com.zhangmh.widget.MyprocessWidgetProvider;

import java.util.List;

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
        startService(new Intent(this, MywatchRunningProcess.class));

        IntentFilter filter=new IntentFilter();
        filter.addAction("com.cskaoyan.widgetupdate");
        filter.addAction("com.zhangmh.watchProcess");
        registerReceiver(receiver,filter);
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

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(!action.equals("com.zhangmh.watchProcess")){
                ActivityManager am= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
                for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcesses){
                    String processName = runningAppProcessInfo.processName;
                    if(getPackageName().equals(processName)){
                        continue;
                    }
                    am.killBackgroundProcesses(processName);
                }
            }

            AppWidgetManager instance = AppWidgetManager.getInstance(context);
            ComponentName name=new ComponentName(context, MyprocessWidgetProvider.class);
            RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.wiggetprocess);
            remoteViews.setTextViewText(R.id.tv_processwidget_count, "当前运行的进程数" + MyProcessUtils.getRunningProcess(context));
            remoteViews.setTextViewText(R.id.tv_processwidget_memory, "可用内存:" + MyProcessUtils.getAvailableRam(context));
            instance.updateAppWidget(name, remoteViews);
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(receiver);
    }
}
