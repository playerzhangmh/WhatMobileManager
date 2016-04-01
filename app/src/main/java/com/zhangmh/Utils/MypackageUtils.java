package com.zhangmh.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.zhangmh.bean.Appinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by coins on 2016/3/31.
 */
public class MypackageUtils {
    public static String getAvailableSDstorage(Context context){
        String s ="";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(externalStorageDirectory.getAbsolutePath());
            long availableBlocks =0;
            long blockSize =0;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR2){
                availableBlocks = statFs.getAvailableBlocksLong();
                blockSize = statFs.getBlockSizeLong();
            }else {
                availableBlocks = statFs.getAvailableBlocks();
                blockSize = statFs.getBlockSize();
            }
            s=Formatter.formatFileSize(context, availableBlocks * blockSize);
        }
        return s;
    }


    public static String getAvailableROMstorage(Context context){
        String s ="";
        File DataStorageDirectory = Environment.getDataDirectory();
        StatFs statFs = new StatFs(DataStorageDirectory.getAbsolutePath());
        long availableBlocks =0;
        long blockSize =0;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR2){
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        }else {
            availableBlocks = statFs.getAvailableBlocks();
            blockSize = statFs.getBlockSize();
        }
        s=Formatter.formatFileSize(context, availableBlocks * blockSize);
        return s;
    }


    public static List<Appinfo> getAppInfolist(Context context){
        List<Appinfo> list=new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for(PackageInfo packageInfo:installedPackages){
            Appinfo appinfo=new Appinfo();
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable drawable = applicationInfo.loadIcon(packageManager);
            appinfo.setIcon(drawable);
            CharSequence charSequence = applicationInfo.loadLabel(packageManager);
            appinfo.setLabel(charSequence.toString());
            int flags = applicationInfo.flags;
            if((applicationInfo.FLAG_SYSTEM&flags)==(applicationInfo.FLAG_SYSTEM)){
                appinfo.setIsSystem(true);
            }else {
                appinfo.setIsSystem(false);
            }
            if((applicationInfo.FLAG_EXTERNAL_STORAGE&flags)==applicationInfo.FLAG_EXTERNAL_STORAGE){
                appinfo.setIsSDcard(true);
            }else {
                appinfo.setIsSDcard(false);
            }
            list.add(appinfo);
        }
        return list;
    }
}
