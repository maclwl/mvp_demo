package com.taidii.diibot.module.school_main.presenter;

import android.text.TextUtils;

import com.taidii.diibot.R;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.VisitorDeleteResponse;
import com.taidii.diibot.entity.school.VisitorInfoResponse;
import com.taidii.diibot.entity.school.VisitorRequestResponse;
import com.taidii.diibot.module.school_main.contract.VisitorDetailContract;
import com.taidii.diibot.module.school_main.model.VisitorDetailModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.DateUtil;

public class VisitorDetailPresenter extends BasePresenter<VisitorDetailContract.Model,VisitorDetailContract.View> implements VisitorDetailContract.Presenter{
    @Override
    protected VisitorDetailContract.Model createModel() {
        return new VisitorDetailModel();
    }

    @Override
    public void getVisitorDetail(int visitor_id) {
        getModel().getVisitorDetail(visitor_id, new ResultCallback<VisitorInfoResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(VisitorInfoResponse visitorInfoResponse) {
                if (visitorInfoResponse!=null&&visitorInfoResponse.getDetail()!=null){
                    getView().visitorDetail(visitorInfoResponse.getDetail());
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void addVisitor(String date,String name, String ic, String temperature, String reason, String contact, int gender, int country, String inDate,String inTime,String outDate,String outTime, boolean checkIn, boolean checkOut,String avatar,String covid_info) {
        String sign_in = "";
        String sign_out = "";
        String sign_in_gmt = "";
        String sign_out_gmt = "";
        /*判断签到*/
        if (checkIn){
            sign_in = inDate +" "+inTime;
            sign_in_gmt = DateUtil.gmtToDate(sign_in);
        }
        if (checkOut){
            sign_out = outDate +" "+outTime;
            sign_out_gmt = DateUtil.gmtToDate(sign_out);
        }
        if (TextUtils.isEmpty(name)){
            getView().showMessage(R.string.hint_visitor_name);
            return;
        }

        if (TextUtils.isEmpty(contact)){
            getView().showMessage(R.string.hint_visitor_contact);
            return;
        }

        if (TextUtils.isEmpty(ic)){
            getView().showMessage(R.string.hint_visitor_ic);
            return;
        }

        getModel().addVisitor(date,name, ic, temperature, reason, contact, gender, country, sign_in_gmt, sign_out_gmt, avatar,covid_info,new ResultCallback<VisitorRequestResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(VisitorRequestResponse visitorRequestResponse) {
                getView().visitorRequestBack();
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void editVisitor(String date, String name, String ic, String temperature, String reason, String contact, int gender, int country, String inDate,String inTime,String outDate,String outTime,boolean checkIn,boolean checkOut,String avatar,String covid_info, int visitor_id) {
        int is_old_avatar = 0;
        if (!TextUtils.isEmpty(avatar)){
            is_old_avatar = 0;
        }else {
            is_old_avatar = 1;
        }
        String sign_in = "";
        String sign_out = "";
        String sign_in_gmt = "";
        String sign_out_gmt = "";
        /*判断签到*/
        if (checkIn){
            sign_in = inDate +" "+inTime;
            sign_in_gmt = DateUtil.gmtToDate(sign_in);
        }
        if (checkOut){
            sign_out = outDate +" "+outTime;
            sign_out_gmt = DateUtil.gmtToDate(sign_out);
        }
        getModel().editVisitor(date, name, ic, temperature, reason, contact, gender, country,  sign_in_gmt, sign_out_gmt, avatar,covid_info, is_old_avatar, visitor_id, new ResultCallback<VisitorRequestResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(VisitorRequestResponse visitorRequestResponse) {
                getView().visitorRequestBack();
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void deleteVisitor(int visitor_id) {
        getModel().deleteVisitor(visitor_id, new ResultCallback<VisitorDeleteResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(VisitorDeleteResponse visitorDeleteResponse) {
                getView().visitorRequestBack();
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }


}
