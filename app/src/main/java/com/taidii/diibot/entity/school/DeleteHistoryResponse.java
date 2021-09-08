package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class DeleteHistoryResponse implements Serializable {

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
