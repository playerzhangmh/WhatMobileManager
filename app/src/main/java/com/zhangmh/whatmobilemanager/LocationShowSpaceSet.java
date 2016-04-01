package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangmh.Utils.WindowMeasureUtils;
import com.zhangmh.application.Myapplication;

public class LocationShowSpaceSet extends Activity {

    private TextView tv_locationSspace_numlocation;
    private SharedPreferences.Editor edit;
    private int windowwide;
    private int windowheight;
    private SharedPreferences setting_sp;
    private LinearLayout linearLayout;



    public static LocationShowSpaceSet mLocationShowSpaceSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationShowSpaceSet=this;
        setContentView(R.layout.activity_location_show_space_set);
        Myapplication application = (Myapplication) getApplication();
        setting_sp = application.getSetting_sp();
        edit = setting_sp.edit();
        int[] windowMeasure = WindowMeasureUtils.getWindowMeasure(this);
        windowwide = windowMeasure[0];
        windowheight = windowMeasure[1];
        Log.v("hw2",windowwide+"--"+windowheight+"LocationShowSpaceSet");

        tv_locationSspace_numlocation = new TextView(this);
        tv_locationSspace_numlocation.setText("what is up");
        tv_locationSspace_numlocation.setTextColor(Color.RED);
        tv_locationSspace_numlocation.setTextSize(25);
        tv_locationSspace_numlocation.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.GRAY);
        linearLayout.addView(tv_locationSspace_numlocation, textParams);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.gravity=Gravity.LEFT|Gravity.TOP;
        layoutParams.gravity=Gravity.LEFT|Gravity.TOP;
        addContentView(linearLayout,layoutParams);
        Log.v("hw2", linearLayout.getWidth() + "--" + linearLayout.getHeight() + "LocationShowSpaceSet");
        linearLayout.setOnTouchListener(onTouchListener);
        linearLayout.setOnClickListener(onClickListener);

    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        float startX;
        float endX;
        float startY;
        float endY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    Log.v("hw2","ACTION_UP");
                    edit.putInt("locationViewSpaceX",(int)linearLayout.getLeft());
                    edit.putInt("locationViewSpaceY",(int)linearLayout.getTop());
                    edit.commit();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.v("hw2","ACTION_MOVE");
                    endX = event.getRawX();
                    endY=event.getRawY();
                    float dx=endX-startX;
                    float dy=endY-startY;
                    if((linearLayout.getLeft()+dx)<0||(linearLayout.getLeft()+linearLayout.getWidth()+dx)>windowwide||(linearLayout.getTop()+dy)<WindowMeasureUtils.getStatusBarHeight(LocationShowSpaceSet.this)||(dy+linearLayout.getTop()+linearLayout.getHeight())>(windowheight)){
                        break;
                    }
                    linearLayout.layout((int)(linearLayout.getLeft()+dx),(int)(linearLayout.getTop()+dy),(int)(linearLayout.getLeft()+linearLayout.getWidth()+dx),(int)(dy+linearLayout.getTop()+linearLayout.getHeight()));
                    Log.v("hw2", linearLayout.getHeight()+"ACTION_MOVE"+linearLayout.getWidth());

                    startX=endX;
                    startY=endY;
                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.v("hw2","ACTION_DOWN");
                    startX=event.getRawX();
                    startY=event.getRawY();
                    break;
            }
            return false;
        }
    };
    View.OnClickListener onClickListener=new View.OnClickListener() {
        long firstclicktime;
        boolean firstclick=true;
        @Override
        public void onClick(View v) {

            if(firstclick){
                firstclicktime=System.currentTimeMillis();
                firstclick=false;
            }else {
                if((System.currentTimeMillis()-firstclicktime)<500){
                    //为双击事件
                    int l=(windowwide-linearLayout.getWidth())/2;
                    int t=(windowheight-linearLayout.getHeight())/2;
                    int r=l+linearLayout.getWidth();
                    int b=t+linearLayout.getHeight();
                    Log.v("hw2",l+"onClick"+t+"onClick"+linearLayout.getWidth()+"onClick"+linearLayout.getHeight());
                    linearLayout.layout(l,t,r,b);
                    edit.putInt("locationViewSpaceX", l);
                    edit.putInt("locationViewSpaceY",t);
                    edit.commit();
                    firstclick=true;
                }
                else {
                    firstclick=true;
                }
            }


        }
    };
    public static LocationShowSpaceSet getmLocationShowSpaceSet() {
        return mLocationShowSpaceSet;
    }

}
