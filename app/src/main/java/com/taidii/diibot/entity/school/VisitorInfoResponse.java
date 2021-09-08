package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class VisitorInfoResponse implements Serializable {

    private VisitorDetailBean detail;
    private int success;

    public VisitorDetailBean getDetail() {
        return detail;
    }

    public void setDetail(VisitorDetailBean detail) {
        this.detail = detail;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
