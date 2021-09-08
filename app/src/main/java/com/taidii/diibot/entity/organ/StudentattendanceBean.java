package com.taidii.diibot.entity.organ;

import java.io.Serializable;

public class StudentattendanceBean implements Serializable {

    private boolean uploaded;
    private int studentId;
    private String weight;
    private String recorded_on;
    private String authorizedPickUpId;
    private int studentklass_id;
    private String others;
    private String remarks;
    private int klassschedule_id;
    private String pick_name;
    private int id;
    private int is_enrollmented;
    private String temperature;
    private String dateTime;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRecorded_on() {
        return recorded_on;
    }

    public void setRecorded_on(String recorded_on) {
        this.recorded_on = recorded_on;
    }

    public String getAuthorizedPickUpId() {
        return authorizedPickUpId;
    }

    public void setAuthorizedPickUpId(String authorizedPickUpId) {
        this.authorizedPickUpId = authorizedPickUpId;
    }

    public int getStudentklass_id() {
        return studentklass_id;
    }

    public void setStudentklass_id(int studentklass_id) {
        this.studentklass_id = studentklass_id;
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

    public int getKlassschedule_id() {
        return klassschedule_id;
    }

    public void setKlassschedule_id(int klassschedule_id) {
        this.klassschedule_id = klassschedule_id;
    }

    public String getPick_name() {
        return pick_name;
    }

    public void setPick_name(String pick_name) {
        this.pick_name = pick_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_enrollmented() {
        return is_enrollmented;
    }

    public void setIs_enrollmented(int is_enrollmented) {
        this.is_enrollmented = is_enrollmented;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
