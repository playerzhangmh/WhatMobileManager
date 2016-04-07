package com.zhangmh.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.zhangmh.Utils.MyProcessUtils;
import com.zhangmh.bean.ProcessInfo;

import java.util.List;

/**
 * Created by coins on 2016/4/3.
 */
public class MywatchRunningProcess extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    boolean flag;
    private List<ProcessInfo> runningProcessList;
    boolean nosendBroadflag;

    public MywatchRunningProcess() {
        super("mywatchrunningprocess");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runningProcessList = MyProcessUtils.getRunningProcessList(this);
        flag=true;
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.zhangmh.myProcessManager");
        registerReceiver(receiver,filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (flag){
            List<ProcessInfo> currentrunningProcessList = MyProcessUtils.getRunningProcessList(this);
            if(!runningProcessList.toString().equals(currentrunningProcessList.toString())&&!nosendBroadflag){
                Intent intent1=new Intent("com.zhangmh.watchProcess");
                sendBroadcast(intent1);
                runningProcessList=currentrunningProcessList;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag=false;
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("hw2","MywatchRunningProcessonReceive");
            nosendBroadflag=intent.getBooleanExtra("nosendBroadflag",true);
        }
    };
}
