package com.taidii.diibot.module.login.model;

import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.CenterTypeBean;
import com.taidii.diibot.module.login.conract.LoginContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.google.gson.JsonObject;

import java.io.IOException;

public class LoginModel extends BaseModel implements LoginContract.Model {

    @Override
    public void getCenterType(String username,String password,ResultCallback<CenterTypeBean> callBack) {
        String url = String.format(ApiContainer.GET_CENTER_TYPE,username,password);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<CenterTypeBean>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(CenterTypeBean result) {
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
