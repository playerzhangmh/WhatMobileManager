package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangmh.Utils.MypackageUtils;
import com.zhangmh.bean.Appinfo;

import java.util.ArrayList;
import java.util.List;

public class MyPackageManager extends Activity {

    private TextView tv_mypackageManager_romstorage;
    private TextView tv_mypackageManager_sdstorage;
    private TextView tv_mypackageManager_appnum;
    private ListView lv_mypackageManager_app;
    private List<Appinfo> appinfoList;
    private List<Appinfo> systemappinfoList;
    private List<Appinfo> userappinfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_package_manager);

        tv_mypackageManager_romstorage = (TextView) findViewById(R.id.tv_mypackageManager_romstorage);
        tv_mypackageManager_sdstorage = (TextView) findViewById(R.id.tv_mypackageManager_sdstorage);
        tv_mypackageManager_appnum = (TextView) findViewById(R.id.tv_mypackageManager_appnum);
        lv_mypackageManager_app = (ListView) findViewById(R.id.lv_mypackageManager_app);
        String availableSDstorage = MypackageUtils.getAvailableSDstorage(MyPackageManager.this);
        if(availableSDstorage.isEmpty()){
            tv_mypackageManager_sdstorage.setText("SD卡异常");

        }else {
            tv_mypackageManager_sdstorage.setText(availableSDstorage);
        }
        tv_mypackageManager_romstorage.setText(MypackageUtils.getAvailableROMstorage(this));

        asyncTask.execute();


    }
    //用异步操作来解决获取应用列表的耗时操作
    AsyncTask asyncTask=new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {

            appinfoList= MypackageUtils.getAppInfolist(MyPackageManager.this);
            for(Appinfo appinfo:appinfoList){
                if(appinfo.isSystem()){
                    systemappinfoList.add(appinfo);
                }else {
                    userappinfoList.add(appinfo);
                }
                publishProgress(10);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            lv_mypackageManager_app.setAdapter(adapter);
            lv_mypackageManager_app.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if(firstVisibleItem<=(userappinfoList.size())){
                        tv_mypackageManager_appnum.setText("用户应用" + userappinfoList.size() + "个");
                    }else {
                        tv_mypackageManager_appnum.setText("系统应用"+systemappinfoList.size()+"个");

                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appinfoList = new ArrayList<>();
            systemappinfoList = new ArrayList<>();
            userappinfoList = new ArrayList<>();

        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    };
    BaseAdapter adapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return appinfoList.size();
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

            LinearLayout view=null;
            PackageHolder holder=new PackageHolder();
            if(position==0){
                TextView tv=new TextView(MyPackageManager.this);
                tv.setText("用户应用" + userappinfoList.size() + "个");
                tv.setTextSize(20);
                tv.setTextColor(Color.RED);
                return tv;
            }
            if(position==userappinfoList.size()+1){
                TextView tv=new TextView(MyPackageManager.this);
                tv.setText("系统应用"+systemappinfoList.size()+"个");
                tv.setTextSize(20);
                tv.setTextColor(Color.RED);
                return tv;
            }

            if(convertView!=null&&convertView instanceof LinearLayout){
                view= (LinearLayout) convertView;
                holder= (PackageHolder) view.getTag();
            }else {
                view= (LinearLayout) View.inflate(MyPackageManager.this,R.layout.item_appdetail,null);
                holder.tv_appitem_label= (TextView) view.findViewById(R.id.tv_appitem_label);
                holder.tv_appitem_storage= (TextView) view.findViewById(R.id.tv_appitem_storage);
                holder.iv_appitem_icon = (ImageView) view.findViewById(R.id.iv_appitem_icon);
                holder.iv_appitem_manager = (ImageView) view.findViewById(R.id.iv_appitem_manager);
                view.setTag(holder);
            }
            Appinfo appinfo=new Appinfo();
            if(position<=userappinfoList.size()){
                appinfo= userappinfoList.get(position - 1);

            }else if(position>=(userappinfoList.size()+2)){
                appinfo= systemappinfoList.get(position - 2-userappinfoList.size());
            }


            //Log.v("hw2",appinfo.getLabel()+"listview");
            holder.tv_appitem_label.setText(appinfo.getLabel());
            //Log.v("hw2",holder.iv_appitem_icon.toString());
            holder.iv_appitem_icon.setImageDrawable(appinfo.getIcon());
            if(appinfo.isSDcard()){
                holder.tv_appitem_storage.setText("SD卡");
            }else {
                holder.tv_appitem_storage.setText("手机内存");

            }



            return view;
        }
    };


    class PackageHolder{
        TextView tv_appitem_label;
        TextView tv_appitem_storage;
        ImageView iv_appitem_icon;
        ImageView iv_appitem_manager;
    }
}
