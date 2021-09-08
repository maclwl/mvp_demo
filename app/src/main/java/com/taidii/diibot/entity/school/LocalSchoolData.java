package com.taidii.diibot.entity.school;

import java.io.Serializable;
import java.util.List;

public class LocalSchoolData implements Serializable {

    private int statusCode;
    private boolean mandatory_health_check;
    private int center_id;
    private boolean available_student;
    private boolean covid_safe_access;
    private String center_name;
    private String datetime;
    private String setting_password;
    private boolean available_staff;
    private String center_logo;
    private List<StudentattendanceBean> studentattendance;
    private List<BusesBean> buses;
    private List<StaffattendanceBean> staffattendance;
    private List<StudentsBean> students;
    private List<GuardiansBean> guardians;
    private List<String> classOrder;
    private List<StaffsBean> staffs;
    private List<?> busattendance;
    private List<GuardiansBean> authorizedPickups;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isMandatory_health_check() {
        return mandatory_health_check;
    }

    public void setMandatory_health_check(boolean mandatory_health_check) {
        this.mandatory_health_check = mandatory_health_check;
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

    public boolean isCovid_safe_access() {
        return covid_safe_access;
    }

    public void setCovid_safe_access(boolean covid_safe_access) {
        this.covid_safe_access = covid_safe_access;
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

    public boolean isAvailable_staff() {
        return available_staff;
    }

    public void setAvailable_staff(boolean available_staff) {
        this.available_staff = available_staff;
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

    public List<BusesBean> getBuses() {
        return buses;
    }

    public void setBuses(List<BusesBean> buses) {
        this.buses = buses;
    }

    public List<StaffattendanceBean> getStaffattendance() {
        return staffattendance;
    }

    public void setStaffattendance(List<StaffattendanceBean> staffattendance) {
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

    public List<String> getClassOrder() {
        return classOrder;
    }

    public void setClassOrder(List<String> classOrder) {
        this.classOrder = classOrder;
    }

    public List<StaffsBean> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffsBean> staffs) {
        this.staffs = staffs;
    }

    public List<?> getBusattendance() {
        return busattendance;
    }

    public void setBusattendance(List<?> busattendance) {
        this.busattendance = busattendance;
    }

    public List<GuardiansBean> getAuthorizedPickups() {
        return authorizedPickups;
    }

    public void setAuthorizedPickups(List<GuardiansBean> authorizedPickups) {
        this.authorizedPickups = authorizedPickups;
    }
}
