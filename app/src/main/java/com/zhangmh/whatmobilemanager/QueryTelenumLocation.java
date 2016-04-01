package com.zhangmh.whatmobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Dao.QueryTeleNumLocationDao;

public class QueryTelenumLocation extends ActionBarActivity {

    private EditText ev_querytelenumlocation_inputNum;
    private TextView tv_querytelenumlocation_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_telenum_location);
        //隐藏actionbar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        ev_querytelenumlocation_inputNum = (EditText) findViewById(R.id.ev_querytelenumlocation_inputNum);
        ev_querytelenumlocation_inputNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String teleNumLocation = QueryTeleNumLocationDao.getTeleNumLocation(QueryTelenumLocation.this, s.toString());
                if(!teleNumLocation.equals("数据库数据不足")){
                    tv_querytelenumlocation_location.setText(teleNumLocation);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_querytelenumlocation_location = (TextView) findViewById(R.id.tv_querytelenumlocation_location);
    }
    public void query(View v){
        String telenum=ev_querytelenumlocation_inputNum.getText().toString();
        if(!telenum.isEmpty()){
            String teleNumLocation = QueryTeleNumLocationDao.getTeleNumLocation(this, telenum);
            if(!teleNumLocation.isEmpty()){
                if(teleNumLocation.equals("数据库数据不足")){
                    //弹一个对话框，询问是否需要在线获取
                    new AlertDialog.Builder(this)
                            .setTitle("暂无数据")
                            .setMessage("是否需要在线去获取归属地信息")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(QueryTelenumLocation.this,"服务器有问题",Toast.LENGTH_LONG).show();
                                    //在线获取代码
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }else {
                    tv_querytelenumlocation_location.setText(teleNumLocation);
                }
            }else {
                Toast.makeText(this,"请输入合法的号码",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,"输入不得为空",Toast.LENGTH_LONG).show();
        }
    }
}
