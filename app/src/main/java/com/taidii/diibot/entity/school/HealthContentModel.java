package com.taidii.diibot.entity.school;

public class HealthContentModel {

    private String name;
    private boolean isCheck;
    private String detail;

    public HealthContentModel(String name, boolean isCheck,String detail) {
        this.name = name;
        this.isCheck = isCheck;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
