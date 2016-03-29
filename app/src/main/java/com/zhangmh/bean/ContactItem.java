package com.zhangmh.bean;

/**
 * Created by coins on 2016/3/28.
 */
public class ContactItem {
    private String name;
    private String teleNum;

    public ContactItem() {
    }

    public ContactItem(String name, String teleNum) {
        this.name = name;
        this.teleNum = teleNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeleNum() {
        return teleNum;
    }

    public void setTeleNum(String teleNum) {
        this.teleNum = teleNum;
    }

    @Override
    public String toString() {
        return "ContactItem{" +
                "name='" + name + '\'' +
                ", teleNum='" + teleNum + '\'' +
                '}';
    }
}
