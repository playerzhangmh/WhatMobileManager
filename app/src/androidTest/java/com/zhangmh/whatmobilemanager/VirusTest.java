package com.zhangmh.whatmobilemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.zhangmh.Dao.VirusDao;
import com.zhangmh.Utils.Md5Utils;

/**
 * Created by coins on 2016/4/5.
 */
public class VirusTest extends AndroidTestCase {
    public void testIsvirus(){
        PackageManager packageManager = getContext().getPackageManager();
        try {
            //ApplicationInfo applicationInfo = packageManager.getApplicationInfo("cn.ti.tt", 0);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo("cn.itcase.getkey", 0);
            String sourceDir = applicationInfo.sourceDir;
            Log.v("hw3",sourceDir);
            String apkMd5 = Md5Utils.getApkMd5(sourceDir);
            boolean b = VirusDao.queryIsVirus(getContext(), apkMd5);
            assertTrue(b);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
