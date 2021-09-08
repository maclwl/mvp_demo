package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class AttendancesBean implements Serializable {

    private boolean uploaded;
    private int studentId;
    private double weight;
    private int authorizedPickUpId;
    private int busDriverId;
    private String others;
    private String remarks;
    private String commonD;
    private int id;
    private int busId;
    private double temperature;
    private int cloudId;
    private String dateTime;
    private int pickUpType;
    private int type;

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAuthorizedPickUpId() {
        return authorizedPickUpId;
    }

    public void setAuthorizedPickUpId(int authorizedPickUpId) {
        this.authorizedPickUpId = authorizedPickUpId;
    }

    public int getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(int busDriverId) {
        this.busDriverId = busDriverId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCommonD() {
        return commonD;
    }

    public void setCommonD(String commonD) {
        this.commonD = commonD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getCloudId() {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(int pickUpType) {
        this.pickUpType = pickUpType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
