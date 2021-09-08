package com.taidii.diibot.module.arc_face.model;

import androidx.collection.ArrayMap;

import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.face.CloudFaceResponse;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.module.arc_face.contract.FaceSynchronizationContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;

public class FaceSynchronizationModel extends BaseModel implements FaceSynchronizationContract.Model {

    @Override
    public void getToken(ResultCallback<TokenInfo> callBack) {
        String url = ApiContainer.GET_TOKEN;
        JsonObject params = new JsonObject();
        params.addProperty("username", SharePrefUtils.getString(Constant.USERNAME));
        params.addProperty("password", SharePrefUtils.getString(Constant.PASSWORD));
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<TokenInfo>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(TokenInfo result) {
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
    public void getCloudFaceList(String token,ResultCallback<CloudFaceResponse> callBack) {
        String url = ApiContainer.GET_CLOUD_FACE;
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", OKHttpUtils.TOKEN_PREFIX + token);
        OKHttpUtils.post(url, null, null, this, new OKHttpUtils.OnResponse<CloudFaceResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(CloudFaceResponse result) {
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
