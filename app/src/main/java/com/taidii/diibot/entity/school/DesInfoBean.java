package com.taidii.diibot.entity.school;

import java.io.Serializable;

public class DesInfoBean implements Serializable {

    private boolean mandatory_health_check;
    private String des_key;
    private String des_iv;

    public boolean isMandatory_health_check() {
        return mandatory_health_check;
    }

    public void setMandatory_health_check(boolean mandatory_health_check) {
        this.mandatory_health_check = mandatory_health_check;
    }

    public String getDes_key() {
        return des_key;
    }

    public void setDes_key(String des_key) {
        this.des_key = des_key;
    }

    public String getDes_iv() {
        return des_iv;
    }

    public void setDes_iv(String des_iv) {
        this.des_iv = des_iv;
    }
}
