package com.taidii.diibot.entity.face;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("face_register_info")
public class FaceRegisterInfo {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private byte[] featureData;
    private String name;
    private String faceImage;
    private String type;

    public FaceRegisterInfo(byte[] faceFeature, String name, String faceImage) {
        this.featureData = faceFeature;
        this.name = name;
        this.faceImage = faceImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
