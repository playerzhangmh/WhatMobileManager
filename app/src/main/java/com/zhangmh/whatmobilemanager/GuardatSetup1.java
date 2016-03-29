package com.zhangmh.whatmobilemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class GuardatSetup1 extends MybaseSetup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardat_setup1);
    }

    @Override
    public void pre(View v) {

    }

    public void next(View v){
        startActivity(new Intent(this,GuardatSetup2.class));
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        finish();
    }
}
