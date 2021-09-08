package com.taidii.diibot.entity.class_card;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ClassCardStudentListBean implements Parcelable{

    /**
     * status : 1
     * data : {"personal_num":0,"teacher_infos":[{"teacher_id":10928,"phone":"","user_id":91909,"name":"韩","gender":1,"honors":[],"employment_date":"2020-18-05","avatar":""},{"teacher_id":5022,"phone":"","user_id":29049,"name":"李老师","gender":1,"honors":[],"employment_date":"2020-18-05","avatar":"https://image.taidii.cn/avatar/teacher/1467700928249.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":10937,"phone":"","user_id":91943,"name":"kh老师1112","gender":1,"honors":[],"employment_date":"2020-01-01","avatar":""},{"teacher_id":10944,"phone":"","user_id":91963,"name":"522老师","gender":1,"honors":[],"employment_date":"2020-21-05","avatar":""},{"teacher_id":10948,"phone":"","user_id":91973,"name":"pingguo","gender":1,"honors":[],"employment_date":"2019-07-06","avatar":""},{"teacher_id":10952,"phone":"","user_id":91981,"name":"都是","gender":1,"honors":[],"employment_date":"2020-01-06","avatar":""},{"teacher_id":10890,"phone":"sadad","user_id":91859,"name":"chenshuai xiao pengyou wqweeqeq dajkshdas 19901122","gender":1,"honors":[],"employment_date":"2001-03-02","avatar":""},{"teacher_id":11003,"phone":"","user_id":92228,"name":"车厘子","gender":1,"honors":[],"employment_date":"2014-02-07","avatar":""},{"teacher_id":11243,"phone":"","user_id":93197,"name":"zz","gender":1,"honors":[],"employment_date":"2015-01-09","avatar":""},{"teacher_id":11257,"phone":"","user_id":93245,"name":"测试老师","gender":1,"honors":[],"employment_date":"2015-01-09","avatar":"https://image.taidii.cn/avatar/teacher/1600950022274_bb2b151d-e214-4690-aaee-3fd9e5cceb8c.png?imageView2/5/w/150/h/150"},{"teacher_id":10983,"phone":"18915410342","user_id":92013,"name":"xx123","gender":0,"honors":[],"employment_date":"2020-20-06","avatar":""},{"teacher_id":11272,"phone":"","user_id":93302,"name":"kkhh123","gender":1,"honors":[],"employment_date":"2020-01-12","avatar":"https://image.taidii.cn/avatar/teacher/1608281694142_65194c67-e9ae-453e-a310-a024e10aadbe.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11274,"phone":"","user_id":93304,"name":"123老师","gender":1,"honors":[],"employment_date":"2020-01-12","avatar":""},{"teacher_id":11281,"phone":"","user_id":93328,"name":"灰太狼老师","gender":0,"honors":[],"employment_date":"2003-01-01","avatar":""},{"teacher_id":11287,"phone":"","user_id":93348,"name":"Tina2","gender":1,"honors":[],"employment_date":"2014-03-02","avatar":"https://image.taidii.cn/avatar/teacher/1614068658285_a3ca8fae-9651-433b-b0a6-5300942fb03e.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11288,"phone":"","user_id":93350,"name":"sandy2","gender":1,"honors":[],"employment_date":"2013-01-02","avatar":"https://image.taidii.cn/avatar/teacher/1402988773081.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":2834,"phone":"","user_id":6265,"name":"cindy","gender":1,"honors":[],"employment_date":"2020-17-06","avatar":"https://image.taidii.cn/avatar/teacher/1615778338646_326ce5ae-61b9-4d7a-9d2e-e91d33253731.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11291,"phone":"","user_id":93402,"name":"财务01","gender":1,"honors":[],"employment_date":"2021-01-04","avatar":""},{"teacher_id":11292,"phone":"","user_id":93403,"name":"财务02","gender":1,"honors":[],"employment_date":"2021-01-04","avatar":""},{"teacher_id":10990,"phone":"","user_id":92117,"name":"安安老师","gender":1,"honors":[],"employment_date":"2014-01-07","avatar":"https://image.taidii.cn/avatar/teacher/1594347888239_30ae2f74-cef9-4031-a8d1-7ed72df00799.jpeg?imageView2/5/w/150/h/150"}],"student_infos":[{"status":0,"id":98004,"avatar":"","name":"Mandy"},{"status":0,"id":98013,"avatar":"https://image.taidii.cn/avatar/student/1620290564367_5757163c-d537-4697-b898-81e98cb8d839.jpeg?imageView2/5/w/150/h/150","name":"丁健2"},{"status":0,"id":98023,"avatar":"","name":"乔磊1号"},{"status":0,"id":98001,"avatar":"","name":"乔阳"},{"status":0,"id":99004,"avatar":"https://image.taidii.cn/avatar/student/1608258345398_994baeda-3da4-44d3-9fbf-afc75a35a674.png?imageView2/5/w/150/h/150","name":"今天"},{"status":0,"id":98015,"avatar":"","name":"侍婷婷"},{"status":0,"id":98028,"avatar":"https://image.taidii.cn/avatar/student/1621583736895_8573d410-1d76-4713-a01e-3c70e02db67f.jpeg?imageView2/5/w/150/h/150","name":"俞杰"},{"status":0,"id":98008,"avatar":"","name":"刘佳"},{"status":0,"id":98003,"avatar":"","name":"周水琳"},{"status":0,"id":98027,"avatar":"","name":"周波"},{"status":0,"id":97749,"avatar":"","name":"墨小会"},{"status":0,"id":98018,"avatar":"https://image.taidii.cn/avatar/student/1607503987535_3ebd57ff-f883-45fc-b05d-51c0660edbef.jpeg?imageView2/5/w/150/h/150","name":"孔晖"},{"status":0,"id":98035,"avatar":"","name":"孟娜"},{"status":0,"id":98014,"avatar":"","name":"张阳"},{"status":0,"id":98031,"avatar":"","name":"徐文琪"},{"status":0,"id":99001,"avatar":"","name":"汤老师"},{"status":0,"id":99024,"avatar":"","name":"洗刷刷"},{"status":0,"id":98029,"avatar":"","name":"牛良"},{"status":0,"id":98997,"avatar":"https://image.taidii.cn/avatar/student/1608545110876_ef410a15-5cb1-4e0a-a70c-eeda7e3105d0.jpeg?imageView2/5/w/150/h/150","name":"王斌"},{"status":0,"id":99000,"avatar":"","name":"王璐瑶"},{"status":0,"id":99104,"avatar":"","name":"王近洋"},{"status":0,"id":98025,"avatar":"https://image.taidii.cn/avatar/student/1601301612760_e75e94a9-046f-4b13-b95a-e5c1361dc484.png?imageView2/5/w/150/h/150","name":"田丰辉"},{"status":0,"id":98024,"avatar":"","name":"瞿俊"},{"status":0,"id":98020,"avatar":"","name":"肖潜"},{"status":0,"id":98998,"avatar":"","name":"蒋圆圆"},{"status":0,"id":98011,"avatar":"https://image.taidii.cn/avatar/student/1608625862344_bedf71b9-35ca-4ffe-9546-3b4f753666fa.jpeg?imageView2/5/w/150/h/150","name":"蔡佳玉"},{"status":0,"id":98034,"avatar":"","name":"赵卓越"},{"status":0,"id":98007,"avatar":"","name":"郝人杰"},{"status":0,"id":98999,"avatar":"","name":"郭成鹏"},{"status":0,"id":98010,"avatar":"","name":"闫倩"},{"status":0,"id":98036,"avatar":"","name":"陈帅"},{"status":0,"id":98021,"avatar":"","name":"陈洋"},{"status":0,"id":98026,"avatar":"","name":"韩毅"},{"status":0,"id":98030,"avatar":"","name":"韩韧"},{"status":0,"id":98017,"avatar":"https://image.taidii.cn/avatar/student/1601018930991_2490211e-d2db-4482-9492-67e075b27930.jpeg?imageView2/5/w/150/h/150","name":"颜丹丹"},{"status":0,"id":98019,"avatar":"","name":"颜明刚"},{"status":0,"id":98022,"avatar":"","name":"马传龙"},{"status":0,"id":98012,"avatar":"https://image.taidii.cn/avatar/student/1608626081349_f6dcdb38-b881-4ee5-88c4-669e39a235a1.jpeg?imageView2/5/w/150/h/150","name":"麻彦平"},{"status":0,"id":98032,"avatar":"","name":"黄雪静"}],"sign_out_num":0,"sign_in_num":0,"sick_num":0}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable{
        /**
         * personal_num : 0
         * teacher_infos : [{"teacher_id":10928,"phone":"","user_id":91909,"name":"韩","gender":1,"honors":[],"employment_date":"2020-18-05","avatar":""},{"teacher_id":5022,"phone":"","user_id":29049,"name":"李老师","gender":1,"honors":[],"employment_date":"2020-18-05","avatar":"https://image.taidii.cn/avatar/teacher/1467700928249.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":10937,"phone":"","user_id":91943,"name":"kh老师1112","gender":1,"honors":[],"employment_date":"2020-01-01","avatar":""},{"teacher_id":10944,"phone":"","user_id":91963,"name":"522老师","gender":1,"honors":[],"employment_date":"2020-21-05","avatar":""},{"teacher_id":10948,"phone":"","user_id":91973,"name":"pingguo","gender":1,"honors":[],"employment_date":"2019-07-06","avatar":""},{"teacher_id":10952,"phone":"","user_id":91981,"name":"都是","gender":1,"honors":[],"employment_date":"2020-01-06","avatar":""},{"teacher_id":10890,"phone":"sadad","user_id":91859,"name":"chenshuai xiao pengyou wqweeqeq dajkshdas 19901122","gender":1,"honors":[],"employment_date":"2001-03-02","avatar":""},{"teacher_id":11003,"phone":"","user_id":92228,"name":"车厘子","gender":1,"honors":[],"employment_date":"2014-02-07","avatar":""},{"teacher_id":11243,"phone":"","user_id":93197,"name":"zz","gender":1,"honors":[],"employment_date":"2015-01-09","avatar":""},{"teacher_id":11257,"phone":"","user_id":93245,"name":"测试老师","gender":1,"honors":[],"employment_date":"2015-01-09","avatar":"https://image.taidii.cn/avatar/teacher/1600950022274_bb2b151d-e214-4690-aaee-3fd9e5cceb8c.png?imageView2/5/w/150/h/150"},{"teacher_id":10983,"phone":"18915410342","user_id":92013,"name":"xx123","gender":0,"honors":[],"employment_date":"2020-20-06","avatar":""},{"teacher_id":11272,"phone":"","user_id":93302,"name":"kkhh123","gender":1,"honors":[],"employment_date":"2020-01-12","avatar":"https://image.taidii.cn/avatar/teacher/1608281694142_65194c67-e9ae-453e-a310-a024e10aadbe.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11274,"phone":"","user_id":93304,"name":"123老师","gender":1,"honors":[],"employment_date":"2020-01-12","avatar":""},{"teacher_id":11281,"phone":"","user_id":93328,"name":"灰太狼老师","gender":0,"honors":[],"employment_date":"2003-01-01","avatar":""},{"teacher_id":11287,"phone":"","user_id":93348,"name":"Tina2","gender":1,"honors":[],"employment_date":"2014-03-02","avatar":"https://image.taidii.cn/avatar/teacher/1614068658285_a3ca8fae-9651-433b-b0a6-5300942fb03e.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11288,"phone":"","user_id":93350,"name":"sandy2","gender":1,"honors":[],"employment_date":"2013-01-02","avatar":"https://image.taidii.cn/avatar/teacher/1402988773081.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":2834,"phone":"","user_id":6265,"name":"cindy","gender":1,"honors":[],"employment_date":"2020-17-06","avatar":"https://image.taidii.cn/avatar/teacher/1615778338646_326ce5ae-61b9-4d7a-9d2e-e91d33253731.jpeg?imageView2/5/w/150/h/150"},{"teacher_id":11291,"phone":"","user_id":93402,"name":"财务01","gender":1,"honors":[],"employment_date":"2021-01-04","avatar":""},{"teacher_id":11292,"phone":"","user_id":93403,"name":"财务02","gender":1,"honors":[],"employment_date":"2021-01-04","avatar":""},{"teacher_id":10990,"phone":"","user_id":92117,"name":"安安老师","gender":1,"honors":[],"employment_date":"2014-01-07","avatar":"https://image.taidii.cn/avatar/teacher/1594347888239_30ae2f74-cef9-4031-a8d1-7ed72df00799.jpeg?imageView2/5/w/150/h/150"}]
         * student_infos : [{"status":0,"id":98004,"avatar":"","name":"Mandy"},{"status":0,"id":98013,"avatar":"https://image.taidii.cn/avatar/student/1620290564367_5757163c-d537-4697-b898-81e98cb8d839.jpeg?imageView2/5/w/150/h/150","name":"丁健2"},{"status":0,"id":98023,"avatar":"","name":"乔磊1号"},{"status":0,"id":98001,"avatar":"","name":"乔阳"},{"status":0,"id":99004,"avatar":"https://image.taidii.cn/avatar/student/1608258345398_994baeda-3da4-44d3-9fbf-afc75a35a674.png?imageView2/5/w/150/h/150","name":"今天"},{"status":0,"id":98015,"avatar":"","name":"侍婷婷"},{"status":0,"id":98028,"avatar":"https://image.taidii.cn/avatar/student/1621583736895_8573d410-1d76-4713-a01e-3c70e02db67f.jpeg?imageView2/5/w/150/h/150","name":"俞杰"},{"status":0,"id":98008,"avatar":"","name":"刘佳"},{"status":0,"id":98003,"avatar":"","name":"周水琳"},{"status":0,"id":98027,"avatar":"","name":"周波"},{"status":0,"id":97749,"avatar":"","name":"墨小会"},{"status":0,"id":98018,"avatar":"https://image.taidii.cn/avatar/student/1607503987535_3ebd57ff-f883-45fc-b05d-51c0660edbef.jpeg?imageView2/5/w/150/h/150","name":"孔晖"},{"status":0,"id":98035,"avatar":"","name":"孟娜"},{"status":0,"id":98014,"avatar":"","name":"张阳"},{"status":0,"id":98031,"avatar":"","name":"徐文琪"},{"status":0,"id":99001,"avatar":"","name":"汤老师"},{"status":0,"id":99024,"avatar":"","name":"洗刷刷"},{"status":0,"id":98029,"avatar":"","name":"牛良"},{"status":0,"id":98997,"avatar":"https://image.taidii.cn/avatar/student/1608545110876_ef410a15-5cb1-4e0a-a70c-eeda7e3105d0.jpeg?imageView2/5/w/150/h/150","name":"王斌"},{"status":0,"id":99000,"avatar":"","name":"王璐瑶"},{"status":0,"id":99104,"avatar":"","name":"王近洋"},{"status":0,"id":98025,"avatar":"https://image.taidii.cn/avatar/student/1601301612760_e75e94a9-046f-4b13-b95a-e5c1361dc484.png?imageView2/5/w/150/h/150","name":"田丰辉"},{"status":0,"id":98024,"avatar":"","name":"瞿俊"},{"status":0,"id":98020,"avatar":"","name":"肖潜"},{"status":0,"id":98998,"avatar":"","name":"蒋圆圆"},{"status":0,"id":98011,"avatar":"https://image.taidii.cn/avatar/student/1608625862344_bedf71b9-35ca-4ffe-9546-3b4f753666fa.jpeg?imageView2/5/w/150/h/150","name":"蔡佳玉"},{"status":0,"id":98034,"avatar":"","name":"赵卓越"},{"status":0,"id":98007,"avatar":"","name":"郝人杰"},{"status":0,"id":98999,"avatar":"","name":"郭成鹏"},{"status":0,"id":98010,"avatar":"","name":"闫倩"},{"status":0,"id":98036,"avatar":"","name":"陈帅"},{"status":0,"id":98021,"avatar":"","name":"陈洋"},{"status":0,"id":98026,"avatar":"","name":"韩毅"},{"status":0,"id":98030,"avatar":"","name":"韩韧"},{"status":0,"id":98017,"avatar":"https://image.taidii.cn/avatar/student/1601018930991_2490211e-d2db-4482-9492-67e075b27930.jpeg?imageView2/5/w/150/h/150","name":"颜丹丹"},{"status":0,"id":98019,"avatar":"","name":"颜明刚"},{"status":0,"id":98022,"avatar":"","name":"马传龙"},{"status":0,"id":98012,"avatar":"https://image.taidii.cn/avatar/student/1608626081349_f6dcdb38-b881-4ee5-88c4-669e39a235a1.jpeg?imageView2/5/w/150/h/150","name":"麻彦平"},{"status":0,"id":98032,"avatar":"","name":"黄雪静"}]
         * sign_out_num : 0
         * sign_in_num : 0
         * sick_num : 0
         */

        private int personal_num;
        private int sign_out_num;
        private int sign_in_num;
        private int sick_num;
        private List<TeacherInfosBean> teacher_infos;
        private List<StudentInfosBean> student_infos;

        public int getPersonal_num() {
            return personal_num;
        }

        public void setPersonal_num(int personal_num) {
            this.personal_num = personal_num;
        }

        public int getSign_out_num() {
            return sign_out_num;
        }

        public void setSign_out_num(int sign_out_num) {
            this.sign_out_num = sign_out_num;
        }

        public int getSign_in_num() {
            return sign_in_num;
        }

        public void setSign_in_num(int sign_in_num) {
            this.sign_in_num = sign_in_num;
        }

        public int getSick_num() {
            return sick_num;
        }

        public void setSick_num(int sick_num) {
            this.sick_num = sick_num;
        }

        public List<TeacherInfosBean> getTeacher_infos() {
            return teacher_infos;
        }

        public void setTeacher_infos(List<TeacherInfosBean> teacher_infos) {
            this.teacher_infos = teacher_infos;
        }

        public List<StudentInfosBean> getStudent_infos() {
            return student_infos;
        }

        public void setStudent_infos(List<StudentInfosBean> student_infos) {
            this.student_infos = student_infos;
        }

        public static class TeacherInfosBean implements Parcelable {
            /**
             * teacher_id : 10928
             * phone :
             * user_id : 91909
             * name : 韩
             * gender : 1
             * honors : []
             * employment_date : 2020-18-05
             * avatar :
             */

            private int teacher_id;
            private String phone;
            private int user_id;
            private String name;
            private int gender;
            private String employment_date;
            private String avatar;
            private String group;
            private List<String> honors = new ArrayList<>();

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public int getTeacher_id() {
                return teacher_id;
            }

            public void setTeacher_id(int teacher_id) {
                this.teacher_id = teacher_id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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

            public String getEmployment_date() {
                return employment_date;
            }

            public void setEmployment_date(String employment_date) {
                this.employment_date = employment_date;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public List<String> getHonors() {
                return honors;
            }

            public void setHonors(List<String> honors) {
                this.honors = honors;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.teacher_id);
                dest.writeString(this.phone);
                dest.writeInt(this.user_id);
                dest.writeString(this.name);
                dest.writeInt(this.gender);
                dest.writeString(this.employment_date);
                dest.writeString(this.avatar);
                dest.writeStringList(this.honors);
            }

            public TeacherInfosBean() {
            }

            protected TeacherInfosBean(Parcel in) {
                this.teacher_id = in.readInt();
                this.phone = in.readString();
                this.user_id = in.readInt();
                this.name = in.readString();
                this.gender = in.readInt();
                this.employment_date = in.readString();
                this.avatar = in.readString();
                this.honors = in.createStringArrayList();
            }

            public static final Creator<TeacherInfosBean> CREATOR = new Creator<TeacherInfosBean>() {
                @Override
                public TeacherInfosBean createFromParcel(Parcel source) {
                    return new TeacherInfosBean(source);
                }

                @Override
                public TeacherInfosBean[] newArray(int size) {
                    return new TeacherInfosBean[size];
                }
            };
        }

        public static class StudentInfosBean {
            /**
             * status : 0
             * id : 98004
             * avatar :
             * name : Mandy
             */

            private int status;
            private int id;
            private String avatar;
            private String name;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.personal_num);
            dest.writeInt(this.sign_out_num);
            dest.writeInt(this.sign_in_num);
            dest.writeInt(this.sick_num);
            dest.writeTypedList(this.teacher_infos);
            dest.writeList(this.student_infos);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.personal_num = in.readInt();
            this.sign_out_num = in.readInt();
            this.sign_in_num = in.readInt();
            this.sick_num = in.readInt();
            this.teacher_infos = in.createTypedArrayList(TeacherInfosBean.CREATOR);
            this.student_infos = new ArrayList<StudentInfosBean>();
            in.readList(this.student_infos, StudentInfosBean.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeParcelable(this.data, flags);
    }

    public ClassCardStudentListBean() {
    }

    protected ClassCardStudentListBean(Parcel in) {
        this.status = in.readInt();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Creator<ClassCardStudentListBean> CREATOR = new Creator<ClassCardStudentListBean>() {
        @Override
        public ClassCardStudentListBean createFromParcel(Parcel source) {
            return new ClassCardStudentListBean(source);
        }

        @Override
        public ClassCardStudentListBean[] newArray(int size) {
            return new ClassCardStudentListBean[size];
        }
    };
}
