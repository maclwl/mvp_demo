package com.taidii.diibot.entity.organ;

public class OrganStudentSignBody {

    private int studentid;
    private int studentklass_id;
    private float temperature;
    private int pickupid;
    private int is_secondary_card;
    private int klassschedule_id;
    private int student_class_bag_id;
    private String class_time;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getStudentklass_id() {
        return studentklass_id;
    }

    public void setStudentklass_id(int studentklass_id) {
        this.studentklass_id = studentklass_id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getPickupid() {
        return pickupid;
    }

    public void setPickupid(int pickupid) {
        this.pickupid = pickupid;
    }

    public int getIs_secondary_card() {
        return is_secondary_card;
    }

    public void setIs_secondary_card(int is_secondary_card) {
        this.is_secondary_card = is_secondary_card;
    }

    public int getKlassschedule_id() {
        return klassschedule_id;
    }

    public void setKlassschedule_id(int klassschedule_id) {
        this.klassschedule_id = klassschedule_id;
    }

    public int getStudent_class_bag_id() {
        return student_class_bag_id;
    }

    public void setStudent_class_bag_id(int student_class_bag_id) {
        this.student_class_bag_id = student_class_bag_id;
    }

    public String getClass_time() {
        return class_time;
    }

    public void setClass_time(String class_time) {
        this.class_time = class_time;
    }
}
