package com.zhangmh.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhangmh.Dao.PackageDbDao;
import com.zhangmh.application.Myapplication;
import com.zhangmh.whatmobilemanager.PackageLockSpace;

import java.util.List;

/**
 * Created by coins on 2016/4/1.
 */
public class Mywatchappstart extends Service {

    private ActivityManager systemService;
    private Myapplication application;
    int i=0;
    boolean flag;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("hw2", "onStartCommandonCreate");

        systemService = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        application = (Myapplication) getApplication();

        //由于监听屏幕关闭事件，一旦关闭，就将临时应用的sp清空
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(mScreenReceiver, filter);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.v("hw2", "onStartCommand");
        flag=true;


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (flag){
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = systemService.getRunningAppProcesses();
                    //for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcesses){
                       // Log.v("hw2",runningAppProcesses.size()+"runningAppProcesses");

                   // }
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(0);
                    String processName = runningAppProcessInfo.processName;
                    boolean liveInsp = application.querytempApp(processName);
                    String liveinDb = PackageDbDao.queryApp(Mywatchappstart.this, processName);
                    Log.v("hw2",liveInsp+processName);
                    if(!liveinDb.isEmpty()&&!liveInsp&&!processName.equals(getPackageName())){
                        Intent intent1=new Intent();
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.setClass(Mywatchappstart.this, PackageLockSpace.class);
                        intent1.putExtra("packagename",processName);
                        intent1.putExtra("label",liveinDb);
                        startActivity(intent1);
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("hw2", "onDestroy");

        flag=false;
    }


    BroadcastReceiver mScreenReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            application.getTempPackage_sp().edit()
                    .clear()
                    .commit();
        }
    };
}
