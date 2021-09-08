package com.taidii.diibot.entity.organ;

import java.io.Serializable;
import java.util.List;

public class ClassOrderBean implements Serializable {

    private String roomname;
    private String coursename;
    private String classname;
    private String klass_schedule;
    private String klass_schedule_cn;
    private int id;
    private List<String> time_list;

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getKlass_schedule() {
        return klass_schedule;
    }

    public void setKlass_schedule(String klass_schedule) {
        this.klass_schedule = klass_schedule;
    }

    public String getKlass_schedule_cn() {
        return klass_schedule_cn;
    }

    public void setKlass_schedule_cn(String klass_schedule_cn) {
        this.klass_schedule_cn = klass_schedule_cn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTime_list() {
        return time_list;
    }

    public void setTime_list(List<String> time_list) {
        this.time_list = time_list;
    }

}
