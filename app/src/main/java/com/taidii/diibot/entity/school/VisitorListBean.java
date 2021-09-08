package com.taidii.diibot.entity.school;

import java.io.Serializable;
import java.util.List;

public class VisitorListBean implements Serializable {

    private int success;
    private List<VisitorsBean> visitors;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<VisitorsBean> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<VisitorsBean> visitors) {
        this.visitors = visitors;
    }

}
