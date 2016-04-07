package com.zhangmh.whatmobilemanager;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.widget.Toast;

public class TrafficStatistic extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_statistic);
        TrafficStats trafficStats=new TrafficStats();
        long mobileTxBytes = trafficStats.getMobileTxBytes();
        long mobileRxBytes = trafficStats.getMobileRxBytes();
        //获取从上次开机开始到现在的总的流量
        Toast.makeText(this, Formatter.formatFileSize(this,mobileRxBytes+mobileTxBytes),Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,Home.class));

    }
}
