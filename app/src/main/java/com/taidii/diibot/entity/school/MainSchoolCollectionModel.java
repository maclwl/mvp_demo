package com.taidii.diibot.entity.school;

import java.io.Serializable;
import java.util.List;

public class MainSchoolCollectionModel implements Serializable {

    private boolean isSpread;//是否展开
    private String modelName;
    private int classId;
    private SchoolMainEnum type;
    private List<MainSchoolContentModel> mainSchoolContentModelList;
    private List<MainSchoolContentModel> mainSchoolContentModelListHide;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public boolean isSpread() {
        return isSpread;
    }

    public void setSpread(boolean spread) {
        isSpread = spread;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public SchoolMainEnum getType() {
        return type;
    }

    public void setType(SchoolMainEnum type) {
        this.type = type;
    }

    public List<MainSchoolContentModel> getMainSchoolContentModelList() {
        return mainSchoolContentModelList;
    }

    public void setMainSchoolContentModelList(List<MainSchoolContentModel> mainSchoolContentModelList) {
        this.mainSchoolContentModelList = mainSchoolContentModelList;
    }

    public List<MainSchoolContentModel> getMainSchoolContentModelListHide() {
        return mainSchoolContentModelListHide;
    }

    public void setMainSchoolContentModelListHide(List<MainSchoolContentModel> mainSchoolContentModelListHide) {
        this.mainSchoolContentModelListHide = mainSchoolContentModelListHide;
    }
}
