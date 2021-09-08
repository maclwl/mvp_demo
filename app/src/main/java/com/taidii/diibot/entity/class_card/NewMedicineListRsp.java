package com.taidii.diibot.entity.class_card;

import java.util.List;

public class NewMedicineListRsp {

    /**
     * status : 1
     * klass_list : [{"status":0,"request_time":"2021-06-10T10:08:13Z","parent_note":"","fullname":"周波","avatar":"","requests":[{"request_time":"2021-06-10T10:08:13Z","total_times":1,"dosage":1,"medicine_name":"今天","medicine_type":0,"measure_type":0,"complete_times":0,"id":73}],"deny_reason":""}]
     */

    private int status;
    private List<KlassListBean> klass_list;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<KlassListBean> getKlass_list() {
        return klass_list;
    }

    public void setKlass_list(List<KlassListBean> klass_list) {
        this.klass_list = klass_list;
    }

    public static class KlassListBean {
        /**
         * status : 0
         * request_time : 2021-06-10T10:08:13Z
         * parent_note :
         * fullname : 周波
         * avatar :
         * requests : [{"request_time":"2021-06-10T10:08:13Z","total_times":1,"dosage":1,"medicine_name":"今天","medicine_type":0,"measure_type":0,"complete_times":0,"id":73}]
         * deny_reason :
         */

        private int status;
        private int batch_id;
        private String request_time;
        private String parent_note;
        private String fullname;
        private String avatar;
        private String deny_reason;
        private List<RequestsBean> requests;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getBatch_id() {
            return batch_id;
        }

        public void setBatch_id(int batch_id) {
            this.batch_id = batch_id;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }

        public String getParent_note() {
            return parent_note;
        }

        public void setParent_note(String parent_note) {
            this.parent_note = parent_note;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDeny_reason() {
            return deny_reason;
        }

        public void setDeny_reason(String deny_reason) {
            this.deny_reason = deny_reason;
        }

        public List<RequestsBean> getRequests() {
            return requests;
        }

        public void setRequests(List<RequestsBean> requests) {
            this.requests = requests;
        }

        public static class RequestsBean {
            /**
             * request_time : 2021-06-10T10:08:13Z
             * total_times : 1
             * dosage : 1
             * medicine_name : 今天
             * medicine_type : 0
             * measure_type : 0
             * complete_times : 0
             * id : 73
             */

            private String request_time;
            private int total_times;
            private int dosage;
            private String medicine_name;
            private int medicine_type;
            private int measure_type;
            private int complete_times;
            private int id;
            private int student_id;

            public String getRequest_time() {
                return request_time;
            }

            public void setRequest_time(String request_time) {
                this.request_time = request_time;
            }

            public int getTotal_times() {
                return total_times;
            }

            public void setTotal_times(int total_times) {
                this.total_times = total_times;
            }

            public int getDosage() {
                return dosage;
            }

            public void setDosage(int dosage) {
                this.dosage = dosage;
            }

            public String getMedicine_name() {
                return medicine_name;
            }

            public void setMedicine_name(String medicine_name) {
                this.medicine_name = medicine_name;
            }

            public int getMedicine_type() {
                return medicine_type;
            }

            public void setMedicine_type(int medicine_type) {
                this.medicine_type = medicine_type;
            }

            public int getMeasure_type() {
                return measure_type;
            }

            public void setMeasure_type(int measure_type) {
                this.measure_type = measure_type;
            }

            public int getComplete_times() {
                return complete_times;
            }

            public void setComplete_times(int complete_times) {
                this.complete_times = complete_times;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
            }
        }
    }
}
