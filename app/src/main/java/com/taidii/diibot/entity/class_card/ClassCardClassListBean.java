package com.taidii.diibot.entity.class_card;

import java.util.List;

public class ClassCardClassListBean {


    /**
     * status : 1
     * data : [{"id":8510,"name":"2020班级"},{"id":8774,"name":"心心大班"},{"id":8779,"name":"美美中班"},{"id":8803,"name":"小1班"},{"id":8824,"name":"贝贝一班"},{"id":8837,"name":"12"},{"id":8920,"name":"腾飞小升中班"},{"id":8925,"name":"大一班"},{"id":8942,"name":"托班新111"},{"id":8957,"name":"中班"},{"id":8958,"name":"小一班"},{"id":8965,"name":"中华小班"}]
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

    public static class DataBean {
        /**
         * id : 8510
         * name : 2020班级
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
