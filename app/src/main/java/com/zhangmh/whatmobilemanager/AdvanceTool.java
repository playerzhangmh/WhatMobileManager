package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class AdvanceTool extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_tool);
    }

    public void queryPhoneLocation(View v){
        startActivity(new Intent(this,QueryTelenumLocation.class));
    }
}
