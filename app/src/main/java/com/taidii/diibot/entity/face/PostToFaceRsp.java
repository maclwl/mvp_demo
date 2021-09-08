package com.taidii.diibot.entity.face;

public class PostToFaceRsp {

    public int status;
    public int klass_id;
    public String face_recognition_image;
    public String face_image_upload_date;
    public String facepp_faceset_token;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKlass_id() {
        return klass_id;
    }

    public void setKlass_id(int klass_id) {
        this.klass_id = klass_id;
    }

    public String getFace_recognition_image() {
        return face_recognition_image;
    }

    public void setFace_recognition_image(String face_recognition_image) {
        this.face_recognition_image = face_recognition_image;
    }

    public String getFace_image_upload_date() {
        return face_image_upload_date;
    }

    public void setFace_image_upload_date(String face_image_upload_date) {
        this.face_image_upload_date = face_image_upload_date;
    }

    public String getFacepp_faceset_token() {
        return facepp_faceset_token;
    }

    public void setFacepp_faceset_token(String facepp_faceset_token) {
        this.facepp_faceset_token = facepp_faceset_token;
    }
}
