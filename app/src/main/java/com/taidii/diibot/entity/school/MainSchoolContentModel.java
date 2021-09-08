package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class MainSchoolContentModel implements Serializable {

    private StudentsBean studentsBean;
    private StaffsBean staffsBean;

    public StudentsBean getStudentsBean() {
        return studentsBean;
    }

    public void setStudentsBean(StudentsBean studentsBean) {
        this.studentsBean = studentsBean;
    }

    public StaffsBean getStaffsBean() {
        return staffsBean;
    }

    public void setStaffsBean(StaffsBean staffsBean) {
        this.staffsBean = staffsBean;
    }
}
