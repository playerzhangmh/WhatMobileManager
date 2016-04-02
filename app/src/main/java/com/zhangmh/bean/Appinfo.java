package com.zhangmh.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by coins on 2016/3/31.
 */
public class Appinfo {
    String label;
    Drawable icon;
    boolean isSDcard;
    boolean isSystem;
    String packagename;

    public Appinfo() {
    }

    public Appinfo(String label, Drawable icon, boolean isSDcard, boolean isSystem, String packagename) {
        this.label = label;
        this.icon = icon;
        this.isSDcard = isSDcard;
        this.isSystem = isSystem;
        this.packagename = packagename;
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

    public boolean isSDcard() {
        return isSDcard;
    }

    public void setIsSDcard(boolean isSDcard) {
        this.isSDcard = isSDcard;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    @Override
    public String toString() {
        return "Appinfo{" +
                "label='" + label + '\'' +
                ", icon=" + icon +
                ", isSDcard=" + isSDcard +
                ", isSystem=" + isSystem +
                ", packagename='" + packagename + '\'' +
                '}';
    }
}
