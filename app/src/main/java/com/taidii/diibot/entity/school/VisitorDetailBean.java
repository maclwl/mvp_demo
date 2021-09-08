package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class VisitorDetailBean implements Serializable {

    private String center_name;
    private int center_id;
    private String sign_in;
    private String reason;
    private String date;
    private String ic;
    private int id;
    private String temperature;
    private String sign_out;
    private String name;
    private int gender;
    private String contact;
    private String avatar;
    private String country;
    private CovidInfoBean covid_info;

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public String getSign_in() {
        return sign_in;
    }

    public void setSign_in(String sign_in) {
        this.sign_in = sign_in;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CovidInfoBean getCovid_info() {
        return covid_info;
    }

    public void setCovid_info(CovidInfoBean covid_info) {
        this.covid_info = covid_info;
    }
}
