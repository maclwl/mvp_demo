package com.taidii.diibot.module.school_main.model;
import androidx.collection.ArrayMap;

import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.entity.school.HealthCheckBody;
import com.taidii.diibot.entity.school.HealthCheckCacheDate;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.entity.school.LocalSchoolData;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StaffPictureCacheData;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StaffSignCacheData;
import com.taidii.diibot.entity.school.StudentPictureCacheData;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.StudentSignCacheData;
import com.taidii.diibot.module.school_main.contract.SchoolMainContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.OrmDBHelper;
import com.taidii.diibot.utils.SharePrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchoolMainModel extends BaseModel implements SchoolMainContract.Model {

    @Override
    public void getLocalSchoolData(String username, String password, ResultCallback<LocalSchoolData> callBack) {
        String url = String.format(ApiContainer.GET_LOCAL_SCHOOL_DATA,username,password);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<LocalSchoolData>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(LocalSchoolData result) {
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

    /*-----------离线数据上传相关-----------*/
    @Override
    public void offLineStudentSign(List<StudentSignCacheData> studentSignCacheList, StudentSignCacheData signCacheData, ResultCallback<SignResponse> callBack) {
        int lastIndex = studentSignCacheList.size() -1;
        List<StudentSignBody> signRequestBodyList = new ArrayList<>();
        StudentSignBody studentSignBody = new StudentSignBody();//学生考勤数据
        studentSignBody.setStudentid(signCacheData.getStudentid());
        studentSignBody.setDate(signCacheData.getDate());
        studentSignBody.setPickupid(signCacheData.getPickupid());
        studentSignBody.setTemperature(signCacheData.getTemperature());
        studentSignBody.setPickuptype(signCacheData.getPickuptype());
        studentSignBody.setWeight(signCacheData.getWeight());
        studentSignBody.setHeight(signCacheData.getHeight());
        studentSignBody.setType(signCacheData.getType());
        studentSignBody.setBusDriverId(signCacheData.getBusDriverId());
        studentSignBody.setBusId(signCacheData.getBusId());
        studentSignBody.setRemarks(signCacheData.getRemarks());
        studentSignBody.setCommon(signCacheData.getCommon());
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
                /*1.签到成功则从数据库中删除该条数据；2.判断是否数据库中的离线数据是否已上传完（通过size与index）*/
                OrmDBHelper.getInstance().delete(signCacheData);
                if (studentSignCacheList.indexOf(signCacheData)==lastIndex){
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (studentSignCacheList.indexOf(signCacheData)==lastIndex){
                    SignResponse result = new SignResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                callBack.onFinish();
                /*判断是否上传完*/
                if (studentSignCacheList.indexOf(signCacheData)==lastIndex){
                    SignResponse result = new SignResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void offLineStaffSign(List<StaffSignCacheData> staffSignCacheList, StaffSignCacheData signCacheData, ResultCallback<SignResponse> callBack) {
        int lastIndex = staffSignCacheList.size() -1;
        List<StaffSignBody> signRequestBodyList = new ArrayList<>();
        StaffSignBody staffSignBody = new StaffSignBody();//老师离线考情数据
        staffSignBody.setStaffid(signCacheData.getStaffid());
        staffSignBody.setDate(signCacheData.getDate());
        staffSignBody.setPickuptype(signCacheData.getPickuptype());
        staffSignBody.setTemperature(signCacheData.getTemperature());
        staffSignBody.setWeight(signCacheData.getWeight());
        staffSignBody.setType(signCacheData.getType());
        staffSignBody.setInputType(signCacheData.getInputType());
        staffSignBody.setRemarks(signCacheData.getRemarks());
        staffSignBody.setCommon(signCacheData.getCommon());
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
                /*1.签到成功则从数据库中删除该条数据；2.判断是否数据库中的离线数据是否已上传完（通过size与index）*/
                OrmDBHelper.getInstance().delete(signCacheData);
                if (staffSignCacheList.indexOf(signCacheData)==lastIndex){
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (staffSignCacheList.indexOf(signCacheData)==lastIndex){
                    SignResponse result = new SignResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (staffSignCacheList.indexOf(signCacheData)==lastIndex){
                    SignResponse result = new SignResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void offLineStudentPicture(List<StudentPictureCacheData> studentPictureCacheList, StudentPictureCacheData pictureCacheData, ResultCallback<PictureInputResponse> callBack) {
        int lastIndex = studentPictureCacheList.size() -1;
        String url = String.format(ApiContainer.STUDENT_PICTURE_INPUT, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("date",pictureCacheData.getDate());
        params.put("json",String.valueOf(pictureCacheData.getStudentId()));
        params.put("file",pictureCacheData.getFileName());
        OKHttpUtils.postImageForm(url, params, this,new OKHttpUtils.OnResponse<PictureInputResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(PictureInputResponse result) {
                /*1.签到成功则从数据库中删除该条数据；2.判断是否数据库中的离线数据是否已上传完（通过size与index）*/
                OrmDBHelper.getInstance().delete(pictureCacheData);
                if (studentPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (studentPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    PictureInputResponse result = new PictureInputResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (studentPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    PictureInputResponse result = new PictureInputResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void offLineStaffPicture(List<StaffPictureCacheData> staffPictureCacheList, StaffPictureCacheData pictureCacheData, ResultCallback<PictureInputResponse> callBack) {
        int lastIndex = staffPictureCacheList.size() -1;
        String url = String.format(ApiContainer.STAFF_PICTURE_INPUT, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("date",pictureCacheData.getDate());
        params.put("json",String.valueOf(pictureCacheData.getStaffId()));
        params.put("file",pictureCacheData.getFileName());
        OKHttpUtils.postImageForm(url, params, this,new OKHttpUtils.OnResponse<PictureInputResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(PictureInputResponse result) {
                /*1.签到成功则从数据库中删除该条数据；2.判断是否数据库中的离线数据是否已上传完（通过size与index）*/
                OrmDBHelper.getInstance().delete(pictureCacheData);
                if (staffPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (staffPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    PictureInputResponse result = new PictureInputResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (staffPictureCacheList.indexOf(pictureCacheData)==lastIndex){
                    PictureInputResponse result = new PictureInputResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }

    @Override
    public void offLineHealthCheck(List<HealthCheckCacheDate> healthCheckCacheList, HealthCheckCacheDate healthCheckCacheDate, ResultCallback<HealthCheckResponse> callBack) {
        int lastIndex = healthCheckCacheList.size() -1;
        List<HealthCheckBody> healthList = new ArrayList<>();
        HealthCheckBody healthCheckBody = new HealthCheckBody();//学生考勤数据
        healthCheckBody.setStudentid(healthCheckCacheDate.getStudentid());
        healthCheckBody.setDate(healthCheckCacheDate.getDate());
        healthCheckBody.setPickupid(healthCheckCacheDate.getPickupid());
        healthCheckBody.setTemperature(healthCheckCacheDate.getTemperature());
        healthCheckBody.setPickuptype(healthCheckCacheDate.getPickuptype());
        healthCheckBody.setWeight(healthCheckCacheDate.getWeight());
        healthCheckBody.setType(healthCheckCacheDate.getType());
        healthCheckBody.setBusDriverId(healthCheckCacheDate.getBusDriverId());
        healthCheckBody.setBusId(healthCheckCacheDate.getBusId());
        healthCheckBody.setRemarks(healthCheckCacheDate.getRemarks());
        healthCheckBody.setCommon(healthCheckCacheDate.getCommon());
        healthList.add(healthCheckBody);
        String json = new Gson().toJson(healthList);
        String url = String.format(ApiContainer.HEALTH_CHECK_REQUEST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD),json);
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<HealthCheckResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(HealthCheckResponse result) {
                /*1.签到成功则从数据库中删除该条数据；2.判断是否数据库中的离线数据是否已上传完（通过size与index）*/
                OrmDBHelper.getInstance().delete(healthCheckCacheDate);
                if (healthCheckCacheList.indexOf(healthCheckCacheDate)==lastIndex){
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
                callBack.onFinish();
                /*判断是否上传完*/
                if (healthCheckCacheList.indexOf(healthCheckCacheDate)==lastIndex){
                    HealthCheckResponse result = new HealthCheckResponse();
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                callBack.onFinish();
                /*判断是否上传完*/
                if (healthCheckCacheList.indexOf(healthCheckCacheDate)==lastIndex){
                    HealthCheckResponse result = new HealthCheckResponse();
                    callBack.onSuccess(result);
                }
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
