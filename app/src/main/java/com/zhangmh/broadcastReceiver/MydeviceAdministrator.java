package com.zhangmh.broadcastReceiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhangmh.whatmobilemanager.Home;

/**
 * Created by coins on 2016/3/29.
 */
public class MydeviceAdministrator extends DeviceAdminReceiver {

    void showToast(Context context, String msg) {
        String status = msg;
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_enabled");
        Home.getMyapplication().getSetting_sp().edit()
                .putBoolean("startAministrators",true)
                .commit();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_disabled");
        Home.getMyapplication().getSetting_sp().edit()
                .putBoolean("startAministrators",false)
                .commit();
    }
}
