package com.zhangmh.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by coins on 2016/4/2.
 */
public class ProcessInfo {
    String packagename;
    String label;
    boolean isSystem;
    Drawable drawable;
    String ram;
    boolean isChecked;

    public ProcessInfo() {
    }

    public ProcessInfo(String packagename, String label, boolean isSystem, Drawable drawable, String ram, boolean isChecked) {
        this.packagename = packagename;
        this.label = label;
        this.isSystem = isSystem;
        this.drawable = drawable;
        this.ram = ram;
        this.isChecked = isChecked;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "packagename='" + packagename + '\'' +
                ", label='" + label + '\'' +
                ", isSystem=" + isSystem +
                ", drawable=" + drawable +
                ", ram='" + ram + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
