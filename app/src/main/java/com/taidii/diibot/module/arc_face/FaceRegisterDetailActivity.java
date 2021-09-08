package com.taidii.diibot.module.arc_face;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.face.FaceCheck;
import com.taidii.diibot.entity.face.FaceDetailInfo;
import com.taidii.diibot.entity.face.FaceRegisterResult;
import com.taidii.diibot.entity.face.EditAvatarRsp;
import com.taidii.diibot.entity.face.PostToFaceRsp;
import com.taidii.diibot.entity.face.TeacherProfileRsp;
import com.taidii.diibot.module.arc_face.contract.CollectionRegisterContract;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.module.arc_face.presenter.CollectionRegisterPresenter;
import com.taidii.diibot.utils.DataHolder;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.FileUtil;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.JsonUtils;
import com.taidii.diibot.utils.LogUtils;

import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 老diibot与新的diibear 与diilight 衔接（存在缺陷：接口调取不统一，（先拿各个类型token，再做操作））
 */

public class FaceRegisterDetailActivity extends BaseMvpActivity<CollectionRegisterPresenter> implements CollectionRegisterContract.View{

    @BindView(R.id.im_face)
    ImageView imFace;

    private String type;
    private FaceDetailInfo faceDetailInfo;

    private String key;
    private String secret;
    private String faceToken;
    private String waitForUploadAvatarImagePath;

    private static final int MSG_UPLOAD_PIC = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //本地人脸库初始化
        ArcFaceServer.getInstance().init(this);
    }

    @Override
    protected CollectionRegisterPresenter createPresenter() {
        return new CollectionRegisterPresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_register_detail;
    }

    @Override
    protected void init() {
        type = getIntent().getStringExtra(Constant.TYPE);
        faceDetailInfo = (FaceDetailInfo) DataHolder.getInstance().getData(Constant.DATA);
        if (faceDetailInfo!=null){
            ImageViewUtils.loadImage(this,faceDetailInfo.getUri(),imFace,0);
        }
    }

    @OnClick({R.id.tv_reentry, R.id.tv_confirm_entry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reentry:
                finish();
                break;
            case R.id.tv_confirm_entry:
                registerFace();
                break;
        }
    }

    /*分类别上传（分有学生（教师权限，上传学生人脸信息），家长（更新家长头像），老师（更新老师头像））*/
    private void registerFace(){
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) {
                    boolean success = ArcFaceServer.getInstance().registerFaceBgr24(FaceRegisterDetailActivity.this, faceDetailInfo.getBgr24(), faceDetailInfo.getWidth(), faceDetailInfo.getHeight(),
                            faceDetailInfo.getName(),faceDetailInfo.getId(),faceDetailInfo.getUri(),type);
                    emitter.onNext(success);
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean success) {
                            if (success){
                                /*人脸图片上传云端（根据类型调不同接口*/
                                switch (type){
                                    case Constant.FACE_STUDENT:
                                        mPresenter.inputStudentFace(faceDetailInfo);
                                        break;
                                    case Constant.FACE_STAFF:
                                        mPresenter.inputStaffFace(faceDetailInfo);
                                        break;
                                    case Constant.FACE_GUARDIAN:
                                        mPresenter.inputGuardianFace(faceDetailInfo);
                                        break;
                                }
                            }
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

    /*---------------------学生人脸上传start----------------------*/
    /*流程：首先调接口获取token，然后调获取key，secret接口，-》鲁班压缩图片，-》核对人脸信息，获取face_token-》上传服务器*/
    /*face++获取face_token所需的key secret*/
    @Override
    public void apiKeySecret(TeacherProfileRsp teacherProfileRsp) {
        key = teacherProfileRsp.getUser().getFacepp_api_key();
        secret = teacherProfileRsp.getUser().getFacepp_api_secret();
        /*压缩图片*/
        compressPic();
    }

    /*学生人脸信息上传成功*/
    @Override
    public void studentFaceInputSuccess(PostToFaceRsp postToFaceRsp) {
        /*更新本地人脸库图片信息*/
        updateRefresh(postToFaceRsp.getFace_recognition_image());
    }

    /*鲁班图片压缩*/
    private void compressPic(){
        final File fileC = new File(faceDetailInfo.getUri());
        Luban.with(this)
                .load(fileC)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.out("压缩后大小:" + file.length());
                        waitForUploadAvatarImagePath = file.getAbsolutePath();
                        checkFace(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        waitForUploadAvatarImagePath = faceDetailInfo.getUri();
                        checkFace(faceDetailInfo.getUri());
                    }
                }).launch();
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Diibot/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /*face++验证人脸，获取到face_token*/
    private void checkFace(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonOperate commonOperate = new CommonOperate(key, secret, false);
                try {
                    Response response1 = commonOperate.detectByte(FileUtil.getBytes(url), 0, null);
                    if (response1.getStatus() == 403) {
                        showMessage(R.string.face_fail);
                        hideLoading();
                    }
                    List<FaceCheck.FacesBean> facesBeanList = getFaceToken(response1);
                    if (null != facesBeanList && facesBeanList.size() == 1) {
                        faceToken = facesBeanList.get(0).getFace_token();
                        mHandler.sendEmptyMessage(MSG_UPLOAD_PIC);
                    } else {
                        hideLoading();
                        if (null == facesBeanList || facesBeanList.size() == 0){
                            showMessage(R.string.face_fail);
                        }else {
                            showMessage(R.string.face_fail);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<FaceCheck.FacesBean> getFaceToken(Response response) throws JSONException {
        if (response.getStatus() != 200) {
            return null;
        }
        String res = new String(response.getContent());
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(res).getAsJsonObject();
        if (json.has("faces")) {
            FaceCheck.FacesBean[] datas = JsonUtils.fromJson(json.get("faces").getAsJsonArray(), FaceCheck
                    .FacesBean[].class);
            return Arrays.asList(datas);
        }

        return null;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPLOAD_PIC:
                    mPresenter.requestStudentFace(faceToken,waitForUploadAvatarImagePath,faceDetailInfo);
                    break;
            }
        }
    };

    /*---------------------家长人脸上传start----------------------*/
    /*流程：调取家长登录获取token接口，表单上传更新家长头像*/
    @Override
    public void guardianFaceInputSuccess(EditAvatarRsp editAvatarRsp) {
        updateRefresh(editAvatarRsp.getAvatar());
    }

    /*---------------------老师人脸上传start----------------------*/
    /*流程：调取老师登录获取token接口，表单上传更新家长头像*/
    @Override
    public void staffFaceInputSuccess(EditAvatarRsp editAvatarRsp) {
        updateRefresh(editAvatarRsp.getAvatar_url());
    }

    /*人脸信息上传成功，更新本地人脸库，刷新界面*/
    private void updateRefresh(String url){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                boolean success = ArcFaceServer.getInstance().registerFaceBgr24(FaceRegisterDetailActivity.this, faceDetailInfo.getBgr24(), faceDetailInfo.getWidth(), faceDetailInfo.getHeight(),
                        faceDetailInfo.getName(),faceDetailInfo.getId(),url,type);
                emitter.onNext(success);
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success){
                            FaceRegisterResult result = new FaceRegisterResult(url,type);
                            EventBusUtil.sendEvent(new Event(10,result));
                            finish();
                            CollectionFaceActivity.instance.finish();
                        }
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
}
