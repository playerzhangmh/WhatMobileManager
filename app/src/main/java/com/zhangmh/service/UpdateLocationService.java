package com.zhangmh.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhangmh.whatmobilemanager.Home;

/**
 * Created by coins on 2016/3/29.
 */
public class UpdateLocationService extends Service {

    private LocationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取定位数据
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        criteria.setAltitudeRequired(true);// 定位海波高度
// 参数 1 偏好设置 参数2 定位方式是否可用
        String bestProvider = manager.getBestProvider(criteria, true);
// 请求定位  参数1 定位方式  参数2 最短的间隔时间 不能小于1分钟 可以指定0  参数3 最短间隔距离 参数4
        manager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v("hw2", "onLocationChanged");
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Home.getMyapplication().getSetting_sp().edit()
                    .putString("latitude",latitude+"")
                    .putString("longitude",longitude+"")
                    .commit();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.v("hw2","onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.v("hw2","onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.v("hw2","onProviderDisabled");

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.removeUpdates(locationListener);
    }
}
