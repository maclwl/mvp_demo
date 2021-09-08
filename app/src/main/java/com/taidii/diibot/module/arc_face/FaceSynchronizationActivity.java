package com.taidii.diibot.module.arc_face;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.RuntimeABI;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BasePermissionActivity;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.module.arc_face.adapter.FaceInfoCollectionAdapter;
import com.taidii.diibot.module.arc_face.contract.FaceSynchronizationContract;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.module.arc_face.presenter.FaceSynchronizationPresenter;
import com.taidii.diibot.utils.ConfigUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.widget.popup.FaceSynchronizationPopup;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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

import static com.arcsoft.face.enums.DetectFaceOrientPriority.ASF_OP_0_ONLY;
import static com.arcsoft.face.enums.DetectFaceOrientPriority.ASF_OP_ALL_OUT;

/**
 * 云端人脸库同步注册到本地人脸库中
 */
public class FaceSynchronizationActivity extends BasePermissionActivity<FaceSynchronizationPresenter> implements FaceSynchronizationContract.View,FaceInfoCollectionAdapter.ItemClickListener {

    @BindView(R.id.rel_top_bar)
    RelativeLayout relTopBar;
    @BindView(R.id.person_face_list)
    RecyclerView personFaceList;

    /*激活SDK相关*/
    private static final String TAG = "FaceSynchronization";
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    // 在线激活所需的权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    // 所需的动态库文件
    private static final String[] LIBRARIES = new String[]{
            // 人脸相关
            "libarcsoft_face_engine.so",
            "libarcsoft_face.so",
            // 图像库相关
            "libarcsoft_image_util.so",
    };
    /*跳转传值数据*/
    private List<GuardiansBean> guardianList = new ArrayList<>();//接送家长
    private List<MainSchoolCollectionModel> allCollectionList = new ArrayList<>();//本地学生相关数据

    /*列表Adapter*/
    private FaceInfoCollectionAdapter faceInfoCollectionAdapter;

    private FaceSynchronizationPopup faceSynchronizationPopup;

    @Override
    protected FaceSynchronizationPresenter createPresenter() {
        return new FaceSynchronizationPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libraryExists = checkSoFile(LIBRARIES);
        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.i(TAG, "onCreate: " + applicationInfo.nativeLibraryDir);
        if (!libraryExists) {
            showShortToast(getString(R.string.library_not_found));
        }else {
            VersionInfo versionInfo = new VersionInfo();
            int code = FaceEngine.getVersion(versionInfo);
            Log.i(TAG, "onCreate: getVersion, code is: " + code + ", versionInfo is: " + versionInfo);
        }
        /*默认角度*/
        ConfigUtil.setFtOrient(this, ASF_OP_ALL_OUT);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_synchronization;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        faceInfoCollectionAdapter = new FaceInfoCollectionAdapter(this);
        personFaceList.setLayoutManager(new LinearLayoutManager(this));
        personFaceList.setAdapter(faceInfoCollectionAdapter);
        /*优化嵌套卡顿*/
        personFaceList.setHasFixedSize(true);
        personFaceList.setNestedScrollingEnabled(false);
        personFaceList.setItemViewCacheSize(600);
        RecyclerView.RecycledViewPool recycledViewPool = new
                RecyclerView.RecycledViewPool();
        personFaceList.setRecycledViewPool(recycledViewPool);
    }

    @Override
    protected void initData() {
        guardianList = (List<GuardiansBean>) getIntent().getSerializableExtra(Constant.GUARDIANS_LIST);
        allCollectionList = (List<MainSchoolCollectionModel>) getIntent().getSerializableExtra(Constant.DATA);
        /*初始化列表*/
        mPresenter.initFaceList(allCollectionList,guardianList);
        faceSynchronizationPopup = new FaceSynchronizationPopup(this);
    }

    /*同步注册人脸库并匹配刷新列表*/
    private void faceSynchronization(){
        /*先初始本地人脸库，才可用人脸注册*/
        ArcFaceServer.getInstance().init(this);
        /*获取云端人脸库数据同步本地并刷新列表 / 先获取token 再获取云端人脸库 注册到本地*/
        mPresenter.getToken(allCollectionList,guardianList);
    }

    /*本地采集注册人脸，刷新列表*/
    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 10:
                mPresenter.initFaceList(allCollectionList,guardianList);
                break;
        }
    }

    /**
     * 同步人脸数据时先进行SDK激活
     */
    @OnClick({R.id.ll_syn, R.id.im_back})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.ll_syn:
                /*先激活在进行人脸库同步数据*/
                activeEngine(v);
                break;
            case R.id.im_back:
                finish();
                break;
        }
    }

    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }

    @Override
    protected void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                activeEngine(null);
            } else {
                showLongToast(getString(R.string.permission_denied_active));
            }
        }
    }

    /* 激活引擎*/
    private void activeEngine(final View view) {
        if (!libraryExists) {
            showShortToast(getString(R.string.library_not_found));
            return;
        }
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();
                Log.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);
                long start = System.currentTimeMillis();
                int activeCode = FaceEngine.activeOnline(FaceSynchronizationActivity.this, Constant.APP_ID, Constant.SDK_KEY);
                Log.i(TAG, "subscribe cost: " + (System.currentTimeMillis() - start));
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            SharePrefUtils.saveBoolean(Constant.FACE_ACTIVE,true);
                            Log.i(TAG, "onNext: -----"+getString(R.string.active_success));
                            /*同步人脸数据*/
                            faceSynchronization();
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            SharePrefUtils.saveBoolean(Constant.FACE_ACTIVE,true);
                            Log.i(TAG, "onNext: -----"+getString(R.string.already_activated));
                            /*同步人脸数据*/
                            faceSynchronization();
                        } else {
                            SharePrefUtils.saveBoolean(Constant.FACE_ACTIVE,false);
                            showShortToast(getString(R.string.active_failed, activeCode));
                        }
                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(FaceSynchronizationActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showShortToast(e.getMessage());
                        if (view != null) {
                            view.setClickable(true);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*刷新列表数据*/
    @Override
    public void refreshChangeList(List<MainSchoolCollectionModel> allCollectionList) {
        faceInfoCollectionAdapter.setDataList(allCollectionList);
    }

    @Override
    public void progress(int progress) {
        faceSynchronizationPopup.incrementProgress(progress);
    }

    @Override
    public void initProgress(int max) {
        faceSynchronizationPopup.initNumMax(max);
        faceSynchronizationPopup.showPopupWindow();
    }

    /*跳转详情*/
    @Override
    public void clickChildContentItem(SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel) {
        if (SharePrefUtils.getBoolean(Constant.FACE_ACTIVE)){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.TYPE, type);
            bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardianList);
            bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
            openActivity(FaceInfoDetailActivity.class,bundle);
        }else {
            showLongToast(R.string.hint_active);
        }
    }
}
