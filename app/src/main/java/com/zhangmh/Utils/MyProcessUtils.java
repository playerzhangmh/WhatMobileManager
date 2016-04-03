package com.zhangmh.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;

import com.zhangmh.bean.ProcessInfo;
import com.zhangmh.whatmobilemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coins on 2016/4/2.
 */
public class MyProcessUtils {
    public static int getRunningProcess(Context context){

        ActivityManager systemService = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = systemService.getRunningAppProcesses();
        return runningAppProcesses.size();
    }
    public static String getTotalRam(Context context){
        ActivityManager systemService = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        systemService.getMemoryInfo(memoryInfo);
        long totalMem = memoryInfo.totalMem;
        String s = Formatter.formatFileSize(context, totalMem);
        return s;

    }
    public static String getAvailableRam(Context context){

        ActivityManager systemService = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        systemService.getMemoryInfo(memoryInfo);
        long availMem = memoryInfo.availMem;
        String s = Formatter.formatFileSize(context, availMem);
        return s;
    }

    public static List<ProcessInfo> getRunningProcessList(Context context){
        List<ProcessInfo> processInfoList=new ArrayList<>();
        ActivityManager systemService = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = systemService.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcesses){
            ProcessInfo processInfo=new ProcessInfo();
            String processName = runningAppProcessInfo.processName;
            if(processName!=null&&!processName.isEmpty()){
                processInfo.setPackagename(processName);//1
                try {
                    Drawable applicationIcon = packageManager.getApplicationIcon(processName);//####
                    if(applicationIcon!=null){
                        processInfo.setDrawable(applicationIcon);
                    }else {
                        processInfo.setDrawable(context.getDrawable(R.mipmap.ic_launcher));
                    }//2
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
                    CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
                    if(applicationLabel.toString().isEmpty()){
                        processInfo.setLabel("未知应用");
                    }else {
                        processInfo.setLabel(applicationLabel.toString());
                    }//3

                    if((applicationInfo.flags&applicationInfo.FLAG_SYSTEM)==applicationInfo.FLAG_SYSTEM){
                        processInfo.setIsSystem(true);
                    }else {
                        processInfo.setIsSystem(false);
                    }//4

                    int[] pid=new int[]{runningAppProcessInfo.pid};
                    Debug.MemoryInfo[] processMemoryInfo = systemService.getProcessMemoryInfo(pid);
                    int totalPss = processMemoryInfo[0].getTotalPss();
                    String s = Formatter.formatFileSize(context, (long) totalPss);
                    processInfo.setRam(s);//5

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();//尽管我们已经考虑到这点，但实际corehe media两个进程的应用名可读到，但是不可用，程序应该不会再进来，在###这句代码还是会报异常
                    processInfo.setLabel("系统进程");
                    processInfo.setPackagename("");
                    processInfo.setRam("未知");
                    processInfo.setIsSystem(true);
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
                    processInfo.setDrawable(drawable);
                }
            }else{
                processInfo.setLabel("系统进程");
                processInfo.setPackagename("");
                processInfo.setRam("未知");
                processInfo.setIsSystem(true);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
                processInfo.setDrawable(drawable);
            }

            processInfoList.add(processInfo);
        }

        return processInfoList;

    }

}
