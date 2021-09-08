package com.taidii.diibot.module.school_main.model;

import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.DeleteHistoryResponse;
import com.taidii.diibot.entity.school.SignHistoryBean;
import com.taidii.diibot.module.school_main.contract.SignHistoryContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignHistoryModel extends BaseModel implements SignHistoryContract.Model {

    @Override
    public void getStudentHistory(int studentId, String fromDate, String toDate, ResultCallback<SignHistoryBean> callBack) {
        String url = String.format(ApiContainer.GET_STUDENT_SIGN_HISTORY, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),studentId,fromDate,toDate);
        OKHttpUtils.get(url,this, new OKHttpUtils.OnResponse<SignHistoryBean>() {
            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(SignHistoryBean result) {
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
    public void deleteSignHistory(int id, int type, ResultCallback<DeleteHistoryResponse> callBack) {
        List<String> valueList = new ArrayList<>();
        valueList.add(String.format(Constant.DELETE_HISTORY_VALUE,id,type));
        String json = new Gson().toJson(valueList);
        String url = String.format(ApiContainer.DELETE_SIGN_HISTORY, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),json);

        OKHttpUtils.delete(url,this, new OKHttpUtils.OnResponse<DeleteHistoryResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(DeleteHistoryResponse result) {
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
