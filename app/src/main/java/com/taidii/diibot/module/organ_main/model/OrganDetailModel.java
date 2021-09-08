package com.taidii.diibot.module.organ_main.model;

import androidx.collection.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.organ.OrganDetailBean;
import com.taidii.diibot.entity.organ.OrganSignResponse;
import com.taidii.diibot.entity.organ.OrganStudentSignBody;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.module.organ_main.contract.OrganDetailContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrganDetailModel extends BaseModel implements OrganDetailContract.Model {

    @Override
    public void getOrganDetail(int student_id, boolean is_child, ResultCallback<OrganDetailBean> callBack) {
        String url = String.format(ApiContainer.GET_ORGAN_DETAIL, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),student_id,is_child);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<OrganDetailBean>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(OrganDetailBean result) {
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
    public void studentSignRequest(OrganStudentSignBody organStudentSignBody, ResultCallback<OrganSignResponse> callBack) {
        List<OrganStudentSignBody> signRequestBodyList = new ArrayList<>();
        signRequestBodyList.add(organStudentSignBody);
        String json = new Gson().toJson(signRequestBodyList);
        String url = String.format(ApiContainer.ORGAN_STUDENT_SIGN_REQUEST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),json,String.valueOf(1));
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<OrganSignResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(OrganSignResponse result) {
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
                callBack.onFinish();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    /*考勤图片上传*/
    @Override
    public void organPictureInput(int studentId, int id, String date, String fileName, ResultCallback<PictureInputResponse> callBack) {
        /*如果无网络，上传失败则保存数据数据库*/
        String url = String.format(ApiContainer.ORGAN_PICTURE_INPUT, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("date",date);
        params.put("json",String.valueOf(studentId));
        params.put("file",fileName);
        params.put("id",String.valueOf(id));
        OKHttpUtils.postImageForm(url, params, this,new OKHttpUtils.OnResponse<PictureInputResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(PictureInputResponse result) {
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
