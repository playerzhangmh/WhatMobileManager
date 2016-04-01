package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhangmh.Utils.HttpUtils;
import com.zhangmh.application.Myapplication;

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

public class SplashActivity extends Activity {

    private static final int MSG_OK = 1;
    private static final int MSG_SERVER_ERROR = 2;
    private static final int MSG_URL_ERROR =3 ;
    private static final int MSG_IO_ERROR =4 ;
    private static final int MSG_TIMEOUT =5 ;
    private static String Deman="http://192.168.3.77/Day10hw/";
    private ProgressBar pb_splash_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tv_splash_version= (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("version"+getVersion());
        pb_splash_update = (ProgressBar) findViewById(R.id.pb_splash_update);


        Myapplication application = (Myapplication) getApplication();
        SharedPreferences setting_sp = application.getSetting_sp();
        boolean autoUpdate = setting_sp.getBoolean("autoUpdate", true);
        if(autoUpdate){
            getLatestVersion();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg=handler.obtainMessage();
                    msg.what=MSG_TIMEOUT;
                    handler.sendMessage(msg);
                    //5s后跳到主页，这里有代码
                }
            }).start();

            //在这段时间里去把asset目录下的dbcopy到应用目录下
            copyDB();
        }


    }

    private void copyDB() {
        File file=new File(getFilesDir()+"/naddress.db");
        if(file.exists()){
            return;
        }
        FileOutputStream fos=null;
        AssetManager assets = getAssets();
        InputStream open =null;
        try {
            open = assets.open("naddress.db");
            fos=new FileOutputStream(file);
            byte[] b=new byte[1024];
            int len=-1;
            while ((len=open.read(b))!=-1){
                fos.write(b,0,len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(open!=null){
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
                            Toast.makeText(SplashActivity.this,"错误号jason10",Toast.LENGTH_SHORT).show();
                            enterHome();
                        }
                    }
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(SplashActivity.this,"网络没有连接",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    Toast.makeText(SplashActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    Toast.makeText(SplashActivity.this,"错误号URL"+MSG_URL_ERROR,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_TIMEOUT:
                    enterHome();
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
                Message msg=handler.obtainMessage();
                try {
                    URL url=new URL(Deman+"version.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode==200){
                        Log.v("hw2","responseCode==200");
                        InputStream is = conn.getInputStream();
                        msg.what=MSG_OK;
                        msg.obj=is;

                    }else {
                        msg.what=MSG_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what=MSG_URL_ERROR;

                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what=MSG_IO_ERROR;

                }
                finally {
                    handler.sendMessage(msg);
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
                                enterHome();
                            }
                        })
                        .show();
            }
            else{
                Toast.makeText(this,"已经是最新版本",Toast.LENGTH_LONG).show();
                //直接进入首页，代码待填
                enterHome();
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
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File file=new File(Environment.getExternalStorageDirectory()+"/newrelease.apk");
                    FileOutputStream fos=null;
                    try {
                        pb_splash_update.setVisibility(View.VISIBLE);
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
                        startActivityForResult(intent, 100);

                    }
                }else {
                    Toast.makeText(SplashActivity.this,"sd卡异常",Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
            Toast.makeText(SplashActivity.this,"获取资源失败",Toast.LENGTH_LONG).show();
            enterHome();
        }

        @Override
        public void onProgress(int bytesWritten, int totalSize) {
            super.onProgress(bytesWritten, totalSize);
            pb_splash_update.setMax(totalSize);
            pb_splash_update.setProgress(bytesWritten);
        }
    };


    //去主页
    public void enterHome(){

        startActivity(new Intent(this,Home.class));
        finish();
    }

    //取消安装的话，直接进入主页


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            enterHome();
        }
    }


  /*  @Override只有物理按键才有作用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("hw2",keyCode+"aaaaaaaaaaaaaa");
        return false;
    }*/
}
