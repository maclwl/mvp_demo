package com.taidii.diibot.entity.enums;

public enum CodeDecryptType {

    TEACHER(0), PARENT(1),STUDENT(2);

    private int type;

    CodeDecryptType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
