package com.zhangmh.whatmobilemanager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    public void vediReplaceString() throws Exception {
        String s="湖北省武汉省市";
        s=s.replace('中',' ');
        System.out.println(s);
        assertEquals(s, "湖北省武汉省市");
    }
}