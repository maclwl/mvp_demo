package com.taidii.diibot.entity.class_card;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ClassCardWeekPlanBean {


    /**
     * status : 1
     * data : [{"week":17,"date_from":"2021-06-21","image_list":["https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-1.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-2.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-3.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-4.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-5.jpg"],"created_by":"min zhao","date_to":"2021-06-27"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * week : 17
         * date_from : 2021-06-21
         * image_list : ["https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-1.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-2.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-3.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-4.jpg","https://image.taidii.cn/to_pdf/c28f7dd8-66bc-490b-8e83-7e77a300d785.pdf-5.jpg"]
         * created_by : min zhao
         * date_to : 2021-06-27
         */

        private int week;
        private String date_from;
        private String created_by;
        private String date_to;
        private List<String> image_list = new ArrayList<>();

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public String getDate_from() {
            return date_from;
        }

        public void setDate_from(String date_from) {
            this.date_from = date_from;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getDate_to() {
            return date_to;
        }

        public void setDate_to(String date_to) {
            this.date_to = date_to;
        }

        public List<String> getImage_list() {
            return image_list;
        }

        public void setImage_list(List<String> image_list) {
            this.image_list = image_list;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.week);
            dest.writeString(this.date_from);
            dest.writeString(this.created_by);
            dest.writeString(this.date_to);
            dest.writeStringList(this.image_list);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.week = in.readInt();
            this.date_from = in.readString();
            this.created_by = in.readString();
            this.date_to = in.readString();
            this.image_list = in.createStringArrayList();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
