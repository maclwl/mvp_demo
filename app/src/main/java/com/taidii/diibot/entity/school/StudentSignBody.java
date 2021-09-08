package com.taidii.diibot.entity.school;

public class StudentSignBody {

    private int studentid;
    private String date;
    private int pickupid;
    private float temperature;
    private int pickuptype;
    private float weight;
    private float height;
    private int type;
    private int busDriverId;
    private int busId;
    private String remarks;
    private String common;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPickupid() {
        return pickupid;
    }

    public void setPickupid(int pickupid) {
        this.pickupid = pickupid;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getPickuptype() {
        return pickuptype;
    }

    public void setPickuptype(int pickuptype) {
        this.pickuptype = pickuptype;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(int busDriverId) {
        this.busDriverId = busDriverId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
