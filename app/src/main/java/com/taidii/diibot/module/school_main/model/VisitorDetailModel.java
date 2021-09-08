package com.taidii.diibot.module.school_main.model;

import androidx.collection.ArrayMap;

import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.VisitorDeleteResponse;
import com.taidii.diibot.entity.school.VisitorInfoResponse;
import com.taidii.diibot.entity.school.VisitorRequestResponse;
import com.taidii.diibot.module.school_main.contract.VisitorDetailContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;

public class VisitorDetailModel extends BaseModel implements VisitorDetailContract.Model {

    @Override
    public void getVisitorDetail(int visitor_id, ResultCallback<VisitorInfoResponse> callBack) {
        String url = String.format(ApiContainer.GET_VISITOR_DETAIL, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject params = new JsonObject();
        params.addProperty("visitor_id", visitor_id);
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<VisitorInfoResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(VisitorInfoResponse result) {
                callBack.onFinish();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void addVisitor(String date,String name, String ic, String temperature, String reason, String contact, int gender, int country, String sign_in,
                           String sign_out, String avatar,String covid_info,ResultCallback<VisitorRequestResponse> callBack) {

        String url = String.format(ApiContainer.ADD_NEW_VISITOR, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("username", SharePrefUtils.getString(Constant.USERNAME));
        params.put("password", SharePrefUtils.getString(Constant.PASSWORD));
        params.put("date", date);
        params.put("date", date);
        params.put("name", name);
        params.put("ic", ic);
        params.put("temperature", temperature);
        params.put("reason", reason);
        params.put("contact", contact);
        params.put("gender", String.valueOf(gender));
        params.put("country", String.valueOf(country));
        params.put("sign_in", sign_in);
        params.put("sign_out", sign_out);
        params.put("covid_info", covid_info);
        params.put("avatar", avatar);
        OKHttpUtils.postFormNoToken(url, params, this,new OKHttpUtils.OnResponse<VisitorRequestResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(VisitorRequestResponse result) {
                callBack.onFinish();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });

    }

    @Override
    public void editVisitor(String date, String name, String ic, String temperature, String reason, String contact, int gender, int country, String sign_in, String sign_out, String avatar,String covid_info, int is_old_avatar, int visitor_id, ResultCallback<VisitorRequestResponse> callBack) {
        String url = String.format(ApiContainer.EDIT_UPDATE_VISITOR, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("username", SharePrefUtils.getString(Constant.USERNAME));
        params.put("password", SharePrefUtils.getString(Constant.PASSWORD));
        params.put("date", date);
        params.put("date", date);
        params.put("name", name);
        params.put("ic", ic);
        params.put("temperature", temperature);
        params.put("reason", reason);
        params.put("contact", contact);
        params.put("gender", String.valueOf(gender));
        params.put("country", String.valueOf(country));
        params.put("sign_in", sign_in);
        params.put("sign_out", sign_out);
        params.put("is_old_avatar", String.valueOf(is_old_avatar));
        params.put("visitor_id", String.valueOf(visitor_id));
        params.put("covid_info", covid_info);
        params.put("avatar", avatar);
        OKHttpUtils.postFormNoToken(url, params, this,new OKHttpUtils.OnResponse<VisitorRequestResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(VisitorRequestResponse result) {
                callBack.onFinish();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });

    }

    @Override
    public void deleteVisitor(int visitor_id, ResultCallback<VisitorDeleteResponse> callBack) {

        String url = String.format(ApiContainer.EDIT_VISITOR, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),visitor_id);

        OKHttpUtils.deleteNoToken(url,this, new OKHttpUtils.OnResponse<VisitorDeleteResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(VisitorDeleteResponse result) {
                callBack.onFinish();
                callBack.onSuccess(result);
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

}
