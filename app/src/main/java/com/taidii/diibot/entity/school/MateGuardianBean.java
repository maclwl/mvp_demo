package com.taidii.diibot.entity.school;

public class MateGuardianBean {

    private boolean isCheck;
    private int id;
    private String name;
    private int gender;
    private String avatar;
    private String relation;
    private boolean isRegisterFace;//是否注册人脸
    private String faceImage;//人脸图片

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
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
}
