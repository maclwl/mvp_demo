package com.taidii.diibot.entity.face;

import com.arcsoft.face.FaceInfo;

public class FaceDetailInfo {

    private int id;
    private String name;
    private String uri;
    private FaceInfo faceInfo;
    private byte[] nv21;
    private byte[] bgr24;
    private int width;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public byte[] getNv21() {
        return nv21;
    }

    public void setNv21(byte[] nv21) {
        this.nv21 = nv21;
    }

    public byte[] getBgr24() {
        return bgr24;
    }

    public void setBgr24(byte[] bgr24) {
        this.bgr24 = bgr24;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
