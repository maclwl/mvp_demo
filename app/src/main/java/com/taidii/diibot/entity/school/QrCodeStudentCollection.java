package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class QrCodeStudentCollection implements Serializable {

    private MainSchoolContentModel contentModel;
    private SchoolMainEnum type;

    public MainSchoolContentModel getContentModel() {
        return contentModel;
    }

    public void setContentModel(MainSchoolContentModel contentModel) {
        this.contentModel = contentModel;
    }

    public SchoolMainEnum getType() {
        return type;
    }

    public void setType(SchoolMainEnum type) {
        this.type = type;
    }
}
