package com.zhangmh.Utils;

import android.os.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by coins on 2016/3/26.
 */
public class Md5Utils {

    public static String encrypt(String password){
        String encryptPwd="";
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(password.getBytes());
            StringBuffer sb=new StringBuffer();
            for (byte b:digest){
                int ret=b&0xff;
                String ret_s=Integer.toHexString(ret);
                if(ret_s.length()==1){
                    sb.append("0");
                }
                sb.append(ret_s);
            }
            encryptPwd=sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
 return encryptPwd;
    }
}
