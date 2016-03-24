package com.zhangmh.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by coins on 2016/3/24.
 */
public class HttpUtils {

    public static String getTextFromInputStream(InputStream is){
        String result="";
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int len=-1;
        byte[] b=new byte[1024];
        try {
            while((len=is.read(b))!=-1){
                baos.write(b,0,len);
            }
            baos.close();
            result = baos.toString();
            baos=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
