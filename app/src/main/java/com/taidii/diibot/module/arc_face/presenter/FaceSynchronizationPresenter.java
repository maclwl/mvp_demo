package com.taidii.diibot.module.arc_face.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.face.CloudFaceResponse;
import com.taidii.diibot.entity.face.FaceDataBean;
import com.taidii.diibot.entity.face.FaceRegisterInfo;
import com.taidii.diibot.entity.face.TokenInfo;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.module.arc_face.contract.FaceSynchronizationContract;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.module.arc_face.model.FaceSynchronizationModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.OrmDBHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceSynchronizationPresenter extends BasePresenter<FaceSynchronizationContract.Model,FaceSynchronizationContract.View> implements FaceSynchronizationContract.Presenter{

    @Override
    protected FaceSynchronizationContract.Model createModel() {
        return new FaceSynchronizationModel();
    }

    @Override
    public void initFaceList(List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> allGuardianList) {
        /*匹配刷新列表*/
        getView().showLoading();
        mateFaceInfo(allCollectionList,allGuardianList);
    }

    @Override
    public void getToken(List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> allGuardianList) {
        getModel().getToken(new ResultCallback<TokenInfo>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(TokenInfo result) {
                if (result!=null){
                    GlobalParams.token = result.getToken();
                    getCloudFaceList(result.getToken(),allCollectionList,allGuardianList);
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void getCloudFaceList(String token,List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> allGuardianList) {
        getModel().getCloudFaceList(token, new ResultCallback<CloudFaceResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(CloudFaceResponse result) {
                getView().hideLoading();
                if (result!=null&&result.getData().size()>0){
                    registerCloudFace(result.getData(),allCollectionList,allGuardianList);
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*云端人脸库注册到本地，同步数据*/
    private void registerCloudFace(List<FaceDataBean> cloudFaceList, List<MainSchoolCollectionModel> allCollectionList, List<GuardiansBean> allGuardianList){
        /*赛选出游人脸的数组*/
        List<FaceDataBean> existFaceList = new ArrayList<>();
        for (FaceDataBean faceData : cloudFaceList){
            if (!TextUtils.isEmpty(faceData.getImage())){
                existFaceList.add(faceData);
            }else {
                //做查找删除
                FaceRegisterInfo info = OrmDBHelper.getInstance().getInfoById(faceData.getId(),FaceRegisterInfo.class);
                if (info!=null){
                    OrmDBHelper.getInstance().delete(info);
                }
            }
        }
        /*注册到本地人脸数据库*/
        getView().initProgress(existFaceList.size());
        List<String> failNameList = new ArrayList<>();
        for (FaceDataBean faceData : existFaceList) {
            Glide.with(BaseApplication.getInstance()).asBitmap().load(faceData.getImage())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> emitter) {

                                    Bitmap bitmap = ArcSoftImageUtil.getAlignedBitmap(resource, true);
                                    // 为图像数据分配内存
                                    byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
                                    // 图像格式转换
                                    int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
                                    bitmap.recycle();
                                    if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                                        emitter.onNext(false);
                                    }else {
                                        String faceType = null;
                                        switch (faceData.getType()){
                                            case 1:
                                                faceType = Constant.FACE_STUDENT;
                                                break;
                                            case 2:
                                                faceType = Constant.FACE_STAFF;
                                                break;
                                            case 3:
                                                faceType = Constant.FACE_GUARDIAN;
                                                break;
                                        }
                                        boolean success = ArcFaceServer.getInstance().registerFaceBgr24(BaseApplication.getInstance(), bgr24, bitmap.getWidth(), bitmap.getHeight(),
                                                faceData.getName(),faceData.getPerson_id(),faceData.getImage(),faceType);
                                        emitter.onNext(success);
                                        if (existFaceList.indexOf(faceData)==existFaceList.size()-1){
                                            emitter.onComplete();
                                        }
                                    }
                                }
                            })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Boolean>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(Boolean success) {
                                            resource.recycle();
                                            getView().progress(existFaceList.indexOf(faceData));
                                            if (!success){
                                                failNameList.add(faceData.getName());
                                            }

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                            Log.i("aaaaaaaa", "onError: ---出错---");
                                            getView().progress(existFaceList.indexOf(faceData));
                                        }

                                        @Override
                                        public void onComplete() {
                                            /*注册完成匹配刷新列表*/
                                            mateFaceInfo(allCollectionList,allGuardianList);
                                        }
                                    });
                        }
                    });
        }
    }

    private void mateFaceInfo(List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardianList){
        /*获取本来人脸库列表*/
        List<FaceRegisterInfo> faceRegisterInfoList = OrmDBHelper.getInstance().getQueryAll(FaceRegisterInfo.class);
        if (faceRegisterInfoList.size()>0){
            /*学生老师*/
            for (MainSchoolCollectionModel collectionModel : allCollectionList){
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStudentsBean()!=null){
                        for (FaceRegisterInfo faceRegister : faceRegisterInfoList){
                            if (faceRegister.getId() == contentModel.getStudentsBean().getCenterStudentId()){
                                contentModel.getStudentsBean().setRegisterFace(true);
                                contentModel.getStudentsBean().setFaceImage(faceRegister.getFaceImage());
                            }
                        }
                    }
                    if (contentModel.getStaffsBean()!=null){
                        for (FaceRegisterInfo faceRegister : faceRegisterInfoList){
                            if (faceRegister.getId() == contentModel.getStaffsBean().getStaffId()){
                                contentModel.getStaffsBean().setRegisterFace(true);
                                contentModel.getStaffsBean().setFaceImage(faceRegister.getFaceImage());
                            }
                        }
                    }
                }
            }
            /*家长*/
            for (GuardiansBean guardian : guardianList){
                for (FaceRegisterInfo faceRegister : faceRegisterInfoList){
                    if (faceRegister.getName().equals(guardian.getName())){
                        guardian.setRegisterFace(true);
                        guardian.setFaceImage(faceRegister.getFaceImage());
                    }
                }
            }
        }
        getView().hideLoading();
        getView().refreshChangeList(allCollectionList);
    }

}
