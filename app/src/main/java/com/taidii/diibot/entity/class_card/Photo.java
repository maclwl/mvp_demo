package com.taidii.diibot.entity.class_card;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;
    private long photoId;
    private String photo;
    private String thumb;
    private String title;
    private String location;
    private String comments;
    private String caption;
    private String takendate;
    private boolean isChecked;
    private String approval;
//    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<String> albums;
    private String photo_url;
    private String student_avatar;
    private String student_name;
    private long id;
    private List<Integer> student_ids = new ArrayList<>();

    public List<Integer> getStudentIds() {
        return student_ids;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.student_ids = studentIds;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public Photo() {
    }

    public String getStudent_avatar() {
        return student_avatar;
    }

    public void setStudent_avatar(String student_avatar) {
        this.student_avatar = student_avatar;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public Photo(String photo, String thumb) {
        this.photo = photo;
        this.thumb = thumb;
    }

    public long getPhotoId() {
        return photoId;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTakendate() {
        return takendate;
    }

    public void setTakendate(String takendate) {
        this.takendate = takendate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }


    public ArrayList<String> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<String> albums) {
        this.albums = albums;
    }

    private Photo(Parcel source) {
        photoId = source.readLong();
        photo = source.readString();
        thumb = source.readString();
        location = source.readString();
        comments = source.readString();
        caption = source.readString();
        takendate = source.readString();
        approval = source.readString();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }

        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }
    };

    public static Creator<Photo> getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(photoId);
        dest.writeString(photo);
        dest.writeString(thumb);
        dest.writeString(location);
        dest.writeString(comments);
        dest.writeString(caption);
        dest.writeString(takendate);
        dest.writeString(approval);
    }


}
