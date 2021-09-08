package com.taidii.diibot.entity.school;

import com.taidii.diibot.entity.enums.SingTypeEnum;

import java.io.Serializable;

public class StudentsBean implements Serializable {

    private boolean isSign;//是否签到
    private boolean hasSignIn = false;//是否有签入记录
    private int busId;//校车ID
    private boolean isHealthCheck;//是否体测
    private SingTypeEnum singType;//签到类型
    private boolean isRegisterFace;//是否注册人脸
    private String faceImage;//人脸图片
    private int classid;
    private String touch_password;
    private int touch_able;
    private String rfid;
    private String classname;
    private String groupname;
    private String emergencyContactNo;
    private String ic;
    private String name;
    private int gender;
    private int centerStudentId;
    private String avatar;
    private int studentCareType;
    private String levelname;

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public boolean isHasSignIn() {
        return hasSignIn;
    }

    public void setHasSignIn(boolean hasSignIn) {
        this.hasSignIn = hasSignIn;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public boolean isHealthCheck() {
        return isHealthCheck;
    }

    public void setHealthCheck(boolean healthCheck) {
        isHealthCheck = healthCheck;
    }

    public SingTypeEnum getSingType() {
        return singType;
    }

    public void setSingType(SingTypeEnum singType) {
        this.singType = singType;
    }

    public boolean isRegisterFace() {
        return isRegisterFace;
    }

    public void setRegisterFace(boolean registerFace) {
        isRegisterFace = registerFace;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
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

    public int getStudentCareType() {
        return studentCareType;
    }

    public void setStudentCareType(int studentCareType) {
        this.studentCareType = studentCareType;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }
}
