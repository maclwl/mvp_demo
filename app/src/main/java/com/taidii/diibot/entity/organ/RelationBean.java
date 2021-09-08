package com.taidii.diibot.entity.organ;

import java.io.Serializable;

public class RelationBean implements Serializable {

    private int studentId;
    private String relation;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

}
