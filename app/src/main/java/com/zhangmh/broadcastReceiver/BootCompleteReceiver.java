package com.zhangmh.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.zhangmh.application.Myapplication;
import com.zhangmh.whatmobilemanager.Home;

/**
 * Created by coins on 2016/3/28.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Myapplication myapplication = Home.getMyapplication();
        SharedPreferences setting_sp = myapplication.getSetting_sp();
        //只有用户开启了防盗才会开启这些功能
        boolean startGaurdat = setting_sp.getBoolean("startGaurdat", false);
        if(startGaurdat){
            String ismi = setting_sp.getString("ismi", "");
            String safeNum = setting_sp.getString("safeNum", "");


            TelephonyManager systemService = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String simSerialNumber = systemService.getSimSerialNumber();
            if(!ismi.isEmpty()){
                if(!ismi.equals(simSerialNumber)){
                    SmsManager aDefault = SmsManager.getDefault();
                    aDefault.sendTextMessage(safeNum,null,"您好友XXX的手机SIM卡更换了",null,null);
                }
            }
        }
    }
}
