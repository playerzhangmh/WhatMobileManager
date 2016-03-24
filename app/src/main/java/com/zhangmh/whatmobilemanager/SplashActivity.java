package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhangmh.Utils.HttpUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends ActionBarActivity {

    private static final int MSG_OK = 1;
    private static String Deman="http://192.168.3.77/Day10hw/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //5s后跳到主页，这里有代码
            }
        }).start();


        //该处缺少判断设置中是否自动检查更新的判断代码，后面需补
        getLatestVersion();
    }
    //主线程消息处理handler
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_OK:
                    InputStream obj = (InputStream) msg.obj;
                    String result= HttpUtils.getTextFromInputStream(obj);
                    if(result.length()>0){
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String newversion=jsonObject.getString("version");
                            String downloadurl=jsonObject.getString("downloadurl");
                            String description = jsonObject.getString("description");
                            Log.v("hw2",newversion+"--"+downloadurl+"--"+description);

                            handleIsnewVersion(newversion,description,downloadurl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };


    //去服务器请求最新版本号和最新apk地址
    public void getLatestVersion(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url=new URL(Deman+"version.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode==200){
                        Log.v("hw2","responseCode==200");
                        InputStream is = conn.getInputStream();
                        Message msg=handler.obtainMessage();
                        msg.what=MSG_OK;
                        msg.obj=is;
                        handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //获取当前版本号
    public String getVersion(){
        String versionName ="";
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    //针对请求到的版本号来做不同的处理
    public void handleIsnewVersion(String newversion,String description, final String downloadurl){
        String version = getVersion();
        Log.v("hw2",version+"---"+newversion);
        if(version.length()>0){
            if(Float.parseFloat(version)<Float.parseFloat(newversion)){
                new AlertDialog.Builder(this)
                        .setTitle("新版本提醒")
                        .setMessage(description)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //根据新获得url去下载并安装
                                downloadANDinstall(downloadurl);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //直接进入首页，代码待填写
                            }
                        })
                        .show();
            }
            else{
                Toast.makeText(this,"已经是最新版本",Toast.LENGTH_LONG).show();
                //直接进入首页，代码待填
            }
        }
    }

    //去下载并安装
    public void downloadANDinstall(String downloadurl ){
        String path=Deman+downloadurl;
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(path,asyncHandler);
    }

    AsyncHttpResponseHandler asyncHandler=new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            if(statusCode==200){
                Log.v("hw2","statusCode==200");
                File file=new File(Environment.getExternalStorageDirectory()+"/newrelease.apk");
                FileOutputStream fos=null;
                try {
                    fos=new FileOutputStream(file);
                    fos.write(responseBody,0,responseBody.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(fos!=null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (file.exists()){
                    //调用系统来安装
                    Intent intent =new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(intent);

                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
            Toast.makeText(SplashActivity.this,"获取资源失败",Toast.LENGTH_LONG).show();
        }
    };

}
