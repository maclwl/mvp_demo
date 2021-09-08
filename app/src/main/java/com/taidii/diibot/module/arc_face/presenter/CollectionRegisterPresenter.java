package com.taidii.diibot.module.arc_face.presenter;

import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.face.FaceDetailInfo;
import com.taidii.diibot.entity.face.EditAvatarRsp;
import com.taidii.diibot.entity.face.PostToFaceRsp;
import com.taidii.diibot.entity.face.TeacherProfileRsp;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.module.arc_face.contract.CollectionRegisterContract;
import com.taidii.diibot.module.arc_face.model.CollectionRegisterModel;
import com.taidii.diibot.net.ResultCallback;

public class CollectionRegisterPresenter extends BasePresenter<CollectionRegisterContract.Model,CollectionRegisterContract.View> implements CollectionRegisterContract.Presenter{

    @Override
    protected CollectionRegisterContract.Model createModel() {
        return new CollectionRegisterModel();
    }

    @Override
    public void inputStudentFace(FaceDetailInfo faceDetailInfo) {
        /*首先获取token 再表单上传图片*/
        getModel().inputStudentFace(faceDetailInfo, new ResultCallback<TokenInfo>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(TokenInfo tokenInfo) {
                /*获取对应的api_key api_secret (通过此来获取face_token) */
                if (tokenInfo!=null){
                    GlobalParams.token = tokenInfo.getToken();
                    requestApiKey(faceDetailInfo);
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    private void requestApiKey(FaceDetailInfo faceDetailInfo){
        getModel().requestApiKey(faceDetailInfo, new ResultCallback<TeacherProfileRsp>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(TeacherProfileRsp teacherProfileRsp) {
                getView().apiKeySecret(teacherProfileRsp);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*学生人脸信息上传云端*/
    @Override
    public void requestStudentFace(String face_token, String image, FaceDetailInfo faceDetailInfo) {
        getModel().requestStudentFace(face_token,image,faceDetailInfo, new ResultCallback<PostToFaceRsp>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PostToFaceRsp postToFaceRsp) {
                getView().studentFaceInputSuccess(postToFaceRsp);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*家长token*/
    @Override
    public void inputGuardianFace(FaceDetailInfo faceDetailInfo) {
        getModel().inputGuardianFace(faceDetailInfo, new ResultCallback<TokenInfo>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(TokenInfo tokenInfo) {
                if (tokenInfo!=null){
                    GlobalParams.token = tokenInfo.getToken();
                    requestGuardianFace(faceDetailInfo);
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*家长人脸上传*/
    private void requestGuardianFace(FaceDetailInfo faceDetailInfo){
        getModel().requestGuardianFace(faceDetailInfo, new ResultCallback<EditAvatarRsp>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(EditAvatarRsp editAvatarRsp) {
                getView().guardianFaceInputSuccess(editAvatarRsp);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*获取token*/
    @Override
    public void inputStaffFace(FaceDetailInfo faceDetailInfo) {
        getModel().inputStaffFace(faceDetailInfo, new ResultCallback<TokenInfo>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(TokenInfo tokenInfo) {
                if (tokenInfo!=null){
                    GlobalParams.token = tokenInfo.getToken();
                    requestStaffFace(faceDetailInfo);
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*老师头像上传*/
    private void requestStaffFace(FaceDetailInfo faceDetailInfo){
        getModel().requestStaffFace(faceDetailInfo, new ResultCallback<EditAvatarRsp>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(EditAvatarRsp editAvatarRsp) {
                getView().staffFaceInputSuccess(editAvatarRsp);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }


}
