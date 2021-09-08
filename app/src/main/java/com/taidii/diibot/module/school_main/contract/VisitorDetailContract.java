package com.taidii.diibot.module.school_main.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.school.VisitorDeleteResponse;
import com.taidii.diibot.entity.school.VisitorDetailBean;
import com.taidii.diibot.entity.school.VisitorInfoResponse;
import com.taidii.diibot.entity.school.VisitorRequestResponse;
import com.taidii.diibot.net.ResultCallback;

public class VisitorDetailContract {

    public interface View extends IView {

        void visitorDetail(VisitorDetailBean visitorDetailBean);

        void visitorRequestBack();
    }

    public interface Model extends IModel {

        void getVisitorDetail(int visitor_id,ResultCallback<VisitorInfoResponse> callBack);

        void addVisitor(String date,String name,String ic,String temperature,String reason,String contact,int gender,int country,
                        String sign_in,String sign_out,String avatar,String covid_info,ResultCallback<VisitorRequestResponse> callBack);

        void editVisitor(String date,String name,String ic,String temperature,String reason,String contact,int gender,int country,
                        String sign_in,String sign_out,String avatar,String covid_info,int is_old_avatar,int visitor_id,ResultCallback<VisitorRequestResponse> callBack);


        void deleteVisitor(int visitor_id,ResultCallback<VisitorDeleteResponse> callBack);
    }

    public interface Presenter {

        void getVisitorDetail(int visitor_id);

        void addVisitor(String date,String name,String ic,String temperature,String reason,String contact,int gender,int country,
                        String inDate,String inTime,String outDate,String outTime,boolean checkIn,boolean checkOut,String avatar,String covid_info);

        void editVisitor(String date,String name,String ic,String temperature,String reason,String contact,int gender,int country,
                         String inDate,String inTime,String outDate,String outTime,boolean checkIn,boolean checkOut,String avatar,String covid_info,int visitor_id);

        void deleteVisitor(int visitor_id);
    }

}
