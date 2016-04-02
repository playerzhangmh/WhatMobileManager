package com.zhangmh.whatmobilemanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Dao.PackageDbDao;
import com.zhangmh.Utils.MypackageUtils;
import com.zhangmh.Utils.WindowMeasureUtils;
import com.zhangmh.application.Myapplication;
import com.zhangmh.bean.Appinfo;

import java.util.ArrayList;
import java.util.List;

public class MyPackageManager extends Activity implements View.OnClickListener{

    private TextView tv_mypackageManager_romstorage;
    private TextView tv_mypackageManager_sdstorage;
    private TextView tv_mypackageManager_appnum;
    private ListView lv_mypackageManager_app;
    private List<Appinfo> appinfoList;
    private List<Appinfo> systemappinfoList;
    private List<Appinfo> userappinfoList;
    private LinearLayout popupwindow_app;
    private LinearLayout ll_popupwindow_start;
    private LinearLayout ll_popupwindow_share;
    private LinearLayout ll_popupwindow_uninstall;
    private PopupWindow popupWindow;
    private Appinfo appinfo;
    private Myapplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_package_manager);
        application = (Myapplication) getApplication();
        appinfoList = new ArrayList<>();
        systemappinfoList = new ArrayList<>();
        userappinfoList = new ArrayList<>();
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
    public void refreshList(){
        appinfoList.clear();
        systemappinfoList.clear();
        userappinfoList.clear();
        appinfoList= MypackageUtils.getAppInfolist(MyPackageManager.this);
        for(Appinfo appinfo:appinfoList){
            if(appinfo.isSystem()){
                systemappinfoList.add(appinfo);
            }else {
                userappinfoList.add(appinfo);
            }
        }
    }
    //用异步操作来解决获取应用列表的耗时操作
    AsyncTask asyncTask=new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {

           refreshList();

            return null;
        }



        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

                lv_mypackageManager_app.setAdapter(adapter);
            lv_mypackageManager_app.setOnItemClickListener(onItemClickListener);
            lv_mypackageManager_app.setOnItemLongClickListener(onItemLongClickListener);
            lv_mypackageManager_app.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                    popupWindow=null;
                    if (firstVisibleItem <= (userappinfoList.size())) {
                        tv_mypackageManager_appnum.setText("用户应用" + userappinfoList.size() + "个");
                    } else {
                        tv_mypackageManager_appnum.setText("系统应用" + systemappinfoList.size() + "个");
                    }

                }

            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




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
            appinfo = new Appinfo();
            if(position<=userappinfoList.size()){
                appinfo = userappinfoList.get(position - 1);

            }else if(position>=(userappinfoList.size()+2)){
                appinfo = systemappinfoList.get(position - 2-userappinfoList.size());
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
            if(PackageDbDao.queryApp(MyPackageManager.this,appinfo.getPackagename()).isEmpty()){
                holder.iv_appitem_manager.setImageResource(R.drawable.unlock);
            }else {
                holder.iv_appitem_manager.setImageResource(R.drawable.lock);
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

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position<=userappinfoList.size()){
                appinfo = userappinfoList.get(position - 1);

            }else if(position>=(userappinfoList.size()+2)){
                appinfo = systemappinfoList.get(position - 2-userappinfoList.size());
            }
            popupwindow_app = (LinearLayout) View.inflate(MyPackageManager.this, R.layout.popupwindow_app, null);
            ll_popupwindow_start = (LinearLayout) popupwindow_app.findViewById(R.id.ll_popupwindow_start);
            ll_popupwindow_share = (LinearLayout) popupwindow_app.findViewById(R.id.ll_popupwindow_share);
            ll_popupwindow_uninstall = (LinearLayout) popupwindow_app.findViewById(R.id.ll_popupwindow_uninstall);
            ll_popupwindow_share.setOnClickListener(MyPackageManager.this);
            ll_popupwindow_start.setOnClickListener(MyPackageManager.this);
            ll_popupwindow_uninstall.setOnClickListener(MyPackageManager.this);


            if(popupWindow==null){
                popupWindow = new PopupWindow(popupwindow_app, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }else {
                popupWindow.dismiss();
            }
            int[] location=new int[2];
            view.getLocationOnScreen(location);
            popupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP,location[0]+( WindowMeasureUtils.getWindowMeasure(MyPackageManager.this)[0]/6),location[1]-15);



        }
    };

    AdapterView.OnItemLongClickListener onItemLongClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(popupWindow!=null){
                popupWindow.dismiss();
            }
            popupWindow=null;

            if(position>0&&position<=userappinfoList.size()){
                appinfo = userappinfoList.get(position - 1);
            }else if(position>=(userappinfoList.size()+2)){
                appinfo = systemappinfoList.get(position - 2-userappinfoList.size());
            }else {
               appinfo=null;
            }
            //通过长按来确定是否要给应用枷锁或解锁
            if(appinfo!=null){
                String packagename = appinfo.getPackagename();
                ImageView iv_appitem_manager= (ImageView) view.findViewById(R.id.iv_appitem_manager);

                String querylabel = PackageDbDao.queryApp(MyPackageManager.this, packagename);
                if(querylabel.isEmpty()){
                    PackageDbDao.insertApp(MyPackageManager.this,packagename,appinfo.getLabel());
                    iv_appitem_manager.setImageResource(R.drawable.lock);

                }else {
                    PackageDbDao.deleteApp(MyPackageManager.this,packagename);
                    iv_appitem_manager.setImageResource(R.drawable.unlock);

                }
            }

            return true;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_popupwindow_start:
                startPackage();
                break;
            case R.id.ll_popupwindow_share:
                sharePackage();
                break;
            case R.id.ll_popupwindow_uninstall:
                uninstallPackage();
                break;
        }
    }

    public void startPackage(){
        String packagename = appinfo.getPackagename();
        PackageManager packageManager = getPackageManager();
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packagename);
        startActivity(launchIntentForPackage);
    }
    public void sharePackage(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "推荐一个超烂的APP给你玩-》》" + appinfo.getLabel() + "下载地址XXXXX");
        intent.setType("text/plain");
        startActivity(intent);
    }
    public void uninstallPackage(){
        if(appinfo.getPackagename().equals(getPackageName())){
            Toast.makeText(MyPackageManager.this,"无法卸载自己",Toast.LENGTH_LONG).show();
            return;
        }
        if(appinfo.isSystem()){
            Toast.makeText(MyPackageManager.this,"无法卸载系统应用",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + appinfo.getPackagename()));
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        new Thread(){
            @Override
            public void run() {
                super.run();
                refreshList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();//此处可用于将数据存于内存中的方法，由于我们数据由磁盘读取，所以暂时用不着
                        popupWindow.dismiss();
                    }
                });

            }
        }.start();


    }
}
