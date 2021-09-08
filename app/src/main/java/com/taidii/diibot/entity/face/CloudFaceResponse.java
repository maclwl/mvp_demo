package com.taidii.diibot.entity.face;

import java.io.Serializable;
import java.util.List;

public class CloudFaceResponse implements Serializable {

    private int status;
    private String message;
    private List<FaceDataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FaceDataBean> getData() {
        return data;
    }

    public void setData(List<FaceDataBean> data) {
        this.data = data;
    }
}
