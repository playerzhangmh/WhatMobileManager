package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Dao.BlacknumDbDao;

import java.util.ArrayList;
import java.util.List;

public class CommuniGuard extends Activity {

    private ListView lv_commGuard_blacknumlist;
    private Button bt_commGuard_add;
    private List<String> blacknumpartlist;
    private BlacknumDbDao dao;
    boolean cursorFlag;
    boolean broadcastFlag;
    private List<String> partblacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communi_guard);
        blacknumpartlist=new ArrayList<>();
        bt_commGuard_add = (Button) findViewById(R.id.bt_commGuard_add);
        lv_commGuard_blacknumlist = (ListView) findViewById(R.id.lv_commGuard_blacknumlist);
        asyncTask.execute();
    }


    private int offset=0;
    private final static int LIMIT=10;
    AsyncTask<Void,Integer,Void> asyncTask=new AsyncTask() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dao = new BlacknumDbDao(CommuniGuard.this);
            lv_commGuard_blacknumlist.setAdapter(blacknumAdapter);
            lv_commGuard_blacknumlist.setOnItemClickListener(onItemClickListener);
            lv_commGuard_blacknumlist.setOnItemLongClickListener(onItemLongClickListener);
            lv_commGuard_blacknumlist.setOnScrollListener(onScrollListener);
            bt_commGuard_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View inflate = View.inflate(CommuniGuard.this, R.layout.adddialog_blacknum, null);
                    final EditText et_addblacknum_num = (EditText) inflate.findViewById(R.id.et_addblacknum_num);
                    final RadioGroup rp_addblacknum_check = (RadioGroup) inflate.findViewById(R.id.rp_addblacknum_check);
                    Button bt_addblacknum_confirm = (Button) inflate.findViewById(R.id.bt_addblacknum_confirm);
                    Button bt_addblacknum_cancel = (Button) inflate.findViewById(R.id.bt_addblacknum_cancel);
                    final AlertDialog alertDialog = new AlertDialog.Builder(CommuniGuard.this)
                            .setView(inflate)
                            .create();
                    alertDialog.show();


                    bt_addblacknum_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String num = et_addblacknum_num.getText().toString();
                            int checkedRadioButtonId = rp_addblacknum_check.getCheckedRadioButtonId();
                            if (num.matches("1[34578]\\d{9}") || num.matches("^1\\d{2}$|\\d{5}$|^55\\d{2}$|^\\d{7}|\\d{8}$")) {
                                switch (checkedRadioButtonId) {
                                    case R.id.rb_addblacknum_sms:
                                        checkedRadioButtonId = 1;
                                        break;
                                    case R.id.rb_addblacknum_call:
                                        checkedRadioButtonId = 2;
                                        break;
                                    case R.id.rb_addblacknum_all:
                                        checkedRadioButtonId = 3;
                                        break;
                                }
                                long insertblacknum = dao.insertblacknum(checkedRadioButtonId, num);
                                if (insertblacknum > -1) {
                                    blacknumpartlist.add(num + "@" + checkedRadioButtonId);
                                    blacknumAdapter.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(CommuniGuard.this, "加入黑名单失败", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CommuniGuard.this, "请输入合法的号码", Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    bt_addblacknum_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        protected Object doInBackground(Object[] params) {
            int count=0;
            while (!cursorFlag){
                if(!broadcastFlag){
                    partblacklist = dao.getPartblacklist(offset, LIMIT);
                    publishProgress(++count);
                }
                //如果不是什么耗时操作，尽量不要放在这里做，涉及线程安全问题
                //如果要做，可以让子线程睡一会，或者跑慢点，不然你子线程数据
                // 更新过快，导致你主线程添加的都是相同内容，相对我这里来说
                //如果不睡的话，在主线程没有放映过来前， publishProgress(++count);
                //这个就会执行多次（20+），尽管broadcastFlag明明为true，还是能跑这么多
                //证明在onprogressUpdate被触发之前，由于broadcastFlag还是false，所以循环会在短时间内多跑很多次
                //多次调用publishProgress(++count)，当子线程收到broadcastFlag被修改的消息后，才会停止调用这个方法
                //而之前多次调用产生的数据还是被onprogressUpdate收到，就会一次性打印多个为true的log，而加入睡眠后
                //会使得onprogressUpdate及时被调用到，使得broadcastFlag被及时修改，所以能够阻止publishProgress(++count)
                //多次调用

                //Android, ListView IllegalStateException: “The content of the adapter has changed but ListView did not receive a notification”
                //同样可以证明为什么我们之前在手机杀毒中，遇到listview的bug，很显然是因为子线程publishProgress和list添加的
                //操作在短时间内进行了多次，1.子线程list+1添加之后，当某次主线程notify之后，adapter发生改变，准备去调整listview的时候发现之前list+2
                // 又发生了变化，但却没收到通知，就会报这个异常，即刚准备对前一个notify做响应操作的时候，list又发生了变化，但此时下一个通知还没来得及
                //发送过来
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            Log.v("doInBackground", "onProgressUpdate" + broadcastFlag+values[0]);
            if(!broadcastFlag){
                blacknumpartlist.addAll(partblacklist);
                broadcastFlag = true;
                Log.v("doInBackground", blacknumpartlist.size() + blacknumpartlist.toString());
                blacknumAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    };
    AbsListView.OnScrollListener onScrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int lastVisiblePosition = view.getLastVisiblePosition();
            if(lastVisiblePosition<(dao.getBlacknumlistcount()-1)){
                if(scrollState==SCROLL_STATE_IDLE&& lastVisiblePosition ==(offset+LIMIT-1)){
                    broadcastFlag=false;
                    offset=offset+LIMIT;
                    Log.v("doInBackground","SCROLL_STATE_IDLE");
                }
            }else {
                cursorFlag=true;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击删除
            final String blackitem = blacknumpartlist.get(position);
            final String[] split = blackitem.split("@");
            new AlertDialog.Builder(CommuniGuard.this)
                    .setTitle("删除黑名单")
                    .setMessage("请问您是否将其剔除黑名单")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int deleteblack = dao.deleteblack(split[0]);
                            if (deleteblack > 0) {
                                blacknumpartlist.remove(blackitem);
                                blacknumAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(CommuniGuard.this, "删除黑名单失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };
    AdapterView.OnItemLongClickListener onItemLongClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            //长按修改

            new AlertDialog.Builder(CommuniGuard.this)
                    .setTitle("请选择拦截模式")
                    .setSingleChoiceItems(new String[]{"拦截短信", "拦截电话", "拦截全部"}, 2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String blackitem = blacknumpartlist.get(position);
                            String[] split = blackitem.split("@");
                            int mode = which + 1;
                            String blacknum = split[0];
                            int updatedb = dao.updatedb(mode, blacknum);
                            if (updatedb > 0) {
                                blacknumpartlist.remove(blackitem);
                                blacknumpartlist.add(position, blacknum + "@" + mode);
                                blacknumAdapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();

                        }
                    })
                    .show();
            return true;
        }
    };


    BaseAdapter blacknumAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return blacknumpartlist.size();
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
            RelativeLayout view=null;
            if(blacknumpartlist.size()>0){
                String blackitem = blacknumpartlist.get(position);
                String[] split = blackitem.split("@");
                BlackitemHolder holder=new BlackitemHolder();
                if(convertView!=null){
                    view= (RelativeLayout) convertView;
                    holder= (BlackitemHolder) view.getTag();
                }else {
                    view= (RelativeLayout) View.inflate(CommuniGuard.this,R.layout.item_blacknumlist,null);
                    holder.tv_blacknumitem_num= (TextView) view.findViewById(R.id.tv_blacknumitem_num);
                    holder.tv_blacknumitem_mode= (TextView) view.findViewById(R.id.tv_blacknumitem_mode);
                    view.setTag(holder);
                }
                String mode="";
                switch (Integer.parseInt(split[1])){
                    case 1:
                        mode="拦截短信";
                        break;
                    case 2:
                        mode="拦截电话";
                        break;
                    case 3:
                        mode="拦截全部";
                        break;
                }
                holder.tv_blacknumitem_mode.setText(mode);
                holder.tv_blacknumitem_num.setText(split[0]);
            }

            return view;
        }
    };

    class BlackitemHolder{
        TextView tv_blacknumitem_num;
        TextView tv_blacknumitem_mode;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cursorFlag=true;
    }
}
