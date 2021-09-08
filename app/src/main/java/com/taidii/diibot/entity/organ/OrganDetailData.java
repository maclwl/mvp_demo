package com.taidii.diibot.entity.organ;

import java.io.Serializable;
import java.util.List;

public class OrganDetailData implements Serializable {
    private String touch_password;
    private int touch_able;
    private String rfid;
    private String groupname;
    private String emergencyContactNo;
    private String ic;
    private String name;
    private int gender;
    private int centerStudentId;
    private String avatar;
    private int is_test_child;
    private List<KlassesBean> klasses;

    public String getTouch_password() {
        return touch_password;
    }

    public void setTouch_password(String touch_password) {
        this.touch_password = touch_password;
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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

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

    public int getCenterStudentId() {
        return centerStudentId;
    }

    public void setCenterStudentId(int centerStudentId) {
        this.centerStudentId = centerStudentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_test_child() {
        return is_test_child;
    }

    public void setIs_test_child(int is_test_child) {
        this.is_test_child = is_test_child;
    }

    public List<KlassesBean> getKlasses() {
        return klasses;
    }

    public void setKlasses(List<KlassesBean> klasses) {
        this.klasses = klasses;
    }

}
