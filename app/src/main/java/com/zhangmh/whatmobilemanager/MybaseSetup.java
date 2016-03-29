package com.zhangmh.whatmobilemanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by coins on 2016/3/29.
 */
public abstract class MybaseSetup extends Activity {

    private int widthPixels;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new MygestureDetector());
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        widthPixels = displayMetrics.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MygestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v("hw2", "MygestureDetector--onFling");
            float startx = e1.getX();
            float starty = e1.getY();
            float endx = e2.getX();
            float endy = e2.getY();
            if((startx-endx)>=(widthPixels/5)){
                //左滑
                next(null);

            }else if((endx-startx)>=(widthPixels/5)){
                //右滑
                pre(null);

            }
            return super.onFling(e1, e2, velocityX, velocityY);

        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.v("hw2", "MygestureDetector--onLongPress");

        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v("hw2", "MygestureDetector--onDoubleTap");

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v("hw2", "MygestureDetector--onDown");

            return super.onDown(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
            Log.v("hw2", "MygestureDetector--onShowPress");

        }
    }

    public abstract void pre(View v);
    public abstract void next(View v);
}
