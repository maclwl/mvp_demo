package com.taidii.diibot.entity.organ;

import java.io.Serializable;

public class OrganDetailBean implements Serializable {

    private OrganDetailData data;
    private int statusCode;

    public OrganDetailData getData() {
        return data;
    }

    public void setData(OrganDetailData data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
