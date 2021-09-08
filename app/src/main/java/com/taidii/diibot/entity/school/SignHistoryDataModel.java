package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class SignHistoryDataModel implements Serializable {

    private AttendancesBean attendances;
    private GuardiansBean guardians;

    public AttendancesBean getAttendances() {
        return attendances;
    }

    public void setAttendances(AttendancesBean attendances) {
        this.attendances = attendances;
    }

    public GuardiansBean getGuardians() {
        return guardians;
    }

    public void setGuardians(GuardiansBean guardians) {
        this.guardians = guardians;
    }
}
