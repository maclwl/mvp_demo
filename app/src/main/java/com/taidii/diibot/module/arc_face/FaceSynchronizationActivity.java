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
 * ????????????????????????????????????????????????
 */
public class FaceSynchronizationActivity extends BasePermissionActivity<FaceSynchronizationPresenter> implements FaceSynchronizationContract.View,FaceInfoCollectionAdapter.ItemClickListener {

    @BindView(R.id.rel_top_bar)
    RelativeLayout relTopBar;
    @BindView(R.id.person_face_list)
    RecyclerView personFaceList;

    /*??????SDK??????*/
    private static final String TAG = "FaceSynchronization";
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    // ???????????????????????????
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    // ????????????????????????
    private static final String[] LIBRARIES = new String[]{
            // ????????????
            "libarcsoft_face_engine.so",
            "libarcsoft_face.so",
            // ???????????????
            "libarcsoft_image_util.so",
    };
    /*??????????????????*/
    private List<GuardiansBean> guardianList = new ArrayList<>();//????????????
    private List<MainSchoolCollectionModel> allCollectionList = new ArrayList<>();//????????????????????????

    /*??????Adapter*/
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
        /*????????????*/
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
        /*??????????????????*/
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
        /*???????????????*/
        mPresenter.initFaceList(allCollectionList,guardianList);
        faceSynchronizationPopup = new FaceSynchronizationPopup(this);
    }

    /*??????????????????????????????????????????*/
    private void faceSynchronization(){
        /*????????????????????????????????????????????????*/
        ArcFaceServer.getInstance().init(this);
        /*?????????????????????????????????????????????????????? / ?????????token ???????????????????????? ???????????????*/
        mPresenter.getToken(allCollectionList,guardianList);
    }

    /*???????????????????????????????????????*/
    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 10:
                mPresenter.initFaceList(allCollectionList,guardianList);
                break;
        }
    }

    /**
     * ??????????????????????????????SDK??????
     */
    @OnClick({R.id.ll_syn, R.id.im_back})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.ll_syn:
                /*???????????????????????????????????????*/
                activeEngine(v);
                break;
            case R.id.im_back:
                finish();
                break;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param libraries ????????????????????????
     * @return ?????????????????????
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

    /* ????????????*/
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
                            /*??????????????????*/
                            faceSynchronization();
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            SharePrefUtils.saveBoolean(Constant.FACE_ACTIVE,true);
                            Log.i(TAG, "onNext: -----"+getString(R.string.already_activated));
                            /*??????????????????*/
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

    /*??????????????????*/
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

    /*????????????*/
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
