package com.taidii.diibot.module.organ_main.model;

import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.organ.LocalOrganData;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.module.organ_main.contract.OrganMainContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;

public class OrganMainModel extends BaseModel implements OrganMainContract.Model {

    @Override
    public void getLocalOrganData(ResultCallback<LocalOrganData> callBack) {
        String url = String.format(ApiContainer.GET_LOCAL_ORGAN_DATA, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<LocalOrganData>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(LocalOrganData result) {
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
    public void getDesInfo(ResultCallback<DesInfoBean> callBack) {
        String url = String.format(ApiContainer.GET_DES_INFO,SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<DesInfoBean>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(DesInfoBean result) {
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
