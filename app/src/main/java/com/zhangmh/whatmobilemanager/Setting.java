package com.zhangmh.whatmobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zhangmh.Utils.ServiceUtils;
import com.zhangmh.application.Myapplication;
import com.zhangmh.view.SettingItems;

import java.util.ArrayList;
import java.util.List;

public class Setting extends ActionBarActivity {

    private static Myapplication application;
    private static Setting mSetting;
    private List<SettingItems> list;
    private ListView lv_setting_itemlists;
    private SettingItems si_setting_startlockpackage;
    private SettingItems si_setting_showLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //隐藏actionbar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        mSetting=this;
        application = (Myapplication) getApplication();

        list=new ArrayList<>();
        //获取专门存放自定义组合控件的layout文件，将里面的自定义控件都加入到listview中
       LinearLayout itemsforsetting = (LinearLayout) View.inflate(this, R.layout.itemsforsetting,null);
        SettingItems si_setting_update = (SettingItems) itemsforsetting.findViewById(R.id.si_setting_update);
        si_setting_showLocation = (SettingItems) itemsforsetting.findViewById(R.id.si_setting_showLocation);

        si_setting_startlockpackage=(SettingItems) itemsforsetting.findViewById(R.id.si_setting_startlockpackage);
        list.add(si_setting_update);
        list.add(si_setting_showLocation);
        list.add(si_setting_startlockpackage);
        itemsforsetting.removeAllViews();
        lv_setting_itemlists = (ListView) findViewById(R.id.lv_setting_itemlists);
        lv_setting_itemlists.setAdapter(myAdapter);

    }

    public static Setting getmActivity(){
        return mSetting;
    }
    BaseAdapter myAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SettingItems settingItems = list.get(position);

            return settingItems;
        }
    };

    //设置手机归属地显示框在屏幕所在位置
    public void showLocationSpace(View v){
        startActivity(new Intent(this,LocationShowSpaceSet.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取服务状态
        boolean serviceAlive1 = ServiceUtils.isServiceAlive(this, "com.zhangmh.service.MytelenumLoctionService");
        if(!serviceAlive1){
            si_setting_showLocation.systemStopedService();
        }

        boolean serviceAlive2 = ServiceUtils.isServiceAlive(this, "com.zhangmh.service.Mywatchappstart");
        if(!serviceAlive2){
            si_setting_startlockpackage.systemStopedService();
        }
    }
}
