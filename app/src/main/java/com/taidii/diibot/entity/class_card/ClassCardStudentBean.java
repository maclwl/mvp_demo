package com.taidii.diibot.entity.class_card;

import java.util.List;

public class ClassCardStudentBean {


    /**
     * count : 4
     * next : null
     * previous : null
     * results : [{"id":9,"title":"测试3","add_time":"2020-09-17 10:42:50","images":[]},{"id":7,"title":"测试1","add_time":"2020-09-17 10:40:58","images":[]},{"id":11,"title":"测试5","add_time":"2020-09-17 10:56:22","images":[]},{"id":8,"title":"测试2","add_time":"2020-09-17 10:41:14","images":[]}]
     */

    private int count;
    private List<ResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 9
         * title : 测试3
         * add_time : 2020-09-17 10:42:50
         * images : []
         */

        private int id;
        private String title;
        private String add_time;
        private String file_url;
        private List<String> images;
        private boolean read_ornot;

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public boolean isRead_ornot() {
            return read_ornot;
        }

        public void setRead_ornot(boolean read_ornot) {
            this.read_ornot = read_ornot;
        }
    }
}
