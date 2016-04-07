package com.zhangmh.whatmobilemanager;

import android.test.AndroidTestCase;
import android.util.Log;

import com.zhangmh.Dao.BlacknumDbDao;

import java.util.List;

/**
 * Created by coins on 2016/4/5.
 */
public class BlackNUmTest extends AndroidTestCase {

    public void testDelete(){
        BlacknumDbDao dao=new BlacknumDbDao(getContext());
        int deleteblack = dao.deleteblack("1234");
        Log.v("hw11",deleteblack+"test");
    }

    public void testInsert(){
        BlacknumDbDao dao=new BlacknumDbDao(getContext());
        for(int i=0;i<200;i++){
            if(i<10){
                dao.insertblacknum(1,"1008"+i);
            }else if(i<100){
                dao.insertblacknum(2,"100"+i);
            }else{
                dao.insertblacknum(2,"10"+i);
            }
        }
    }

    public void  testgetPartlist(){
        BlacknumDbDao dao=new BlacknumDbDao(getContext());
        List<String> partblacklist = dao.getPartblacklist(20, 10);
        Log.v("testgetPartlist",partblacklist.toString());

    }
}
