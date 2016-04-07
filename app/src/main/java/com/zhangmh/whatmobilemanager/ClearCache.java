package com.zhangmh.whatmobilemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Utils.MypackageUtils;
import com.zhangmh.bean.Appinfo;
import com.zhangmh.bean.CacheInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClearCache extends ActionBarActivity {

    private ProgressBar pb_clearcache_scanprogress;
    private TextView tv_clearcache_scan;
    private TextView bt_clearcache_clear;
    private ListView lv_clearcache_cacheitems;
    private List<CacheInfo> cacheInfoList;
    private List<Appinfo> appInfolist;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        pb_clearcache_scanprogress = (ProgressBar) findViewById(R.id.pb_clearcache_scanprogress);
        tv_clearcache_scan = (TextView) findViewById(R.id.tv_clearcache_scan);
        bt_clearcache_clear = (TextView) findViewById(R.id.bt_clearcache_clear);
        lv_clearcache_cacheitems = (ListView) findViewById(R.id.lv_clearcache_cacheitems);
        cacheInfoList = new ArrayList<>();
        appInfolist=new ArrayList<>();
        asyncTask.execute();
    }


    private boolean cancelflag;
    AsyncTask<Void,Integer,Void> asyncTask=new AsyncTask<Void,Integer,Void>() {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cancelflag=false;

            appInfolist = MypackageUtils.getAppInfolist(ClearCache.this);
            pb_clearcache_scanprogress.setMax(appInfolist.size());
            packageManager = ClearCache.this.getPackageManager();

        }

        @Override
        protected Void doInBackground(Void... params) {

            int count=0;
            for(Appinfo appinfo: appInfolist){

                try {
                    Class<?> aClass = ClearCache.this.getClassLoader().loadClass("android.content.pm.PackageManager");
                    Method getPackageSizeInfo = aClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                    getPackageSizeInfo.invoke(packageManager,appinfo.getPackagename(),new MyPackageStatsObserver());

                    if(cancelflag){
                        cancel(true);
                        return null;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(++count);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_clearcache_scan.setText("扫描完成");
            if(cacheInfoList.size()>0){
                lv_clearcache_cacheitems.setAdapter(mycacheAdapter);
                bt_clearcache_clear.setVisibility(View.VISIBLE);
                bt_clearcache_clear.setOnClickListener(onClickListener);
            }else {
                Toast.makeText(ClearCache.this,"没有缓存可以清理",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb_clearcache_scanprogress.setProgress(values[0]);
        }
    };

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Class<?> aClass = ClearCache.this.getClassLoader().loadClass("android.content.pm.PackageManager");
                Method freeStorageAndNotify = aClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
                freeStorageAndNotify.invoke(packageManager,Long.MAX_VALUE,new MyPackageDataObserver());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    };

    BaseAdapter mycacheAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return cacheInfoList.size();
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

            CacheInfo cacheInfo = cacheInfoList.get(position);
            MycacheHolder holder=new MycacheHolder();
            View view =null;
            if(convertView!=null){
                view=convertView;
                holder = (MycacheHolder) view.getTag();
            }else {
                view = View.inflate(ClearCache.this, R.layout.item_clearcache_tag, null);
                holder.iv_cacheitem_icon= (ImageView) view.findViewById(R.id.iv_cacheitem_icon);
                holder.tv_cacheitem_label= (TextView) view.findViewById(R.id.tv_cacheitem_label);
                holder.tv_cacheitem_cache= (TextView) view.findViewById(R.id.tv_cacheitem_cache);
                view.setTag(holder);
            }
            holder.iv_cacheitem_icon.setImageDrawable(cacheInfo.getIcon());
            holder.tv_cacheitem_label.setText(cacheInfo.getLabel());
            holder.tv_cacheitem_cache.setText(cacheInfo.getCache());

            return view;
        }
    };
    class MycacheHolder{
        ImageView iv_cacheitem_icon;
        TextView tv_cacheitem_label;
        TextView tv_cacheitem_cache;

    }

    class MyPackageStatsObserver extends IPackageStatsObserver.Stub{

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cacheSize = pStats.cacheSize;
            String cachesize= Formatter.formatFileSize(ClearCache.this,cacheSize);
            final String packageName = pStats.packageName;
            Log.v("hw2",cacheSize+packageName);

            if(cacheSize/1024>12){
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                    CharSequence charSequence = applicationInfo.loadLabel(packageManager);
                    Drawable drawable = applicationInfo.loadIcon(packageManager);
                    CacheInfo cacheInfo=new CacheInfo(charSequence.toString(),drawable,cachesize,packageName);
                    cacheInfoList.add(cacheInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Log.v("hw2",packageName+"无法使用");
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_clearcache_scan.setText("正在扫描:"+packageName);
                }
            });
        }
    }


    class MyPackageDataObserver extends IPackageDataObserver.Stub{

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            if(cacheInfoList.size()>0){
                cacheInfoList.clear();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mycacheAdapter.notifyDataSetChanged();
                    Toast.makeText(ClearCache.this,"清理完毕",Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       cancelflag=true;
       // asyncTask.cancel(true); //无法完全取消，当back时只是阻塞在这里了，再次进入的时候，会接着之前的继续
        //但是如果是用子线程做这部分，用handler往追按成发消息，就可以直接打断

    }
}
