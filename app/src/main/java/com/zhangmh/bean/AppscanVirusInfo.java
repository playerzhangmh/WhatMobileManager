package com.zhangmh.bean;

/**
 * Created by coins on 2016/4/4.
 */
public class AppscanVirusInfo {
    String location;
    boolean isVirus;
    String packagename;

    public AppscanVirusInfo() {
    }

    public AppscanVirusInfo(String location, boolean isVirus, String packagename) {
        this.location = location;
        this.isVirus = isVirus;
        this.packagename = packagename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVirus() {
        return isVirus;
    }

    public void setIsVirus(boolean isVirus) {
        this.isVirus = isVirus;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
}
