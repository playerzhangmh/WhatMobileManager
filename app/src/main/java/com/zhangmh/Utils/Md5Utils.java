package com.zhangmh.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestException;
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

    public static String getApkMd5(String apklocation){
        String apkMd5="";
        File apkfile=new File(apklocation);
        FileInputStream fis=null;
        byte[] digest=null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            fis=new FileInputStream(apkfile);
            byte[] b=new byte[1024];
            int len=-1;
            while ((len=fis.read(b))!=-1){
                md5.digest(b,0,len);
            }
            digest = md5.digest();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DigestException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(digest!=null){
            StringBuffer result=new StringBuffer();
            for (byte b:digest){
                int ret=b&0xff;
                String ret_s=Integer.toHexString(ret);
                if(ret_s.length()==1){
                    result.append("0");
                }
                result.append(ret_s);
            }
            apkMd5=result.toString();
        }

        return apkMd5;

    }

}
