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

    public Appinfo() {
    }

    public Appinfo(String label, Drawable icon, boolean isSDcard, boolean isSystem) {
        this.label = label;
        this.icon = icon;
        this.isSDcard = isSDcard;
        this.isSystem = isSystem;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean isSDcard() {
        return isSDcard;
    }

    public void setIsSDcard(boolean isSDcard) {
        this.isSDcard = isSDcard;
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

    @Override
    public String toString() {
        return "Appinfo{" +
                "label='" + label + '\'' +
                ", icon=" + icon +
                ", isSDcard=" + isSDcard +
                ", isSystem=" + isSystem +
                '}';
    }
}
