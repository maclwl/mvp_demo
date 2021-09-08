package com.taidii.diibot.entity.organ;

import java.io.Serializable;
import java.util.List;

public class OrganSignResponse implements Serializable {


    private DataBean data;
    private int statusCode;
    private List<Integer> cloudId;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Integer> getCloudId() {
        return cloudId;
    }

    public void setCloudId(List<Integer> cloudId) {
        this.cloudId = cloudId;
    }

    public static class DataBean {

        private String temperature;
        private String recorded_on;
        private int number_of_class;
        private int studentklass_id;
        private int balance_of_class;
        private int id;
        private int present;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getRecorded_on() {
            return recorded_on;
        }

        public void setRecorded_on(String recorded_on) {
            this.recorded_on = recorded_on;
        }

        public int getNumber_of_class() {
            return number_of_class;
        }

        public void setNumber_of_class(int number_of_class) {
            this.number_of_class = number_of_class;
        }

        public int getStudentklass_id() {
            return studentklass_id;
        }

        public void setStudentklass_id(int studentklass_id) {
            this.studentklass_id = studentklass_id;
        }

        public int getBalance_of_class() {
            return balance_of_class;
        }

        public void setBalance_of_class(int balance_of_class) {
            this.balance_of_class = balance_of_class;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPresent() {
            return present;
        }

        public void setPresent(int present) {
            this.present = present;
        }
    }
}
