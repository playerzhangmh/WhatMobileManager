package com.zhangmh.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.zhangmh.Dao.BlacknumDbDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InterceptCallservice extends Service {

    private BlacknumDbDao dao;

    public InterceptCallservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlacknumDbDao(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }
    PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(TelephonyManager.CALL_STATE_RINGING==state){
                int blacknumMode = dao.isBlacknum(incomingNumber);
                Log.v("hw12",blacknumMode+"incomingNumber");
                if(blacknumMode==2||blacknumMode==3){
                    //拦截电话
                    interceptCall();
                }
            }
        }
    };

    //endCall
    public void interceptCall(){
        try {
            Class<?> aClass = getClassLoader().loadClass("android.os.ServiceManager");
            Method getService = aClass.getMethod("getService", String.class);
            IBinder invoke = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
            boolean b = iTelephony.endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
