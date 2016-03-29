package com.zhangmh.broadcastReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.zhangmh.application.Myapplication;
import com.zhangmh.service.UpdateLocationService;
import com.zhangmh.whatmobilemanager.Home;
import com.zhangmh.whatmobilemanager.R;

/**
 * Created by coins on 2016/3/28.
 */
public class SmsReceiver extends BroadcastReceiver {


    private SharedPreferences setting_sp;

    @Override
    public void onReceive(Context context, Intent intent) {

        Myapplication myapplication = Home.getMyapplication();
        setting_sp = myapplication.getSetting_sp();


        //只有用户开启了防盗才会开启这些功能
        boolean startGaurdat = setting_sp.getBoolean("startGaurdat", false);
        if(startGaurdat){
            String safeNum = setting_sp.getString("safeNum", "");
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();
                Log.v("hw2",sender+"ccccc"+safeNum);
                if (safeNum.equals(sender)) {
                    if (body.equals("#*alarm*#")) {
                        //播放一段音乐
                        Log.v("hw2","#*alarm*#");
                        playAmusic(context);
                    } else if (body.equals("#*location*#")) {
                        Log.v("hw2","#*location*#");

                        //定位
                        getLocation(context,safeNum);
                    } else if (body.equals("#*wipedata*#")) {
                        //远程擦除数据
                    } else if (body.equals("#*lockscreen*#")) {
                        //远程锁屏
                    }
                }
            }
        }

    }
    public void playAmusic(Context context){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void getLocation(Context context,String safeNum){
        // 由于获取定位需要一定的时间，但广播生命周期较短，故需要用一个service来完成这些操作
        context.startService(new Intent(context, UpdateLocationService.class));
        String latitude = setting_sp.getString("latitude", "");
        String longitude = setting_sp.getString("longitude", "");
        Log.v("hw2", latitude + "----" + longitude);
        if(!latitude.isEmpty()&&!longitude.isEmpty()){
            SmsManager aDefault = SmsManager.getDefault();
            aDefault.sendTextMessage(safeNum,null,latitude+","+longitude,null,null);
        }
    }


    //擦除数据
    public void wipeData(Context context){
        Log.v("hw2", "wipedata");

        //在此应该实现wipedata
        DevicePolicyManager mDPM =
                (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        mDPM.wipeData(0);
    }
    //远程锁屏
    public void lockScreen(Context context){
        Log.v("hw2","lockscrenn");

        //在此应该实现锁屏
        DevicePolicyManager mDPM =
                (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.lockNow();
        //mDPM.resetPassword("123",0);重置密码
    }

}
