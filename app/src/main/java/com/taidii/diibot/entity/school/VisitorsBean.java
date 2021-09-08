package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class VisitorsBean implements Serializable {

    private String sign_out;
    private String name;
    private String sign_in;
    private String avatar;
    private String date;
    private int id;

    public String getSign_out() {
        return sign_out;
    }

    public void setSign_out(String sign_out) {
        this.sign_out = sign_out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign_in() {
        return sign_in;
    }

    public void setSign_in(String sign_in) {
        this.sign_in = sign_in;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
