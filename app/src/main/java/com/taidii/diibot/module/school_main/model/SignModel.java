package com.taidii.diibot.module.school_main.model;

import androidx.collection.ArrayMap;

import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.StaffPictureCacheData;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StaffSignCacheData;
import com.taidii.diibot.entity.school.StudentPictureCacheData;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StudentSignCacheData;
import com.taidii.diibot.module.school_main.contract.SignContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.OrmDBHelper;
import com.taidii.diibot.utils.SharePrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignModel extends BaseModel implements SignContract.Model {

    @Override
    public void studentSignRequest(StudentSignBody studentSignBody, ResultCallback<SignResponse> callBack) {
        List<StudentSignBody> signRequestBodyList = new ArrayList<>();
        signRequestBodyList.add(studentSignBody);
        String json = new Gson().toJson(signRequestBodyList);
        String url = String.format(ApiContainer.STUDENT_SIGN_REQUEST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),json);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<SignResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(SignResponse result) {
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
                /*保存请求数据到本地数据库*/
                StudentSignCacheData signCacheData = new StudentSignCacheData();
                signCacheData.setStudentid(studentSignBody.getStudentid());
                signCacheData.setDate(studentSignBody.getDate());
                signCacheData.setPickupid(studentSignBody.getPickupid());
                signCacheData.setTemperature(studentSignBody.getTemperature());
                signCacheData.setPickuptype(studentSignBody.getPickuptype());
                signCacheData.setWeight(studentSignBody.getWeight());
                signCacheData.setHeight(studentSignBody.getHeight());
                signCacheData.setType(studentSignBody.getType());
                signCacheData.setBusDriverId(studentSignBody.getBusDriverId());
                signCacheData.setBusId(studentSignBody.getBusId());
                signCacheData.setRemarks(studentSignBody.getRemarks());
                signCacheData.setCommon(studentSignBody.getCommon());
                OrmDBHelper.getInstance().insert(signCacheData);
                /*离线，签到成功通知*/
                SignResponse signResponse = new SignResponse();
                signResponse.setStatusCode(1);
                callBack.onSuccess(signResponse);
                /*通知首页刷新*/
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void staffSignRequest(StaffSignBody staffSignBody, ResultCallback<SignResponse> callBack) {
        List<StaffSignBody> signRequestBodyList = new ArrayList<>();
        signRequestBodyList.add(staffSignBody);
        String json = new Gson().toJson(signRequestBodyList);
        String url = String.format(ApiContainer.STAFF_SIGN_REQUEST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),json);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<SignResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(SignResponse result) {
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
                /*保存请求数据到本地数据库*/
                StaffSignCacheData staffSignCacheData = new StaffSignCacheData();
                staffSignCacheData.setStaffid(staffSignBody.getStaffid());
                staffSignCacheData.setDate(staffSignBody.getDate());
                staffSignCacheData.setPickuptype(staffSignBody.getPickuptype());
                staffSignCacheData.setTemperature(staffSignBody.getTemperature());
                staffSignCacheData.setWeight(staffSignBody.getWeight());
                staffSignCacheData.setType(staffSignBody.getType());
                staffSignCacheData.setInputType(staffSignBody.getInputType());
                staffSignCacheData.setRemarks(staffSignBody.getRemarks());
                staffSignCacheData.setCommon(staffSignBody.getCommon());
                OrmDBHelper.getInstance().insert(staffSignCacheData);
                /*离线，签到成功通知*/
                SignResponse signResponse = new SignResponse();
                signResponse.setStatusCode(1);
                callBack.onSuccess(signResponse);
                /*通知首页刷新*/
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    /*学生图片上传*/
    @Override
    public void studentPictureInput(int studentId, String date, String fileName, ResultCallback<PictureInputResponse> callBack) {
        /*如果无网络，上传失败则保存数据数据库*/
        String url = String.format(ApiContainer.STUDENT_PICTURE_INPUT, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("date",date);
        params.put("json",String.valueOf(studentId));
        params.put("file",fileName);
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
                /*上传信息保存本地数据库*/
                StudentPictureCacheData pictureCacheData = new StudentPictureCacheData();
                pictureCacheData.setStudentId(studentId);
                pictureCacheData.setDate(date);
                pictureCacheData.setFileName(fileName);
                OrmDBHelper.getInstance().insert(pictureCacheData);
                /*通知刷新首页*/
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void staffPictureInput(int staffId, String date, String fileName, ResultCallback<PictureInputResponse> callBack) {
        /*如果无网络，上传失败则保存数据数据库*/
        String url = String.format(ApiContainer.STAFF_PICTURE_INPUT, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("date",date);
        params.put("json",String.valueOf(staffId));
        params.put("file",fileName);
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
                /*上传信息保存本地数据库*/
                StaffPictureCacheData pictureCacheData = new StaffPictureCacheData();
                pictureCacheData.setStaffId(staffId);
                pictureCacheData.setDate(date);
                pictureCacheData.setFileName(fileName);
                OrmDBHelper.getInstance().insert(pictureCacheData);
                /*通知刷新首页*/
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }


}
