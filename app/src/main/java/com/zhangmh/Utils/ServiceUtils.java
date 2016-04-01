package com.zhangmh.Utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by coins on 2016/3/31.
 */
public class ServiceUtils {
    public static boolean isServiceAlive(Context context,String packagename){
        ActivityManager systemService = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = systemService.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo serviceInfo:runningServices){
            ComponentName service = serviceInfo.service;
            String className = service.getClassName();
            if(className.equals(packagename)){
                return true;
            }
        }
        return false;
    }
}
