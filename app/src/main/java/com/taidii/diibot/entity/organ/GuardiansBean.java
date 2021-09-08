package com.taidii.diibot.entity.organ;

import java.io.Serializable;
import java.util.List;

public class GuardiansBean implements Serializable {

    private String rfid;
    private String contactNo;
    private String ic;
    private int id;
    private String homephoneNo;
    private String name;
    private int gender;
    private String officephoneNo;
    private String avatar;
    private String email;
    private List<RelationBean> relation;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHomephoneNo() {
        return homephoneNo;
    }

    public void setHomephoneNo(String homephoneNo) {
        this.homephoneNo = homephoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getOfficephoneNo() {
        return officephoneNo;
    }

    public void setOfficephoneNo(String officephoneNo) {
        this.officephoneNo = officephoneNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RelationBean> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationBean> relation) {
        this.relation = relation;
    }

}
