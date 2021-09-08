package com.taidii.diibot.entity.school;

import java.io.Serializable;
import java.util.List;

public class HealthCheckResponse implements Serializable {

    private int statusCode;
    private List<Integer> cloudId;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Integer> getCloudId() {
        return cloudId;
    }

    public void setCloudId(List<Integer> cloudId) {
        this.cloudId = cloudId;
    }
}
