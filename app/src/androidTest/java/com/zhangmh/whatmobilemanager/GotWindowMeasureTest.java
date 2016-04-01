package com.zhangmh.whatmobilemanager;

import android.test.AndroidTestCase;
import android.util.Log;

import com.zhangmh.Utils.WindowMeasureUtils;

/**
 * Created by coins on 2016/3/30.
 */
public class GotWindowMeasureTest extends AndroidTestCase {

    public void testgetMeasures(){
        int[] windowMeasure = WindowMeasureUtils.getWindowMeasure(getContext());
        Log.v("hw2",windowMeasure[1]+"---"+windowMeasure[0]);
        Log.i("hw2","ddddddddd");

        assertEquals(-1, windowMeasure[0]);
        assertEquals(600,windowMeasure[1]);
    }
    public void testStatusHeight(){
        int statusBarHeight = WindowMeasureUtils.getStatusBarHeight(getContext());
        Log.v("hw2",statusBarHeight+"dddddddff");
    }

    public void testTitleHeight(){
        int titleHeight = WindowMeasureUtils.getTitleBarHeight(getContext(),LocationShowSpaceSet.getmLocationShowSpaceSet());
        Log.v("hw2",titleHeight+"dddddddff");
    }

}
