package com.taidii.diibot.entity.enums;

public enum SingTypeEnum {

    SIGN_IN(0), SIGN_TEMP(4),SIGN_OUT(1);

    private int type;

    SingTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
