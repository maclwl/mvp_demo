package com.taidii.diibot.entity.school;

import com.taidii.diibot.entity.enums.CodeDecryptType;

public class QrCodeDecryptBean {

    private int id_num;
    private int user_type;

    public int getId_num() {
        return id_num;
    }

    public void setId_num(int id_num) {
        this.id_num = id_num;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public CodeDecryptType getType(){
        CodeDecryptType type = CodeDecryptType.STUDENT;
        switch (user_type){
            case 0:
                type = CodeDecryptType.TEACHER;
                break;
            case 1:
                type = CodeDecryptType.PARENT;
                break;
            case 2:
                type = CodeDecryptType.STUDENT;
                break;
        }
        return type;
    }

}
