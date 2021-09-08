package com.taidii.diibot.module.school_main.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.school.HealthCheckBody;
import com.taidii.diibot.entity.school.HealthCheckCacheDate;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.module.school_main.contract.HealthContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.OrmDBHelper;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HealthModel extends BaseModel implements HealthContract.Model {

    @Override
    public void healthRequest(HealthCheckBody healthCheckBody, ResultCallback<HealthCheckResponse> callBack) {
        List<HealthCheckBody> healthList = new ArrayList<>();
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
                HealthCheckCacheDate checkCacheDate = new HealthCheckCacheDate();
                checkCacheDate.setStudentid(healthCheckBody.getStudentid());
                checkCacheDate.setDate(healthCheckBody.getDate());
                checkCacheDate.setPickupid(healthCheckBody.getPickupid());
                checkCacheDate.setTemperature(healthCheckBody.getTemperature());
                checkCacheDate.setPickuptype(healthCheckBody.getPickuptype());
                checkCacheDate.setWeight(healthCheckBody.getWeight());
                checkCacheDate.setType(healthCheckBody.getType());
                checkCacheDate.setBusDriverId(healthCheckBody.getBusDriverId());
                checkCacheDate.setBusId(healthCheckBody.getBusId());
                checkCacheDate.setRemarks(healthCheckBody.getRemarks());
                checkCacheDate.setCommon(healthCheckBody.getCommon());
                OrmDBHelper.getInstance().insert(checkCacheDate);
                /*体侧成功通知*/
                HealthCheckResponse response = new HealthCheckResponse();
                response.setStatusCode(1);
                callBack.onSuccess(response);

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onFinish();
            }
        });
    }
}
