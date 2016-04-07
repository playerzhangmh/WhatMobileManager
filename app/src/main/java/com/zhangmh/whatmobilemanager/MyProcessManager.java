package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangmh.Utils.MyProcessUtils;
import com.zhangmh.application.Myapplication;
import com.zhangmh.bean.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

public class MyProcessManager extends Activity {

    private Myapplication application;
    private TextView tv_myprocessManager_processCount;
    private TextView tv_myprocessManager_ram;
    private ListView lv_myprocessManager_process;
    private List<ProcessInfo> userprocesslist;
    private List<ProcessInfo> systemprocesslist;
    private List<ProcessInfo> allrunningprocesslist;
    private List<ProcessInfo> checkedProcesslist;
    private ProcessInfo processInfo;
    LinearLayout linearLayout;//当前的一项
    private int runningProcess;
    private String totalRam;
    private String availableRam;
    private TextView tv_myprocessManager_procseenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_process_manager);

        allrunningprocesslist = new ArrayList<>();
        userprocesslist = new ArrayList<>();
        systemprocesslist = new ArrayList<>();
        checkedProcesslist = new ArrayList<>();

        application = (Myapplication) getApplication();
        tv_myprocessManager_processCount = (TextView) findViewById(R.id.tv_myprocessManager_processCount);
        tv_myprocessManager_ram = (TextView) findViewById(R.id.tv_myprocessManager_RAM);
        tv_myprocessManager_procseenum = (TextView) findViewById(R.id.tv_myprocessManager_procseenum);
        lv_myprocessManager_process = (ListView) findViewById(R.id.lv_myprocessManager_process);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.zhangmh.watchProcess");
        registerReceiver(broadcastReceiver,intentFilter);
        asyncTask.execute();



    }


    AsyncTask asyncTask=new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {

            runningProcess = MyProcessUtils.getRunningProcess(MyProcessManager.this);
            totalRam = MyProcessUtils.getTotalRam(MyProcessManager.this);
            availableRam = MyProcessUtils.getAvailableRam(MyProcessManager.this);
            allrunningprocesslist= MyProcessUtils.getRunningProcessList(MyProcessManager.this);
            refresh();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tv_myprocessManager_processCount.setText(runningProcess + "个");
            tv_myprocessManager_ram.setText(availableRam+"/"+totalRam);
            lv_myprocessManager_process.setAdapter(adapter);
            lv_myprocessManager_process.setOnItemClickListener(onItemClickListener);
            lv_myprocessManager_process.setOnScrollListener(onScrollListener);
        }
    };

    //初始化加刷新
    public void refresh(){
        if(userprocesslist.size()>0){
            userprocesslist.clear();
        }
        if (systemprocesslist.size()>0){
            systemprocesslist.clear();
        }
        if(checkedProcesslist.size()>0){
            checkedProcesslist.clear();
        }
        for(ProcessInfo processInfo:allrunningprocesslist){
            if(processInfo.isSystem()){
                systemprocesslist.add(processInfo);
            }else {
                userprocesslist.add(processInfo);
            }
            if(processInfo.isChecked()&&!getPackageName().equals(processInfo.getPackagename())&&!processInfo.getRam().equals("未知")){
                checkedProcesslist.add(processInfo);
            }
        }
    }

    BaseAdapter adapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return allrunningprocesslist.size();
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
            if(position==0){
                TextView tv=new TextView(MyProcessManager.this);
                tv.setText("用户进程("+userprocesslist.size()+")个");
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextSize(20);
                return tv;
            }
            if(position==userprocesslist.size()+1){
                TextView tv=new TextView(MyProcessManager.this);
                tv.setText("系统进程("+systemprocesslist.size()+")个");
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextSize(20);
                return tv;
            }
            processInfo = new ProcessInfo();

            MyprocessHolder holder=new MyprocessHolder();
            if(position>0&&position<=userprocesslist.size()){
                processInfo =userprocesslist.get(position-1);
            }else if(position>userprocesslist.size()+1){
                processInfo =systemprocesslist.get(position-userprocesslist.size()-2);
            }
            if(convertView!=null&&convertView instanceof LinearLayout){
                linearLayout= (LinearLayout) convertView;
                holder= (MyprocessHolder) linearLayout.getTag();
            }else {
                linearLayout= (LinearLayout) View.inflate(MyProcessManager.this,R.layout.item_processdetail,null);
                holder.iv_processitem_icon= (ImageView) linearLayout.findViewById(R.id.iv_processitem_icon);
                holder.tv_processitem_label= (TextView) linearLayout.findViewById(R.id.tv_processitem_label);
                holder.tv_processitem_RAM= (TextView) linearLayout.findViewById(R.id.tv_processitem_RAM);
                holder.cb_processitem_killtag= (CheckBox) linearLayout.findViewById(R.id.cb_processitem_killtag);
                linearLayout.setTag(holder);
            }
            holder.iv_processitem_icon.setImageDrawable(processInfo.getDrawable());
            holder.tv_processitem_label.setText(processInfo.getLabel());
            holder.tv_processitem_RAM.setText(processInfo.getRam());
            holder.cb_processitem_killtag.setChecked(processInfo.isChecked());

            return linearLayout;
        }
    };

    class MyprocessHolder{
        TextView tv_processitem_label;
        TextView tv_processitem_RAM;
        ImageView iv_processitem_icon;
        CheckBox cb_processitem_killtag;

    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(position>0&&position<=userprocesslist.size()){
                processInfo =userprocesslist.get(position-1);
            }else if(position>userprocesslist.size()+1){
                processInfo =systemprocesslist.get(position-userprocesslist.size()-2);
            }else {
                return;
            }
            linearLayout= (LinearLayout) view;
            CheckBox cb_processitem_killtag = (CheckBox) linearLayout.findViewById(R.id.cb_processitem_killtag);
            if(cb_processitem_killtag.isChecked()){
                cb_processitem_killtag.setChecked(false);
                processInfo.setIsChecked(false);
            }else {
                cb_processitem_killtag.setChecked(true);
                processInfo.setIsChecked(true);
            }
        }
    };


    AbsListView.OnScrollListener onScrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem>=userprocesslist.size()+1) {
                tv_myprocessManager_procseenum.setText("系统进程(" + systemprocesslist.size() + ")个");
            }else {
                tv_myprocessManager_procseenum.setText("用户进程("+userprocesslist.size()+")个");
            }

        }
    };


    public void checkAll(View v){

        for (ProcessInfo processInfo:allrunningprocesslist){
            processInfo.setIsChecked(true);
        }
        refresh();
        adapter.notifyDataSetChanged();
        Log.v("hw2",checkedProcesslist.toString()+"@@@@@@");
    }
    public void oppositeCheck(View v){
        for (ProcessInfo processInfo:allrunningprocesslist){
            if(processInfo.isChecked()){
                processInfo.setIsChecked(false);
            }else {
                processInfo.setIsChecked(true);
            }
        }
        refresh();
        Log.v("hw2", checkedProcesslist.toString() + "------");
        adapter.notifyDataSetChanged();

    }
    public void cancelCheck(View v){
        for (ProcessInfo processInfo:allrunningprocesslist){
                processInfo.setIsChecked(false);
        }
        refresh();
        Log.v("hw2", checkedProcesslist.toString() + "%%%%%%%%");
        adapter.notifyDataSetChanged();
    }
    public void clearCheckedProcess(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {
                refresh();
                ActivityManager am= (ActivityManager) getSystemService(MyProcessManager.ACTIVITY_SERVICE);
                Log.v("clearCheckedProcess",checkedProcesslist.toString());
                for(ProcessInfo processInfo:checkedProcesslist){
                    String packagename = processInfo.getPackagename();
                    am.killBackgroundProcesses(packagename);
                }
                Message message = handler.obtainMessage();
                message.what=1;
                message.obj=MyProcessUtils.getRunningProcessList(MyProcessManager.this);;
                handler.sendMessage(message);
            }
        }).start();
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("hw2","MyProcessManagerReceive");
            Message message = handler.obtainMessage();
            message.what=2;
            handler.sendMessage(message);
        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    allrunningprocesslist= (List<ProcessInfo>) msg.obj;
                    refresh();
                    adapter.notifyDataSetChanged();
                    tv_myprocessManager_processCount.setText(MyProcessUtils.getRunningProcess(MyProcessManager.this) + "");
                    tv_myprocessManager_ram.setText(MyProcessUtils.getAvailableRam(MyProcessManager.this)+"/"+MyProcessUtils.getTotalRam(MyProcessManager.this));
                    Intent intent=new Intent("com.zhangmh.myProcessManager");
                    intent.putExtra("nosendBroadflag",true);
                    sendBroadcast(intent);
                    break;
                case 2:
                    //service检测到
                    //此处有bug，由于service每隔2s发一个广播过来，将所有的check都置为了false，所以导致手动选取出问题了
                    for (ProcessInfo processInfo:allrunningprocesslist){
                        if(processInfo.isChecked()){
                            return;
                        }
                    }
                    allrunningprocesslist=MyProcessUtils.getRunningProcessList(MyProcessManager.this);
                    refresh();
                    adapter.notifyDataSetChanged();
                    tv_myprocessManager_processCount.setText(MyProcessUtils.getRunningProcess(MyProcessManager.this) + "");
                    tv_myprocessManager_ram.setText(MyProcessUtils.getAvailableRam(MyProcessManager.this)+"/"+MyProcessUtils.getTotalRam(MyProcessManager.this));
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        Intent intent=new Intent("com.zhangmh.myProcessManager");
        intent.putExtra("nosendBroadflag",false);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ProcessInfo processInfo:allrunningprocesslist){
            processInfo.setIsChecked(false);
        }
        adapter.notifyDataSetChanged();
    }
}
