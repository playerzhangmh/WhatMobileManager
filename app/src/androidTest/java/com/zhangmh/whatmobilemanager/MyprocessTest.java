package com.zhangmh.whatmobilemanager;

import android.app.ActivityManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.zhangmh.Utils.MyProcessUtils;
import com.zhangmh.bean.ProcessInfo;

import java.util.List;

/**
 * Created by coins on 2016/4/6.
 */
public class MyprocessTest extends AndroidTestCase {
    public void testProcesssize(){
        int runningProcess = MyProcessUtils.getRunningProcess(getContext());
        Log.v("testProcesssize",runningProcess+"");
    }

    public void testProcessname(){
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(getContext().ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        String process = runningServices.get(0).process;
        Log.v("testProcesssize",process+"");
    }

    public void testGetprocessList(){
        List<ProcessInfo> realRunningProcessList = MyProcessUtils.getRunningProcessList(getContext());
        Log.v("testGetprocessList",realRunningProcessList.toString());
    }
}
