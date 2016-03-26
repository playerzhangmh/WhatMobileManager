package com.zhangmh.view;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangmh.application.Myapplication;
import com.zhangmh.whatmobilemanager.R;
import com.zhangmh.whatmobilemanager.Setting;

/**
 * Created by coins on 2016/3/25.
 */
public class SettingItems extends RelativeLayout implements View.OnClickListener{
    Setting mSetting;
    private Myapplication application;
    private SharedPreferences setting_sp;
    private SharedPreferences.Editor edit;
    private TextView tv_setting_title;
    private TextView tv_setting_state;
    private CheckBox cb_setting_ischecked;
    private String itemtitle;
    private String onString;
    private String offString;
    private String spKeyname;

    public SettingItems(Context context) {
        super(context);

        init(null);

    }

    public SettingItems(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs){

        mSetting=  Setting.getmActivity();
        application = (Myapplication) mSetting.getApplication();
        setting_sp = application.getSetting_sp();
        View inflate = View.inflate(getContext(), R.layout.setting_item_content, null);
        tv_setting_title = (TextView) inflate.findViewById(R.id.tv_setting_title);
        tv_setting_state = (TextView) inflate.findViewById(R.id.tv_setting_state);
        cb_setting_ischecked = (CheckBox) inflate.findViewById(R.id.cb_setting_ischecked);


        edit = setting_sp.edit();
        if(attrs!=null){
            itemtitle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "itemtitle");
            onString = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "onString");
            offString = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "offString");
            spKeyname = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "spKeyname");
            boolean checked = setting_sp.getBoolean(spKeyname, true);
            tv_setting_title.setText(itemtitle);
            if(checked){
                tv_setting_state.setText(onString);
                cb_setting_ischecked.setChecked(true);
            }else {
                tv_setting_state.setText(offString);
                cb_setting_ischecked.setChecked(false);
            }
            addView(inflate);
          //  View activity_setting=View.inflate(getContext(), R.layout.activity_setting, null);
         //    LinearLayout viewById = (LinearLayout) activity_setting.findViewById(R.id.zanshi);
          //  viewById.addView(this);
            setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        boolean checked = cb_setting_ischecked.isChecked();
        Log.v("hw2",checked+"onClick");
        if(checked){
            Log.v("hw2",checked+"t");
            cb_setting_ischecked.setChecked(false);
            tv_setting_state.setText(offString);
            edit.putBoolean(spKeyname, false);
            edit.commit();
        }else {
            Log.v("hw2",checked+"f");
            cb_setting_ischecked.setChecked(true);
            tv_setting_state.setText(onString);
            edit.putBoolean(spKeyname, true);
            edit.commit();
        }

    }

}
