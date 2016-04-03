package com.zhangmh.whatmobilemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

    public void testLabel(){//验证当在string的xml文件中，不写label，应用label为空而不是null
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getContext().getPackageManager().getApplicationInfo("com.zhangmh.whatmobilemanager", 0);
            CharSequence applicationLabel = getContext().getPackageManager().getApplicationLabel(applicationInfo);
            Log.v("hw2",applicationLabel.toString()+"aaaaaaaa");
            assertEquals(true,applicationLabel.toString().isEmpty());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
