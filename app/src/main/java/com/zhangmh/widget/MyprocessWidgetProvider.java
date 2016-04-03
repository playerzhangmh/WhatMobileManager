package com.zhangmh.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.zhangmh.Utils.MyProcessUtils;
import com.zhangmh.whatmobilemanager.R;

/**
 * Created by coins on 2016/4/3.
 */
public class MyprocessWidgetProvider extends AppWidgetProvider {

    private RemoteViews remoteViews;
    private int processcount;
    private String availableram;

    public MyprocessWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.v("hw2", "MyprocessWidgetProvider--onReceive");

       /* String action = intent.getAction();
        if(action.equals("com.zhangmh.watchProcess")){
            processcount = intent.getIntExtra("processcount", -1);
            availableram = intent.getStringExtra("availableram");
            Log.v("hw2",processcount+availableram);
        }
        if(remoteViews!=null){
            remoteViews.setTextViewText(R.id.tv_processwidget_count, "当前运行的进程数" + processcount);
            remoteViews.setTextViewText(R.id.tv_processwidget_memory, "可用内存:" + availableram);
        }else {
            remoteViews = new RemoteViews("com.zhangmh.whatmobilemanager", R.layout.wiggetprocess);
        }*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.v("hw2", "MyprocessWidgetProvider--onUpdate");
        processcount= MyProcessUtils.getRunningProcess(context);
        availableram=MyProcessUtils.getAvailableRam(context);
        //告诉系统要去更新那个widget
        ComponentName name = new ComponentName(context,MyprocessWidgetProvider.class);
        remoteViews = new RemoteViews("com.zhangmh.whatmobilemanager", R.layout.wiggetprocess);
        remoteViews.setTextViewText(R.id.tv_processwidget_count, "当前运行的进程数" +processcount);
        remoteViews.setTextViewText(R.id.tv_processwidget_memory, "可用内存:" + availableram);
        Intent intent = new Intent("com.cskaoyan.widgetupdate");
        PendingIntent pdintent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_widget_clear, pdintent);;
        appWidgetManager.updateAppWidget(name, remoteViews);


    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.v("hw2", "MyprocessWidgetProvider--onDeleted");

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.v("hw2", "MyprocessWidgetProvider--onEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.v("hw2", "MyprocessWidgetProvider--onDisabled");

    }
}
