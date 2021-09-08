package com.taidii.diibot.module.school_main.model;

import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.VisitorListBean;
import com.taidii.diibot.module.school_main.contract.VisitorListContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;

public class VisitorListModel extends BaseModel implements VisitorListContract.Model {

    @Override
    public void getVisitorList(ResultCallback<VisitorListBean> callBack) {
        String url = String.format(ApiContainer.GET_VISITOR_LIST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject params = new JsonObject();
        OKHttpUtils.getNoToken(url,null,null,this,new OKHttpUtils.OnResponse<VisitorListBean>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(VisitorListBean result) {
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
