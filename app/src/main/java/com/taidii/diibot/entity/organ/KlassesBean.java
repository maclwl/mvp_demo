package com.taidii.diibot.entity.organ;

import java.io.Serializable;

public class KlassesBean implements Serializable {

    private int is_secondary_card;
    private String recorded_on;
    private int is_try_class;
    private int number_of_class;
    private String studentklass_id;
    private String date;
    private int klassschedule_id;
    private int present;
    private String temperature;
    private String class_time;
    private String klassschedule_klassname;
    private int balance_of_class;
    private int student_class_bag_id;

    public int getIs_secondary_card() {
        return is_secondary_card;
    }

    public void setIs_secondary_card(int is_secondary_card) {
        this.is_secondary_card = is_secondary_card;
    }

    public String getRecorded_on() {
        return recorded_on;
    }

    public void setRecorded_on(String recorded_on) {
        this.recorded_on = recorded_on;
    }

    public int getIs_try_class() {
        return is_try_class;
    }

    public void setIs_try_class(int is_try_class) {
        this.is_try_class = is_try_class;
    }

    public int getNumber_of_class() {
        return number_of_class;
    }

    public void setNumber_of_class(int number_of_class) {
        this.number_of_class = number_of_class;
    }

    public String getStudentklass_id() {
        return studentklass_id;
    }

    public void setStudentklass_id(String studentklass_id) {
        this.studentklass_id = studentklass_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getKlassschedule_id() {
        return klassschedule_id;
    }

    public void setKlassschedule_id(int klassschedule_id) {
        this.klassschedule_id = klassschedule_id;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getClass_time() {
        return class_time;
    }

    public void setClass_time(String class_time) {
        this.class_time = class_time;
    }

    public String getKlassschedule_klassname() {
        return klassschedule_klassname;
    }

    public void setKlassschedule_klassname(String klassschedule_klassname) {
        this.klassschedule_klassname = klassschedule_klassname;
    }

    public int getBalance_of_class() {
        return balance_of_class;
    }

    public void setBalance_of_class(int balance_of_class) {
        this.balance_of_class = balance_of_class;
    }

    public int getStudent_class_bag_id() {
        return student_class_bag_id;
    }

    public void setStudent_class_bag_id(int student_class_bag_id) {
        this.student_class_bag_id = student_class_bag_id;
    }
}
