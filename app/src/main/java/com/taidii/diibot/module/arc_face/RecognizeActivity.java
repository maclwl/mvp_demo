package com.taidii.diibot.module.arc_face;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.enums.DetectFaceOrientPriority;
import com.arcsoft.face.enums.DetectMode;
import com.clj.fastble.BleManager;
import com.gyf.immersionbar.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BasePermissionActivity;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.face.FacePreviewInfo;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.module.arc_face.contract.ArcFaceContract;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.module.arc_face.faceserver.CompareResult;
import com.taidii.diibot.module.arc_face.presenter.ArcFacePresenter;
import com.taidii.diibot.module.school_main.StaffSignActivity;
import com.taidii.diibot.module.school_main.StudentSignActivity;
import com.taidii.diibot.module.school_main.StudentSignListSelectActivity;
import com.taidii.diibot.utils.ConfigUtil;
import com.taidii.diibot.utils.DataHolder;
import com.taidii.diibot.utils.DrawHelper;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.camera.CameraHelper;
import com.taidii.diibot.utils.camera.CameraListener;
import com.taidii.diibot.utils.face.FaceHelper;
import com.taidii.diibot.utils.face.FaceListener;
import com.taidii.diibot.utils.face.LivenessType;
import com.taidii.diibot.utils.face.RequestFeatureStatus;
import com.taidii.diibot.utils.face.RequestLivenessStatus;
import com.taidii.diibot.view.titlebar.MainTopTabLayout;
import com.taidii.diibot.widget.face.FaceRectView;
import com.taidii.diibot.widget.popup.BluetoothConnectFailPopup;
import com.taidii.diibot.widget.popup.BluetoothTempPopup;
import com.taidii.diibot.widget.popup.CloseBluetoothPopup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecognizeActivity extends BasePermissionActivity<ArcFacePresenter> implements ViewTreeObserver.OnGlobalLayoutListener, ArcFaceContract.View
            ,CloseBluetoothPopup.CloseBluetoothListener,BluetoothTempPopup.BluetoothTempListener{

    @BindView(R.id.single_camera_texture_preview)
    TextureView previewView; /*????????????????????????????????????SurfaceView???TextureView*/
    @BindView(R.id.single_camera_face_rect_view)
    FaceRectView faceRectView; /* ????????????????????????*/
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.main_tab)
    MainTopTabLayout mainTab;
    @BindView(R.id.im_logo)
    RoundedImageView imLogo;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.im_connect)
    ImageView imConnect;
    @BindView(R.id.im_rotate)
    ImageView imRotate;

    private boolean isPrepared = false;/*???????????????*/

    private static final String TAG = "RecognizeActivity";

    private static final int MAX_DETECT_NUM = 10;

    /**
     * ???FR??????????????????????????????FR?????????????????????
     */
    private static final int WAIT_LIVENESS_INTERVAL = 100;
    /**
     * ???????????????????????????ms???
     */
    private static final long FAIL_RETRY_INTERVAL = 1000;
    /**
     * ????????????????????????
     */
    private static final int MAX_RETRY_TIME = 3;

    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    /**
     * ??????????????????????????????????????????????????????RGB??????????????????????????????????????????
     */
    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * VIDEO??????????????????????????????????????????????????????
     */
    private FaceEngine ftEngine;
    /**
     * ???????????????????????????
     */
    private FaceEngine frEngine;
    /**
     * IMAGE????????????????????????????????????????????????????????????
     */
    private FaceEngine flEngine;

    private int ftInitCode = -1;
    private int frInitCode = -1;
    private int flInitCode = -1;
    private FaceHelper faceHelper;
    private List<CompareResult> compareResultList;

    /**
     * ?????????????????????
     */
    private boolean livenessDetect = true;

    /**
     * ????????????????????????????????????
     */
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    /**
     * ????????????????????????????????????????????????
     */
    private ConcurrentHashMap<Integer, Integer> extractErrorRetryMap = new ConcurrentHashMap<>();
    /**
     * ?????????????????????
     */
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    /**
     * ??????????????????????????????????????????
     */
    private ConcurrentHashMap<Integer, Integer> livenessErrorRetryMap = new ConcurrentHashMap<>();

    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    private CompositeDisposable delayFaceTaskCompositeDisposable = new CompositeDisposable();

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * ????????????
     */
    private static final float SIMILAR_THRESHOLD = 0.8F;
    /**
     * ???????????????????????????
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE

    };

    private List<GuardiansBean> guardianList = new ArrayList<>();//????????????
    private List<MainSchoolCollectionModel> allCollectionList = new ArrayList<>();//??????????????????
    private SingTypeEnum signType = SingTypeEnum.SIGN_IN;//????????????
    private String center_name;
    private String center_logo;

    /*?????????????????????*/
    private boolean isCloseBluetooth;//??????????????????
    private boolean isVisible = false;//????????????
    private FastBluetoothModel changeModel;//????????????????????????
    private RotateAnimation rotateAnimation;//????????????
    private CloseBluetoothPopup closeBluetoothPopup;//????????????
    private BluetoothTempPopup bluetoothTempPopup;//?????????????????????
    private BluetoothConnectFailPopup bluetoothConnectFailPopup;//??????????????????
    private List<FastBluetoothModel> resultModelList = new ArrayList<>();

    @Override
    protected ArcFacePresenter createPresenter() {
        return new ArcFacePresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }
        // Activity???????????????????????????????????????
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //????????????????????????
        ArcFaceServer.getInstance().init(this);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recongnize;
    }

    @Override
    protected void initView() {
        //???????????????????????????????????????
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        compareResultList = new ArrayList<>();
        /*???????????????tab*/
        mainTab.setDefault();
        /*????????????tab*/
        mainTab.changeScan();
        initListener();
    }

    @Override
    protected void init() {
        super.init();
        guardianList = (List<GuardiansBean>) getIntent().getSerializableExtra(Constant.GUARDIANS_LIST);
        allCollectionList = (List<MainSchoolCollectionModel>) getIntent().getSerializableExtra(Constant.DATA);
        center_name = getIntent().getStringExtra(Constant.NAME);
        center_logo = getIntent().getStringExtra(Constant.AVATAR);
        hasConnect = getIntent().getBooleanExtra(Constant.HAS_CONNECT,false);
        resultModelList = (List<FastBluetoothModel>)DataHolder.getInstance().getData(Constant.BLUETOOTH_DATA);
        tvCenter.setText(center_name);
        ImageViewUtils.loadImage(this, center_logo, imLogo, R.drawable.avatar_loading2);

        bluetoothConnectFailPopup = new BluetoothConnectFailPopup(this);
        bluetoothTempPopup = new BluetoothTempPopup(this,this);
        closeBluetoothPopup = new CloseBluetoothPopup(this,this);

        if(hasConnect){
            imConnect.setSelected(true);
            closeBluetoothPopup.setDateList(resultModelList);
        }else {
            imConnect.setSelected(false);
        }
    }

    /*view??????????????????*/
    private void initListener() {
        /*top_tab*/
        mainTab.setMainTabListener(new MainTopTabLayout.MainTabListener() {
            @Override
            public void tabSelect(SingTypeEnum type) {
                signType = type;
            }
        });
    }

    @OnClick({R.id.im_scan_class, R.id.im_connect, R.id.im_rotate})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.im_scan_class:
                finish();
                break;
            case R.id.im_connect:
                /*???????????????---->??????????????????????????????????????????*/
                if (!hasConnect){
                    /*????????????????????????*/
                    if (isBtEnable()){
                        /*????????????*/
                        bluetoothTempPopup.showPopupWindow();
                    }else {
                        /*????????????*/
                        BleManager.getInstance().enableBluetooth();
                    }
                }else {
                    /*??????????????????*/
                    closeBluetoothPopup.showPopupWindow();
                }
                break;
            case R.id.im_rotate:
                /*?????????????????????????????????????????????*/
                if (isScanning){
                    showLongToast(R.string.scanning_device);
                }else if (isConnecting){
                    showLongToast(R.string.connecting_device);
                }else {
                    /*??????????????????*/
                    bluetoothTempPopup.showPopupWindow();
                }
                break;
        }
    }

    /**
     * ???????????????
     */
    private void initEngine() {
        ftEngine = new FaceEngine();
        ftInitCode = ftEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_DETECT);

        frEngine = new FaceEngine();
        frInitCode = frEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_0_ONLY,
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION);

        flEngine = new FaceEngine();
        flInitCode = flEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_0_ONLY,
                16, MAX_DETECT_NUM, FaceEngine.ASF_LIVENESS);

        Log.i(TAG, "initEngine:  init: " + ftInitCode);

        if (ftInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "ftEngine", ftInitCode);
            Log.i(TAG, "initEngine: " + error);
            if (!SharePrefUtils.getBoolean(Constant.FACE_ACTIVE)){
                showShortToast(R.string.hint_active);
            }else {
                showLongToast(error);
            }
        }
        if (frInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "frEngine", frInitCode);
            Log.i(TAG, "initEngine: " + error);
            if (!SharePrefUtils.getBoolean(Constant.FACE_ACTIVE)){
                showShortToast(R.string.hint_active);
            }else {
                showShortToast(error);
            }
        }
        if (flInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "flEngine", flInitCode);
            Log.i(TAG, "initEngine: " + error);
            if (!SharePrefUtils.getBoolean(Constant.FACE_ACTIVE)){
                showShortToast(R.string.hint_active);
            }else {
                showShortToast(error);
            }
        }
    }

    /**
     * ???????????????faceHelper??????????????????????????????????????????????????????????????????crash
     */
    private void unInitEngine() {
        if (ftInitCode == ErrorInfo.MOK && ftEngine != null) {
            synchronized (ftEngine) {
                int ftUnInitCode = ftEngine.unInit();
                Log.i(TAG, "unInitEngine: " + ftUnInitCode);
            }
        }
        if (frInitCode == ErrorInfo.MOK && frEngine != null) {
            synchronized (frEngine) {
                int frUnInitCode = frEngine.unInit();
                Log.i(TAG, "unInitEngine: " + frUnInitCode);
            }
        }
        if (flInitCode == ErrorInfo.MOK && flEngine != null) {
            synchronized (flEngine) {
                int flUnInitCode = flEngine.unInit();
                Log.i(TAG, "unInitEngine: " + flUnInitCode);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        tvStatus.setText("");
        if (isPrepared) {
            initCamera();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    protected void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                initEngine();
                initCamera();
            } else {
                showShortToast(getString(R.string.permission_denied));
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
            isPrepared = true;
        }
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }

            //??????FR?????????
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId, final Integer errorCode) {
                //FR??????
                if (faceFeature != null) {
                    Integer liveness = livenessMap.get(requestId);
                    //??????????????????????????????????????????
                    if (!livenessDetect) {
                        searchFace(faceFeature, requestId);
                    }
                    //?????????????????????????????????
                    else if (liveness != null && liveness == LivenessInfo.ALIVE) {
                        searchFace(faceFeature, requestId);
                    }
                    //??????????????????????????????????????????????????????????????????
                    else {
                        if (requestFeatureStatusMap.containsKey(requestId)) {
                            Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                    .subscribe(new Observer<Long>() {
                                        Disposable disposable;

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            disposable = d;
                                            getFeatureDelayedDisposables.add(disposable);
                                        }

                                        @Override
                                        public void onNext(Long aLong) {
                                            onFaceFeatureInfoGet(faceFeature, requestId, errorCode);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            getFeatureDelayedDisposables.remove(disposable);
                                        }
                                    });
                        }
                    }

                }
                //??????????????????
                else {
                    if (increaseAndGetValue(extractErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        extractErrorRetryMap.put(requestId, 0);

                        String msg;
                        // ?????????FaceInfo????????????????????????????????????????????????????????????RGB????????????????????????????????????
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = getString(R.string.low_confidence_level);
                        } else {
                            msg = "ExtractCode:" + errorCode;
                        }
                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
                        // ??????????????????????????????????????????????????????????????????????????????
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                        retryRecognizeDelayed(requestId);
                    } else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                    }
                }
            }

            @Override
            public void onFaceLivenessInfoGet(@Nullable LivenessInfo livenessInfo, final Integer requestId, Integer errorCode) {
                if (livenessInfo != null) {
                    int liveness = livenessInfo.getLiveness();
                    livenessMap.put(requestId, liveness);
                    // ??????????????????
                    if (liveness == LivenessInfo.NOT_ALIVE) {
                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_ALIVE"));
                        // ?????? FAIL_RETRY_INTERVAL ??????????????????????????????UNKNOWN????????????????????????????????????????????????
                        retryLivenessDetectDelayed(requestId);
                    }
                } else {
                    if (increaseAndGetValue(livenessErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        livenessErrorRetryMap.put(requestId, 0);
                        String msg;
                        // ?????????FaceInfo????????????????????????????????????????????????????????????RGB????????????????????????????????????
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = getString(R.string.low_confidence_level);
                        } else {
                            msg = "ProcessCode:" + errorCode;
                        }
                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
                        retryLivenessDetectDelayed(requestId);
                    } else {
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                    }
                }
            }


        };

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Camera.Size lastPreviewSize = previewSize;
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror, false, false);
                Log.i(TAG, "onCameraOpened: " + drawHelper.toString());
                // ????????????????????????????????????????????????????????????
                if (faceHelper == null ||
                        lastPreviewSize == null ||
                        lastPreviewSize.width != previewSize.width || lastPreviewSize.height != previewSize.height) {
                    Integer trackedFaceCount = null;
                    // ??????????????????????????????
                    if (faceHelper != null) {
                        trackedFaceCount = faceHelper.getTrackedFaceCount();
                        faceHelper.release();
                    }
                    faceHelper = new FaceHelper.Builder()
                            .ftEngine(ftEngine)
                            .frEngine(frEngine)
                            .flEngine(flEngine)
                            .frQueueSize(MAX_DETECT_NUM)
                            .flQueueSize(MAX_DETECT_NUM)
                            .previewSize(previewSize)
                            .faceListener(faceListener)
                            .trackedFaceCount(trackedFaceCount == null ? ConfigUtil.getTrackedFaceCount(RecognizeActivity.this.getApplicationContext()) : trackedFaceCount)
                            .build();
                }
            }

            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
                    drawPreviewInfo(facePreviewInfoList);
                }
                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        Integer status = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
                        /**
                         * ????????????????????????????????????????????????????????????????????????????????????????????????ANALYZING???????????????????????????ALIVE???NOT_ALIVE??????????????????????????????
                         */
                        if (livenessDetect && (status == null || status != RequestFeatureStatus.SUCCEED)) {
                            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
                            if (liveness == null
                                    || (liveness != LivenessInfo.ALIVE && liveness != LivenessInfo.NOT_ALIVE && liveness != RequestLivenessStatus.ANALYZING)) {
                                livenessMap.put(facePreviewInfoList.get(i).getTrackId(), RequestLivenessStatus.ANALYZING);
                                faceHelper.requestFaceLiveness(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId(), LivenessType.RGB);
                            }
                        }
                        /**
                         * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                         * ??????????????????????????????????????????{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer, Integer)}?????????
                         */
                        if (status == null
                                || status == RequestFeatureStatus.TO_RETRY) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }

    /**
     * ??????????????????
     * @param facePreviewInfoList
     */
    private void drawPreviewInfo(List<FacePreviewInfo> facePreviewInfoList) {
        for (int i = 0; i < facePreviewInfoList.size(); i++) {
            String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
            Integer recognizeStatus = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
            if (recognizeStatus != null) {
                if (recognizeStatus == RequestFeatureStatus.FAILED) {
                    tvStatus.setText(R.string.face_not_recognized);
                }
                if (recognizeStatus == RequestFeatureStatus.SUCCEED) {
                    tvStatus.setText(R.string.recognize_success);
                }
            } else {
                tvStatus.setText(R.string.face_not_recognized);
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param facePreviewInfoList ?????????trackId??????
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            livenessErrorRetryMap.clear();
            extractErrorRetryMap.clear();
            if (getFeatureDelayedDisposables != null) {
                getFeatureDelayedDisposables.clear();
            }
            return;
        }
        Enumeration<Integer> keys = requestFeatureStatusMap.keys();
        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == key) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(key);
                livenessMap.remove(key);
                livenessErrorRetryMap.remove(key);
                extractErrorRetryMap.remove(key);
            }
        }
    }

    /**
     * ???????????????????????????
     * @param frFace
     * @param requestId
     */
    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
                        CompareResult compareResult = ArcFaceServer.getInstance().getTopOfFaceLib(frFace);
                        emitter.onNext(compareResult);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            faceHelper.setName(requestId, "VISITOR " + requestId);
                            return;
                        }
                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                                faceHelper.setName(requestId, "VISITOR " + requestId);
                                return;
                            }
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded) {
                                //??????????????????????????????????????????????????? MAX_DETECT_NUM ??????????????????????????????????????????????????????
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
                                }
                                //?????????????????????????????????trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
                            }
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
                            faceHelper.setName(requestId, getString(R.string.recognize_success_notice, compareResult.getUserName()));
                            mPresenter.mateFace(compareResult, allCollectionList, guardianList);

                        } else {
                            /*kong*/
                            faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_REGISTERED"));
                            retryRecognizeDelayed(requestId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_REGISTERED"));
                        retryRecognizeDelayed(requestId);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * ???map???key?????????value???1??????
     *
     * @param countMap map
     * @param key      key
     * @return ???1??????value
     */
    public int increaseAndGetValue(Map<Integer, Integer> countMap, int key) {
        if (countMap == null) {
            return 0;
        }
        Integer value = countMap.get(key);
        if (value == null) {
            value = 0;
        }
        countMap.put(key, ++value);
        return value;
    }

    /**
     * ?????? FAIL_RETRY_INTERVAL ????????????????????????
     *
     * @param requestId ??????ID
     */
    private void retryLivenessDetectDelayed(final Integer requestId) {
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // ????????????????????????UNKNOWN????????????????????????????????????????????????
                        if (livenessDetect) {
                            faceHelper.setName(requestId, Integer.toString(requestId));
                        }
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }

    /**
     * ?????? FAIL_RETRY_INTERVAL ????????????????????????
     *
     * @param requestId ??????ID
     */
    private void retryRecognizeDelayed(final Integer requestId) {
        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // ????????????????????????????????????FAILED????????????????????????????????????????????????
                        faceHelper.setName(requestId, Integer.toString(requestId));
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.clear();
        }
        if (delayFaceTaskCompositeDisposable != null) {
            delayFaceTaskCompositeDisposable.clear();
        }
        if (faceHelper != null) {
            ConfigUtil.setTrackedFaceCount(this, faceHelper.getTrackedFaceCount());
            faceHelper.release();
            faceHelper = null;
        }

        ArcFaceServer.getInstance().unInit();
        super.onDestroy();
    }

    /*------??????????????????????????????????????????--------*/
    @Override
    public void faceResultSingStudent(List<QrCodeStudentCollection> modelResultList) {
        if (modelResultList.size() > 0) {
            /*???????????????????????????*/
            if (modelResultList.size() > 1) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.SIGN_TYPE, signType);
                bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardianList);
                bundle.putSerializable(Constant.STUDENT_MODEL_LIST, (Serializable) modelResultList);
                openActivity(StudentSignListSelectActivity.class, bundle);
            } else {
                QrCodeStudentCollection collection = modelResultList.get(0);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.TYPE, collection.getType());
                bundle.putSerializable(Constant.SIGN_TYPE, signType);
                bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardianList);
                bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, collection.getContentModel());
                openActivity(StudentSignActivity.class, bundle);
            }
        }
    }

    /*------??????????????????????????????????????????--------*/
    @Override
    public void faceResultSignStaff(SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TYPE, type);
        bundle.putSerializable(Constant.SIGN_TYPE, signType);
        bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardianList);
        bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
        openActivity(StaffSignActivity.class, bundle);
    }

    /*---------------------------------????????????????????????-----------------------------------*/
    /*????????????????????????*/
    public void startRotate() {
        imRotate.setVisibility(View.VISIBLE);
        imConnect.setVisibility(View.GONE);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setDuration(2000);//????????????????????????
        rotateAnimation.setRepeatCount(-1);//??????????????????
        rotateAnimation.setFillAfter(true);//???????????????????????????????????????????????????
        rotateAnimation.setStartOffset(10);//????????????????????????
        imRotate.setAnimation(rotateAnimation);
    }

    /*????????????????????????*/
    public void stopRotate() {
        imRotate.setVisibility(View.GONE);
        imConnect.setVisibility(View.VISIBLE);
        imRotate.clearAnimation();
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            rotateAnimation = null;
        }
    }

    /*????????????????????????*/
    @Override
    public void connectBluetooth() {
        /*??????????????????*/
        startRotate();
        /*????????????*/
        checkPermissions();
    }

    /*??????????????????*/
    @Override
    public void closeBluetooth() {
        /*???????????????????????????UI*/
        isCloseBluetooth = true;
        stopRotate();
        imConnect.setSelected(false);
        closeFastBluetooth();
    }

    /*????????????????????????*/
    @Override
    protected void bluetoothScanFinish() {
        /*???????????????????????????UI*/
        stopRotate();
        imConnect.setSelected(false);
        /*????????????????????????*/
        closeBluetoothPopup.showPopupWindow();
    }

    /*????????????????????????UI*/
    @Override
    public void bluetoothConnectSuccess() {
        stopRotate();
        imConnect.setSelected(true);
    }

    /*????????????????????????UI*/
    @Override
    protected void bluetoothConnectFail() {
        stopRotate();
        imConnect.setSelected(true);
    }

    /*????????????????????????UI*/
    @Override
    protected void bluetoothDisConnect() {
        /*????????????????????????????????????????????????????????????*/
        stopRotate();
        imConnect.setSelected(false);
        if (isVisible&&!isCloseBluetooth){
            bluetoothConnectFailPopup.showPopupWindow();
        }
        isCloseBluetooth = false;
    }

    /*??????????????????????????????????????????????????????*/
    @Override
    protected void bluetoothActiveDisConnected() {
        if (changeModel!=null){
            stopRotate();
            changeConnectBluetooth(changeModel);
        }
    }

    /*????????????????????????*/
    @Override
    protected void bluetoothDeviceList(List<FastBluetoothModel> resultModelList) {
        super.bluetoothDeviceList(resultModelList);
        closeBluetoothPopup.setDateList(resultModelList);
    }

    /*??????????????????*/
    @Override
    public void changeConnectDevice(FastBluetoothModel model) {
        changeModel = model;
        /*??????????????????/????????????/?????????????????????????????????*/
        closeBluetoothPopup.dismiss();
        /*?????????????????????????????????(?????????????????????????????????)*/
        if (hasConnect){
            /*?????????????????????????????????(?????????????????????????????????)*/
            disConnectFastBluetooth();
        }else {
            if (changeModel!=null){
                stopRotate();
                changeConnectBluetooth(changeModel);
            }
        }
    }

}
