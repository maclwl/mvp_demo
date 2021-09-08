package com.taidii.diibot.entity.face;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeacherUser implements Serializable {

    public String type;
    public int id;
    public String avatar;
    public String name;
    public String center_name;
    public String center_city;
    public String email;
    public String first_name;
    public String last_name;
    public String username;
    public boolean ngportfolio;
    public int center_id;
    public int face_recognition_enabled;
    public String facepp_api_secret;
    public String facepp_api_key;
    @SerializedName(value = "center_type")
    public int centerType;
    public String hq_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public String getCenter_city() {
        return center_city;
    }

    public void setCenter_city(String center_city) {
        this.center_city = center_city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isNgportfolio() {
        return ngportfolio;
    }

    public void setNgportfolio(boolean ngportfolio) {
        this.ngportfolio = ngportfolio;
    }

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public int getFace_recognition_enabled() {
        return face_recognition_enabled;
    }

    public void setFace_recognition_enabled(int face_recognition_enabled) {
        this.face_recognition_enabled = face_recognition_enabled;
    }

    public String getFacepp_api_secret() {
        return facepp_api_secret;
    }

    public void setFacepp_api_secret(String facepp_api_secret) {
        this.facepp_api_secret = facepp_api_secret;
    }

    public String getFacepp_api_key() {
        return facepp_api_key;
    }

    public void setFacepp_api_key(String facepp_api_key) {
        this.facepp_api_key = facepp_api_key;
    }

    public int getCenterType() {
        return centerType;
    }

    public void setCenterType(int centerType) {
        this.centerType = centerType;
    }

    public String getHq_id() {
        return hq_id;
    }

    public void setHq_id(String hq_id) {
        this.hq_id = hq_id;
    }
}
