package com.zhangmh.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhangmh.Dao.QueryTeleNumLocationDao;
import com.zhangmh.Utils.WindowMeasureUtils;
import com.zhangmh.application.Myapplication;
import com.zhangmh.whatmobilemanager.Home;

/**
 * Created by coins on 2016/3/30.
 */
public class MytelenumLoctionService extends Service {
    WindowManager mWm;
    private TelephonyManager systemService;
    private TextView view;
    private Myapplication application;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = (Myapplication) getApplication();
        Log.v("hw22","onCreatedddd");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        systemService.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);


        return super.onStartCommand(intent, flags, startId);
    }
    PhoneStateListener phoneStateListener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.v("hw2","CALL_STATE_OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.v("hw2","CALL_STATE_RINGING");
                    showLocaton(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.v("hw2","CALL_STATE_IDLE");
                    hideLocationview();
                    break;

            }
        }
    };
    public void showLocaton(String incomingNumber){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 定义显示toast宽和高
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // 不可以获取焦点 不能触摸 保持屏幕常亮
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;


        String teleNumLocation = QueryTeleNumLocationDao.getTeleNumLocation(this, incomingNumber);
        Log.v("hw2",teleNumLocation);
        if(!teleNumLocation.isEmpty()&&!teleNumLocation.equals("数据库数据不足")){
            view = new TextView(this);
            view.setText(teleNumLocation);
            view.setTextColor(Color.BLUE);
            view.setTextSize(25);

           // view.setGravity(Gravity.CENTER);

            SharedPreferences setting_sp = application.getSetting_sp();
            int[] windowMeasure = WindowMeasureUtils.getWindowMeasure(this);
           /* params.x=windowMeasure[0]/2;//中心位置和父控件的距离
            params.y=windowMeasure[1]/2;*/

            int locationViewSpaceX = setting_sp.getInt("locationViewSpaceX", windowMeasure[0] / 2);
            int locationViewSpaceY = setting_sp.getInt("locationViewSpaceY", windowMeasure[1] / 2);
           /* params.x=locationViewSpaceX;
            params.y=locationViewSpaceY;*/
            params.gravity=Gravity.LEFT|Gravity.TOP;

            params.x=locationViewSpaceX;
            params.y=locationViewSpaceY;
            Log.v("hw2", params.x + "----asdf"+params.y);
           // view.setLayoutParams(params);

            mWm.addView(view,params);
        }
    }

    //电话挂断的时候隐藏
    public void hideLocationview(){
        if(mWm!=null&&view!=null){
            mWm.removeView(view);
        }
    }
}
