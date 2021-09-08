package com.taidii.diibot.module.arc_face.contract;

import com.taidii.diibot.base.mvp.IModel;
import com.taidii.diibot.base.mvp.IView;
import com.taidii.diibot.entity.face.FaceDetailInfo;
import com.taidii.diibot.entity.face.EditAvatarRsp;
import com.taidii.diibot.entity.face.PostToFaceRsp;
import com.taidii.diibot.entity.face.TeacherProfileRsp;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.net.ResultCallback;

public class CollectionRegisterContract {

    public interface View extends IView{

        void apiKeySecret(TeacherProfileRsp teacherProfileRsp);

        void studentFaceInputSuccess(PostToFaceRsp postToFaceRsp);

        void guardianFaceInputSuccess(EditAvatarRsp editAvatarRsp);

        void staffFaceInputSuccess(EditAvatarRsp editAvatarRsp);
    }

    public interface Model extends IModel {

        void inputStudentFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack);

        void requestStudentFace(String face_token,String image,FaceDetailInfo faceDetailInfo,ResultCallback<PostToFaceRsp> callBack);

        void requestApiKey(FaceDetailInfo faceDetailInfo,ResultCallback<TeacherProfileRsp> callBack);

        void inputGuardianFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack);

        void requestGuardianFace(FaceDetailInfo faceDetailInfo, ResultCallback<EditAvatarRsp> callBack);

        void inputStaffFace(FaceDetailInfo faceDetailInfo, ResultCallback<TokenInfo> callBack);

        void requestStaffFace(FaceDetailInfo faceDetailInfo, ResultCallback<EditAvatarRsp> callBack);
    }

    public interface Presenter{

        void inputStudentFace(FaceDetailInfo faceDetailInfo);

        void requestStudentFace(String face_token,String image,FaceDetailInfo faceDetailInfo);

        void inputGuardianFace(FaceDetailInfo faceDetailInfo);

        void inputStaffFace(FaceDetailInfo faceDetailInfo);
    }

}
