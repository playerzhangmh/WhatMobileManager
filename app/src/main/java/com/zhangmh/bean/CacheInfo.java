package com.zhangmh.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by coins on 2016/4/4.
 */
public class CacheInfo {
    String label;
    Drawable icon;
    String cache;
    String packagename;

    public CacheInfo(String label, Drawable icon, String cache, String packagename) {
        this.label = label;
        this.icon = icon;
        this.cache = cache;
        this.packagename = packagename;
    }

    public CacheInfo() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    @Override
    public String toString() {
        return "CacheInfo{" +
                "label='" + label + '\'' +
                ", icon=" + icon +
                ", cache='" + cache + '\'' +
                ", packagename='" + packagename + '\'' +
                '}';
    }

}
