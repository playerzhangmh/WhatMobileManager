package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Dao.VirusDao;
import com.zhangmh.Utils.Md5Utils;
import com.zhangmh.Utils.MypackageUtils;
import com.zhangmh.bean.Appinfo;
import com.zhangmh.bean.AppscanVirusInfo;

import java.util.ArrayList;
import java.util.List;

public class KillVirus extends ActionBarActivity {

    private ImageView iv_killvirus_scanact;
    private TextView tv_killvirus_scan;
    private ProgressBar pb_killvirus_scanprogress;
    private ListView lv_killvirus_scanallapp;
    private List<AppscanVirusInfo> allPackageList;
    private List<AppscanVirusInfo> virusList;
    private Animation animation;
    private List<Appinfo> appInfolist;
    private PackageManager packageManager;
    private String scanningpackagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_virus);
        iv_killvirus_scanact = (ImageView) findViewById(R.id.iv_killvirus_scanact);
        tv_killvirus_scan = (TextView) findViewById(R.id.tv_killvirus_scan);
        pb_killvirus_scanprogress = (ProgressBar) findViewById(R.id.pb_killvirus_scanprogress);
        lv_killvirus_scanallapp = (ListView) findViewById(R.id.lv_killvirus_scanallapp);
        animation = new RotateAnimation(0,360, Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        iv_killvirus_scanact.setAnimation(animation);
        animation.start();
        allPackageList = new ArrayList<>();
        virusList = new ArrayList<>();
        boolean cancelled = asyncTask.isCancelled();
        asyncTask.execute();
    }

    private boolean cancelflag;
    AsyncTask<Void,Integer,Void> asyncTask=new AsyncTask<Void, Integer, Void>() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("AsyncTask","onPreExecute");
            cancelflag=false;
            packageManager = KillVirus.this.getPackageManager();
            appInfolist = MypackageUtils.getAppInfolist(KillVirus.this);
            pb_killvirus_scanprogress.setMax(appInfolist.size());
            lv_killvirus_scanallapp.setAdapter(adapter);

        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.v("AsyncTask", "doInBackground");

            int count=0;
            for(Appinfo appinfo: appInfolist){
                scanningpackagename = appinfo.getPackagename();
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(scanningpackagename, 0);
                    String sourceDir = applicationInfo.sourceDir;
                    String apkMd5 = Md5Utils.getApkMd5(sourceDir);
                    boolean isVirus = VirusDao.queryIsVirus(KillVirus.this, apkMd5);
                    if("cn.itcast.getkey".equals(scanningpackagename)){
                        Log.v("hw5",scanningpackagename+isVirus);

                    }

                    if(cancelflag){
                        cancel(true);
                        return null;
                    }
                    final AppscanVirusInfo appscanVirusInfo=new AppscanVirusInfo(sourceDir,isVirus,scanningpackagename);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            allPackageList.add(0, appscanVirusInfo);
                            Log.v("onPostExecute", allPackageList.size() + appscanVirusInfo.getPackagename());
                        }
                    });

                    if(isVirus){
                        virusList.add(appscanVirusInfo);
                    }
                    publishProgress(++count);
                    Log.v("AsyncTask", "publishProgress");


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.v("AsyncTask", "onPostExecute");

            animation.cancel();

            if(virusList.size()>0){
                new AlertDialog.Builder(KillVirus.this)
                        .show();
            }else {
                tv_killvirus_scan.setText("扫描完成，没有病毒");
                Toast.makeText(KillVirus.this,"系统很安全，没有病毒",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb_killvirus_scanprogress.setProgress(values[0]);
            tv_killvirus_scan.setText("正在扫描" + scanningpackagename);
            Log.v("AsyncTask", "onProgressUpdate");
            //lv_killvirus_scanallapp.requestLayout();
            adapter.notifyDataSetChanged();

            //This illegalStateException arises when a ui thread is updating the view and another background thread
            // changes the data again. That moment causes this issue.
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("AsyncTask", "onCancelled");

        }
    };

    BaseAdapter adapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return allPackageList.size();
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
            AppscanVirusInfo appscanVirusInfo = allPackageList.get(position);
            LinearLayout view =null;
            TextView tv=null;
            if(convertView!=null){
                view= (LinearLayout) convertView;
                tv= (TextView) view.getTag();
            }else {
                view= (LinearLayout) View.inflate(KillVirus.this,R.layout.item_scanvirus_appname,null);
                tv= (TextView) view.findViewById(R.id.tv_scanitem_appname);
                view.setTag(tv);
            }
            if(tv!=null){
                if(appscanVirusInfo.isVirus()){
                    Log.v("hw4","appscanVirusInfo.isVirus()"+appscanVirusInfo.getPackagename());
                    tv.setTextColor(Color.RED);
                }
                tv.setText(appscanVirusInfo.getPackagename());
            }
            if(appscanVirusInfo.getPackagename().equals("cn.itcase.getkey")){
                Log.v("hw5","cn.itcase.getkey");
            }
            return view;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelflag=true;
        //asyncTask.cancel(true);//无法取消
        //因为取消的是所在线程的操作，这里是取消了主线程的操作，而子线程还在继续遍历操作
    }
}
