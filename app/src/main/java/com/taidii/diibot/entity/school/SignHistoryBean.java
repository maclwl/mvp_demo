package com.taidii.diibot.entity.school;

import java.io.Serializable;
import java.util.List;

public class SignHistoryBean implements Serializable {

    private int statusCode;
    private List<?> pickups;
    private List<GuardiansBean> guardians;
    private List<AttendancesBean> attendances;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<?> getPickups() {
        return pickups;
    }

    public void setPickups(List<?> pickups) {
        this.pickups = pickups;
    }

    public List<GuardiansBean> getGuardians() {
        return guardians;
    }

    public void setGuardians(List<GuardiansBean> guardians) {
        this.guardians = guardians;
    }

    public List<AttendancesBean> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendancesBean> attendances) {
        this.attendances = attendances;
    }

}
