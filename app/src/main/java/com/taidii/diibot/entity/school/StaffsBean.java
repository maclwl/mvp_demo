package com.taidii.diibot.entity.school;

import com.taidii.diibot.entity.enums.SingTypeEnum;

import java.io.Serializable;

public class StaffsBean implements Serializable {

    private boolean isSign;//是否考情
    private boolean hasSignIn = false;//是否有签入记录
    private SingTypeEnum signType;
    private boolean isRegisterFace;//是否注册人脸
    private String faceImage;//人脸图片
    private String name;
    private int gender;
    private int touch_able;
    private String rfid;
    private String avatar;
    private int staffId;
    private String ic;

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

    public SingTypeEnum getSignType() {
        return signType;
    }

    public void setSignType(SingTypeEnum signType) {
        this.signType = signType;
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
