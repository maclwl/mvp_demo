package com.taidii.diibot.module.arc_face.faceserver;

public class CompareResult {
    private int id;
    private String type;
    private String userName;
    private float similar;
    private int trackId;

    public CompareResult(String userName, float similar) {
        this.userName = userName;
        this.similar = similar;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
