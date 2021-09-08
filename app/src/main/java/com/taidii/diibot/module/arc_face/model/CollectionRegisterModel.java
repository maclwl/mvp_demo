package com.taidii.diibot.module.arc_face.model;
import androidx.collection.ArrayMap;
import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseModel;
import com.taidii.diibot.entity.face.FaceDetailInfo;
import com.taidii.diibot.entity.face.EditAvatarRsp;
import com.taidii.diibot.entity.face.PostToFaceRsp;
import com.taidii.diibot.entity.face.TeacherProfileRsp;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.module.arc_face.contract.CollectionRegisterContract;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;

public class CollectionRegisterModel extends BaseModel implements CollectionRegisterContract.Model {

    @Override
    public void inputStudentFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack) {
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
            }
        });
    }

    @Override
    public void requestApiKey(FaceDetailInfo faceDetailInfo,ResultCallback<TeacherProfileRsp> callBack) {
        String url = ApiContainer.SVC_GET_TEACHER_PROFILE;
        OKHttpUtils.get(url, this,new OKHttpUtils.OnResponse<TeacherProfileRsp>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(TeacherProfileRsp result) {
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
            }
        });
    }

    @Override
    public void requestStudentFace(String face_token,String image,FaceDetailInfo faceDetailInfo, ResultCallback<PostToFaceRsp> callBack) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("face_token", face_token);
        params.put("student_id", String.valueOf(faceDetailInfo.getId()));
        params.put("image", image);
        String url = ApiContainer.SVC_UPLOAD_STUDENT_FACE;
        OKHttpUtils.postImageForm(url, params, this,new OKHttpUtils.OnResponse<PostToFaceRsp>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(PostToFaceRsp result) {
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

    /*获取家长登录token*/
    @Override
    public void inputGuardianFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack) {
        String url = ApiContainer.SVC_GET_TOKEN_GUARDIAN;
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
            }
        });
    }

    /*家长人脸信息更新*/
    @Override
    public void requestGuardianFace(FaceDetailInfo faceDetailInfo, ResultCallback<EditAvatarRsp> callBack) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("avatar", faceDetailInfo.getUri());
        String url = String.format(ApiContainer.SVC_UPLOAD_USER_AVATAR, faceDetailInfo.getId());
        OKHttpUtils.postForm(url, params, this,new OKHttpUtils.OnResponse<EditAvatarRsp>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(EditAvatarRsp result) {
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

    /*获取token*/
    @Override
    public void inputStaffFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack) {
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
            }
        });
    }

    /*上传老师头像*/
    @Override
    public void requestStaffFace(FaceDetailInfo faceDetailInfo, ResultCallback<EditAvatarRsp> callBack) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("avatar", faceDetailInfo.getUri());
        String url = String.format(ApiContainer.SVC_UPLOAD_AVATAR, faceDetailInfo.getId());
        OKHttpUtils.postForm(url, params, this,new OKHttpUtils.OnResponse<EditAvatarRsp>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onSuccess(EditAvatarRsp result) {
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
