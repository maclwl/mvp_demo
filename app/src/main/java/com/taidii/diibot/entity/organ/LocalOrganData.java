package com.taidii.diibot.entity.organ;

import java.io.Serializable;
import java.util.List;

public class LocalOrganData implements Serializable {

    private int statusCode;
    private boolean available_staff;
    private int center_id;
    private boolean available_student;
    private String center_name;
    private String datetime;
    private String setting_password;
    private String center_logo;
    private List<StudentattendanceBean> studentattendance;
    private List<?> staffattendance;
    private List<StudentsBean> students;
    private List<GuardiansBean> guardians;
    private List<ClassOrderBean> classOrder;
    private List<StaffsBean> staffs;
    private List<?> authorizedPickups;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isAvailable_staff() {
        return available_staff;
    }

    public void setAvailable_staff(boolean available_staff) {
        this.available_staff = available_staff;
    }

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public boolean isAvailable_student() {
        return available_student;
    }

    public void setAvailable_student(boolean available_student) {
        this.available_student = available_student;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSetting_password() {
        return setting_password;
    }

    public void setSetting_password(String setting_password) {
        this.setting_password = setting_password;
    }

    public String getCenter_logo() {
        return center_logo;
    }

    public void setCenter_logo(String center_logo) {
        this.center_logo = center_logo;
    }

    public List<StudentattendanceBean> getStudentattendance() {
        return studentattendance;
    }

    public void setStudentattendance(List<StudentattendanceBean> studentattendance) {
        this.studentattendance = studentattendance;
    }

    public List<?> getStaffattendance() {
        return staffattendance;
    }

    public void setStaffattendance(List<?> staffattendance) {
        this.staffattendance = staffattendance;
    }

    public List<StudentsBean> getStudents() {
        return students;
    }

    public void setStudents(List<StudentsBean> students) {
        this.students = students;
    }

    public List<GuardiansBean> getGuardians() {
        return guardians;
    }

    public void setGuardians(List<GuardiansBean> guardians) {
        this.guardians = guardians;
    }

    public List<ClassOrderBean> getClassOrder() {
        return classOrder;
    }

    public void setClassOrder(List<ClassOrderBean> classOrder) {
        this.classOrder = classOrder;
    }

    public List<StaffsBean> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffsBean> staffs) {
        this.staffs = staffs;
    }

    public List<?> getAuthorizedPickups() {
        return authorizedPickups;
    }

    public void setAuthorizedPickups(List<?> authorizedPickups) {
        this.authorizedPickups = authorizedPickups;
    }
}
