package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class CenterTypeBean implements Serializable {

    private int status;
    private int center_type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCenter_type() {
        return center_type;
    }

    public void setCenter_type(int center_type) {
        this.center_type = center_type;
    }
}
