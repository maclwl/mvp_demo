package com.taidii.diibot.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.school.LocalSchoolData;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 云端人脸信息注册本地人脸库
 */
public class FaceRegisterService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null ) {
            registerFace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private synchronized void registerFace() {
        String url = String.format(ApiContainer.GET_LOCAL_SCHOOL_DATA, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject params = new JsonObject();
        OKHttpUtils.postNoToken(url, null, params, this,new OKHttpUtils.OnResponse<LocalSchoolData>() {

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onSuccess(LocalSchoolData result) {
                /*注册人脸*/
                registerFaceInfo(result);
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void registerFaceInfo(LocalSchoolData localSchoolData){
        List<StudentsBean> studentList = localSchoolData.getStudents();
        for (StudentsBean student : studentList) {
            Glide.with(BaseApplication.getInstance()).asBitmap().load(student.getAvatar())
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
                                    if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                                        return;
                                    }
                                    boolean success = ArcFaceServer.getInstance().registerBgr24(BaseApplication.getInstance(), bgr24, bitmap.getWidth(), bitmap.getHeight(),
                                            student.getName());
                                    emitter.onNext(success);
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

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    });
        }
    }
}
