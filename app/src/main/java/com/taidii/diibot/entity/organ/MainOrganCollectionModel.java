package com.taidii.diibot.entity.organ;

import java.util.List;

public class MainOrganCollectionModel {

    private boolean isSpread;//是否展开
    private ClassOrderBean classOrder;
    private List<StudentsBean> studentsList;

    public boolean isSpread() {
        return isSpread;
    }

    public void setSpread(boolean spread) {
        isSpread = spread;
    }

    public ClassOrderBean getClassOrder() {
        return classOrder;
    }

    public void setClassOrder(ClassOrderBean classOrder) {
        this.classOrder = classOrder;
    }

    public List<StudentsBean> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<StudentsBean> studentsList) {
        this.studentsList = studentsList;
    }
}
