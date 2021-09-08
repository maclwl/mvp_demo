package com.taidii.diibot.entity.organ;

import java.io.Serializable;

public class StaffsBean implements Serializable {

    private String name;
    private int gender;
    private int touch_able;
    private String rfid;
    private String avatar;
    private int staffId;
    private String ic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getTouch_able() {
        return touch_able;
    }

    public void setTouch_able(int touch_able) {
        this.touch_able = touch_able;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

}
