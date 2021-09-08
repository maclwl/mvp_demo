package com.taidii.diibot.entity.face;

import java.io.Serializable;
import java.util.List;

public class UserBean implements Serializable {

    private String username;
    private String email;
    private Object user_type;
    private String avatar;
    private String fullname;
    private Object guardian;
    private int pk;
    private boolean active;
    private CenterBean center;
    private List<?> children;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getUser_type() {
        return user_type;
    }

    public void setUser_type(Object user_type) {
        this.user_type = user_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Object getGuardian() {
        return guardian;
    }

    public void setGuardian(Object guardian) {
        this.guardian = guardian;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CenterBean getCenter() {
        return center;
    }

    public void setCenter(CenterBean center) {
        this.center = center;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }
}
