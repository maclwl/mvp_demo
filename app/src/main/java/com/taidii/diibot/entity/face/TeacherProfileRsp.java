package com.taidii.diibot.entity.face;

import java.io.Serializable;

public class TeacherProfileRsp implements Serializable {

    private TeacherUser user;

    public TeacherUser getUser() {
        return user;
    }

    public void setUser(TeacherUser user) {
        this.user = user;
    }

}
