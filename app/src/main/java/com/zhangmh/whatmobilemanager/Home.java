package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Utils.Md5Utils;
import com.zhangmh.application.Myapplication;

public class Home extends ActionBarActivity {
    private static final String MARQUEE="欢迎使用安全卫士，我们会用心保护您的手机，让您没有后顾之忧，请您随意使用";
    private static final String[] GRIDVIEW_ITEMS={"手机防盗","通讯卫士","软件管理",
    "进程管理","流量统计","手机杀毒",
    "缓存清理","高级工具","设置中心"
};
    private static final int[] GRIDVIEW_ICONS={R.raw.safe,R.raw.callmsgsafe,R.raw.app,R.raw.taskmanager,R.raw.netmanager,
    R.raw.trojan,R.raw.sysoptimize,R.raw.atools,R.raw.settings};
    private SharedPreferences setting_sp;
    private SharedPreferences.Editor editor;
    private static Myapplication myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //在这里我就直接获取myapplication，给SettingItem用，避免空引用

        myapplication= (Myapplication) getApplication();
        //隐藏actionbar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        TextView tv_home_marquee= (TextView) findViewById(R.id.tv_home_marquee);
        tv_home_marquee.setText(MARQUEE);
        tv_home_marquee.setSelected(true);

        //找到gridview
        GridView gv_home_content= (GridView) findViewById(R.id.gv_home_content);
        gv_home_content.setOnItemClickListener(itemClickListener);
        gv_home_content.setAdapter(myAdapter);

        //获得配置文件实例
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        editor = setting_sp.edit();

    }

    BaseAdapter myAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return GRIDVIEW_ICONS.length;
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
            LinearLayout view= (LinearLayout) View.inflate(Home.this,R.layout.gv_home_items,null);
            ImageView iv_gridview_icon= (ImageView) view.findViewById(R.id.iv_gridview_icon);
            TextView tv_gridview_content= (TextView) view.findViewById(R.id.tv_gridview_content);

            iv_gridview_icon.setImageResource(GRIDVIEW_ICONS[position]);
            tv_gridview_content.setText(GRIDVIEW_ITEMS[position]);
            return view;
        }
    };
    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           switch (position){
               case 0:
                   //弹出对话，根据配置文件，若有密码则验证密码，没有则设置密码
                   String guardAgainstThief = setting_sp.getString("GuardAgainstThief", "");
                   if(guardAgainstThief.isEmpty()){
                       setPasswordforGuardatDialog();
                   }else {
                       validatePasswordforGuardatDialog();
                   }

                   break;
               case 1:
                   break;
               case 2:
                   break;
               case 3:
                   break;
               case 4:
                   break;
               case 5:
                   break;
               case 6:
                   break;
               case 7:
                   break;
               case 8:
                   startActivity(new Intent(Home.this,Setting.class));
                   break;


           }
        }
    };

    private void validatePasswordforGuardatDialog() {

        //自定义dialog，setview来将view添加进去
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout guardat_validate = (LinearLayout) View.inflate(this, R.layout.guardat_validate, null);
        final EditText et_guardat_validatepwd = (EditText) guardat_validate.findViewById(R.id.et_guardat_validatepwd);
        Button bt_guardat_validate = (Button) guardat_validate.findViewById(R.id.bt_guardat_validate);
        Button bt_guardat_cancel = (Button) guardat_validate.findViewById(R.id.bt_guardat_cancel);
        builder.setView(guardat_validate);
        //把控件加入中后创建一个对话框并弹出来
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final String guardAgainstThief = setting_sp.getString("GuardAgainstThief", "");
        bt_guardat_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setpwd = Md5Utils.encrypt(et_guardat_validatepwd.getText().toString());
                if(!guardAgainstThief.isEmpty()){
                    if(!setpwd.isEmpty()){
                        if(setpwd.equals(guardAgainstThief)){
                            startActivity(new Intent(Home.this,GuardagainestThief.class));
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(Home.this,"密码不正确",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(Home.this,"输入不得为空",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Home.this,"系统异常",Toast.LENGTH_LONG).show();
                }
            }
        });

        bt_guardat_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void setPasswordforGuardatDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout guardat_setpwd = (LinearLayout) View.inflate(this, R.layout.guardat_setpwd, null);
        final EditText et_guardat_setpwd = (EditText) guardat_setpwd.findViewById(R.id.et_guardat_setpwd);
        final EditText et_guardat_confirmpwd = (EditText) guardat_setpwd.findViewById(R.id.et_guardat_confirmpwd);
        Button bt_guardat_validate = (Button) guardat_setpwd.findViewById(R.id.bt_guardat_validate);
        Button bt_guardat_cancel = (Button) guardat_setpwd.findViewById(R.id.bt_guardat_cancel);
        builder.setView(guardat_setpwd);
        //把控件加入中后创建一个对话框并弹出来
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        bt_guardat_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String setpwd = et_guardat_setpwd.getText().toString();
                String confirmpwd = et_guardat_confirmpwd.getText().toString();
                if(!setpwd.isEmpty()&&!confirmpwd.isEmpty()){
                    if(setpwd.equals(confirmpwd)){
                        editor.putString("GuardAgainstThief", Md5Utils.encrypt(setpwd));
                        editor.commit();
                        alertDialog.dismiss();
                    }else {
                        Toast.makeText(Home.this,"两次密码输入不一致",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Home.this,"输入不得为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_guardat_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }

    public static Myapplication getMyapplication(){
        return myapplication;
    }

}
