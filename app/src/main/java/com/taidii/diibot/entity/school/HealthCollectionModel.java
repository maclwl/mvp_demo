package com.taidii.diibot.entity.school;

import java.util.List;

public class HealthCollectionModel {

    private String key;//部位
    private List<HealthContentModel> contentModelList;//疾病所有集合

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<HealthContentModel> getContentModelList() {
        return contentModelList;
    }

    public void setContentModelList(List<HealthContentModel> contentModelList) {
        this.contentModelList = contentModelList;
    }
}
