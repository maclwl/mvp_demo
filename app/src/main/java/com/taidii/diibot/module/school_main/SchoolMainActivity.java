package com.taidii.diibot.module.school_main;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.entity.enums.NetworkType;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.module.arc_face.FaceSynchronizationActivity;
import com.taidii.diibot.module.arc_face.RecognizeActivity;
import com.taidii.diibot.module.arc_face.faceserver.ArcFaceServer;
import com.taidii.diibot.module.class_card.ClassCardPageOneActivity;
import com.taidii.diibot.module.login.LoginActivity;
import com.taidii.diibot.module.privacy.CustomWebActivity;
import com.taidii.diibot.module.school_main.adapter.MainSchoolCollectionAdapter;
import com.taidii.diibot.module.school_main.contract.SchoolMainContract;
import com.taidii.diibot.module.school_main.presenter.SchoolMainPresenter;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.service.FaceRegisterService;
import com.taidii.diibot.utils.AgreementUtils;
import com.taidii.diibot.utils.DataHolder;
import com.taidii.diibot.utils.LocalOffLineDataUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.TimerUtils;
import com.taidii.diibot.utils.network.NetworkUtil;
import com.taidii.diibot.view.titlebar.MainTopBarLayout;
import com.taidii.diibot.view.titlebar.MainTopTabLayout;
import com.taidii.diibot.widget.popup.BluetoothConnectFailPopup;
import com.taidii.diibot.widget.popup.BluetoothTempPopup;
import com.taidii.diibot.widget.popup.CloseBluetoothPopup;
import com.taidii.diibot.widget.popup.LoginOutPopup;
import com.taidii.diibot.widget.popup.NoDataInputPopup;
import com.taidii.diibot.widget.popup.SearchPopup;
import com.taidii.diibot.widget.popup.SettingPopup;
import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import razerdp.basepopup.BasePopupWindow;

public class SchoolMainActivity extends BaseManualBluetoothActivity<SchoolMainPresenter> implements SchoolMainContract.View,CloseBluetoothPopup.CloseBluetoothListener
        , MainSchoolCollectionAdapter.ItemClickListener, LoginOutPopup.LoginOutListener, SettingPopup.SettingPopupListener
        , QRCodeView.Delegate, TimerUtils.TimerCountListener,BluetoothTempPopup.BluetoothTempListener,SearchPopup.SearchPopupListener{

    @BindView(R.id.main_top_bar)
    MainTopBarLayout mainTopBar;
    @BindView(R.id.class_list)
    RecyclerView classList;
    @BindView(R.id.rel_class)
    RelativeLayout relClass;
    @BindView(R.id.rel_scan)
    RelativeLayout relScan;
    @BindView(R.id.main_tab)
    MainTopTabLayout mainTab;
    @BindView(R.id.ll_camera_change)
    LinearLayout llCameraChange;
    @BindView(R.id.z_bar_view)
    ZBarView mZBarView;
    @BindView(R.id.rel_scanning)
    RelativeLayout relScanning;
    @BindView(R.id.rel_scan_result)
    RelativeLayout relScanResult;

    private boolean isCloseBluetooth;//是否关闭蓝牙
    private TimerUtils timerUtils;//扫码超时倒计时
    private SingTypeEnum signType;//签到类型
    private boolean isVisible = false;//是否可见
    private int contentPosition;//item监听内层列表position
    private int collectionPosition;//item监听外层列表position
    private FastBluetoothModel changeModel;//切换连接蓝牙设备
    private SearchPopup searchPopup;//搜索弹窗
    private SettingPopup settingPopup;//设置弹窗
    private LoginOutPopup loginOutPopup;//退出登录弹窗
    private CloseBluetoothPopup closeBluetoothPopup;//关闭蓝牙
    private NoDataInputPopup noDataInputPopup;//没有离线数据上传弹窗
    private BluetoothTempPopup bluetoothTempPopup;//蓝牙测体温弹窗
    protected BluetoothConnectFailPopup bluetoothConnectFailPopup;//蓝牙断开连接
    private MainSchoolCollectionAdapter mainSchoolCollectionAdapter;
    private List<GuardiansBean> guardiansList = new ArrayList<>();//接送家长

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;//扫码后置摄像头
    private List<MainSchoolCollectionModel> allCollectionList = new ArrayList<>();//列表所有数据（包含老师）
    private MainTopBarLayout.ChangeMainEnum statusType =  MainTopBarLayout.ChangeMainEnum.SCAN;//默认为扫码模式
    /*搜索弹窗输入框相关设置*/
    public boolean adjust = true;
    public boolean autoOpen = true;
    public int alignMode = BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT | BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST;

    /*传值给人脸识别*/
    private String center_name;
    private String center_logo;

    /*蓝牙相关*/
    private List<FastBluetoothModel> resultModelList = new ArrayList<>();

    @Override
    protected SchoolMainPresenter createPresenter() {
        return new SchoolMainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_school_main;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        initListener();
        mainSchoolCollectionAdapter = new MainSchoolCollectionAdapter(this);
        classList.setLayoutManager(new LinearLayoutManager(this));
        classList.setAdapter(mainSchoolCollectionAdapter);
        /*优化嵌套卡顿*/
        classList.setHasFixedSize(true);
        classList.setNestedScrollingEnabled(false);
        classList.setItemViewCacheSize(600);
        RecyclerView.RecycledViewPool recycledViewPool = new
                RecyclerView.RecycledViewPool();
        classList.setRecycledViewPool(recycledViewPool);

        /*默认第一个tab*/
        mainTab.setDefault();
        /*默认显示扫码*/
        mainTopBar.setDefault();
        /*隐藏体温tab*/
        mainTab.changeScan();
        signType = SingTypeEnum.SIGN_IN;
        /*默认显示切换摄像头*/
        llCameraChange.setVisibility(View.VISIBLE);
        /*扫码代理*/
        mZBarView.setDelegate(this);

        getClassList();
    }

    private void getClassList() {
        String url = String.format(ApiContainer.CLASS_CLASS_LIST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
//        String url = ApiContainer.CLASS_CLASS_LIST;
        ArrayMap<String, String> params = new ArrayMap<>();
        OKHttpUtils.get(url, params,this,new OKHttpUtils.OnResponse<ClassCardClassListBean>() {

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onSuccess(ClassCardClassListBean result) {
                if(result!=null&&result.getStatus()==1){
                    GlobalParams.mClassList.clear();
                    GlobalParams.mClassList.addAll(result.getData());
                }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void init() {
        super.init();
        timerUtils = new TimerUtils(this);
        searchPopup = new SearchPopup(this,this);
        searchPopup.setAdjustInputMethod(adjust)
                .setAutoShowInputMethod(searchPopup.findViewById(R.id.edit_search), autoOpen)
                .setAdjustInputMode(R.id.edit_search, alignMode);
        settingPopup = new SettingPopup(this, this);
        loginOutPopup = new LoginOutPopup(this, this);
        loginOutPopup.setTitle(this.getResources().getString(R.string.login_out));
        bluetoothTempPopup = new BluetoothTempPopup(this,this);
        noDataInputPopup = new NoDataInputPopup(this);
        closeBluetoothPopup = new CloseBluetoothPopup(this,this);
        bluetoothConnectFailPopup = new BluetoothConnectFailPopup(this);
    }

    @Override
    protected void initData() {
        /*获取des解密 key信息*/
        mPresenter.getDesInfo();
        /*如果有本地离线数据，则走 onResume（上传完之后再获取首页学校数据），反之则直接获取*/
        if (!LocalOffLineDataUtil.isExistOffLineData()) {
            mPresenter.getLocalSchoolData(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD));
        }

        ArcFaceServer.getInstance().init(this);
        /*同步本地人脸库*/
        //registerFace();
    }

    private void registerFace() {
        Intent intent = new Intent(act, FaceRegisterService.class);
        act.startService(intent);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        /*有网则请求*/
        NetworkType mType = NetworkUtil.getNetworkType(BaseApplication.getInstance());
        if (mType == NetworkType.NETWORK_NO) {
            /*无网络则不处理*/
        } else {
            if (LocalOffLineDataUtil.isExistOffLineData()) {
                localOffLineInput();
            }
        }
        /*离线数据UI状态*/
        changeInputTipUI();
        /*判断当前模式，如果是扫码，则开启二维码扫码*/
        switch (statusType){
            case SCAN:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToScanning();
                    }
                },300);
                break;
        }
    }

    /*网络恢复连接*/
    @Override
    protected void netConnect() {
        if (LocalOffLineDataUtil.isExistOffLineData()) {
            localOffLineInput();
        }
    }

    /*无网络状态离线考勤，图片数据上传（1.显示主页且有网络状态下上传;2.网络连接恢复正常上传）*/
    private void localOffLineInput() {
        /*本地数据库中是否存在有离线的考勤数据*/
        mPresenter.localOffLineInput();
        /*开始上传动画*/
        mainTopBar.startInputAnim();
    }

    /*view点击监听事件*/
    private void initListener() {
        /*top_tab*/
        mainTab.setMainTabListener(new MainTopTabLayout.MainTabListener() {
            @Override
            public void tabSelect(SingTypeEnum type) {
                signType = type;
            }
        });
        /*top_bar*/
        mainTopBar.setMainTopBarListener(new MainTopBarLayout.MainTopBarListener() {
            @Override
            public void clickLogo() {
                /*退出登录弹窗*/
                loginOutPopup.showPopupWindow();
            }

            @Override
            public void clickContent() {
                /*蓝牙测体温---->判断是否已连接，是否打开蓝牙*/
                if (!hasConnect){
                    /*判断是否打开蓝牙*/
                    if (isBtEnable()){
                        /*扫描蓝牙*/
                        bluetoothTempPopup.showPopupWindow();
                    }else {
                        /*打开蓝牙*/
                        BleManager.getInstance().enableBluetooth();
                    }
                }else {
                    /*蓝牙设备切换*/
                    closeBluetoothPopup.showPopupWindow();
                }
            }

            @Override
            public void clickClassScan(MainTopBarLayout.ChangeMainEnum status) {
                statusType = status;
                switch (status) {
                    case CLASS:
                        relClass.setVisibility(View.VISIBLE);
                        relScan.setVisibility(View.GONE);
                        /*显示体温tab*/
                        mainTab.changeClass();
                        mainTab.setDefault();
                        signType = SingTypeEnum.SIGN_IN;
                        llCameraChange.setVisibility(View.GONE);
                        /*关闭扫码*/
                        closeScan();
                        break;
                    case SCAN:
                        relClass.setVisibility(View.GONE);
                        relScan.setVisibility(View.VISIBLE);
                        /*隐藏体温tab*/
                        mainTab.changeScan();
                        mainTab.setDefault();
                        signType = SingTypeEnum.SIGN_IN;
                        llCameraChange.setVisibility(View.VISIBLE);
                        /*开启扫码*/
                        goToScanning();
                        break;
                }
            }

            @Override
            public void clickVisitor() {
                /*新增游客*/
                openActivity(VisitorListActivity.class);
            }

            @Override
            public void clickSetting() {
                /*设置弹窗*/
                settingPopup.showPopupWindow();
            }

            @Override
            public void clickRefresh() {
                /*刷新列表数据*/
                mPresenter.getLocalSchoolData(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD));
            }

            @Override
            public void input() {
                /*离线文件上传*/
                noDataInputPopup.showPopupWindow();
            }

            @Override
            public void closeBluetooth() {
                /*正在扫描和正在连接蓝牙不做操作*/
                if (isScanning){
                    showLongToast(R.string.scanning_device);
                }else if (isConnecting){
                    showLongToast(R.string.connecting_device);
                }else {
                    /*关闭蓝牙设备*/
                    bluetoothTempPopup.showPopupWindow();
                }
            }

            @Override
            public void search() {
                /*搜索功能*/
                searchPopup.showPopupWindow();
            }

            @Override
            public void closeSearch() {
                /*关闭搜索结果及搜索功能刷新列表*/
                mainTopBar.setChangeScreen();
                mPresenter.initSettingList(allCollectionList);
            }

            @Override
            public void faceRecognize() {
                /*跳转人脸考勤*/
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.DATA, (Serializable) allCollectionList);
                bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
                bundle.putString(Constant.NAME,center_name);
                bundle.putString(Constant.AVATAR,center_logo);
                bundle.putBoolean(Constant.HAS_CONNECT,hasConnect);
                openActivity(RecognizeActivity.class,bundle);
                /*蓝牙结果*/
                DataHolder.getInstance().setData(Constant.BLUETOOTH_DATA,resultModelList);
                /*关闭扫码(不然导致人脸中占用摄像头)*/
                closeScan();
            }
        });
    }

    /*tabBar展示内容赋值*/
    @Override
    public void topBarInfo(String center_name, String center_logo) {
        this.center_name = center_name;
        this.center_logo = center_logo;
        mainTopBar.setCenterInfo(this, center_logo, center_name);
    }

    /*是否有离线数据，刷新提示UI*/
    private void changeInputTipUI() {
        if (LocalOffLineDataUtil.isExistOffLineData()) {
            mainTopBar.setImInputTipChange(true);
        } else {
            mainTopBar.setImInputTipChange(false);
        }
    }

    /*接送家长列表*/
    @Override
    public void guardiansList(List<GuardiansBean> guardiansList) {
        this.guardiansList = guardiansList;
    }

    /*网络获取列表所有展示数据返回*/
    @Override
    public void schoolCollection(List<MainSchoolCollectionModel> classModelList) {
        allCollectionList = classModelList;
        /*初始列表设置*/
        mPresenter.initSettingList(classModelList);
        /*离线UI刷新*/
        changeInputTipUI();
        /*结束动画*/
        mainTopBar.stopInputAnim();
    }

    /*剔除老师考勤返回列表*/
    @Override
    public void settingCollectionResult(List<MainSchoolCollectionModel> classModelList) {
        mainSchoolCollectionAdapter.setDataList(classModelList);
        if(SharePrefUtils.getBoolean(Constant.IS_HIDE_SIGN_IN)){
            mPresenter.hideSignInSetting(allCollectionList);
        }
    }

    /*签到成功刷新列表UI*/
    @Override
    public void signBackRefreshList(List<MainSchoolCollectionModel> signCollectionModelList) {
        mainSchoolCollectionAdapter.notifySetChanged(signCollectionModelList);
        if(SharePrefUtils.getBoolean(Constant.IS_HIDE_SIGN_IN)){
            mPresenter.hideSignInSetting(allCollectionList);
        }
        mPresenter.getLocalSchoolDataCount(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD));
    }

    /*隐藏已签到学生UI*/
    @Override
    public void hintSignInList(List<MainSchoolCollectionModel> signCollectionModelList) {
        mainSchoolCollectionAdapter.notifySetChanged(signCollectionModelList);
    }

    /*搜索学生监听*/
    @Override
    public void search(String keyword) {
        /*匹配搜索处理*/
        mPresenter.mateSearch(keyword,mainSchoolCollectionAdapter.getDataList());
    }

    /*搜索匹配结果刷新列表*/
    @Override
    public void mateSearchResult(List<MainSchoolCollectionModel> resultModelList,String keyword) {
        /*判断是否匹配到结果*/
        if (resultModelList.size()>0){
            mainTopBar.setChangeSearch(keyword);
            mainSchoolCollectionAdapter.setDataList(resultModelList);
        }else {
            showMessage(R.string.search_none);
        }

    }

    /*老师扫码签到，但未列表未打开老师考勤（刷新整个学校数据）*/
    @Override
    public void signBackAllCollectionModelList(List<MainSchoolCollectionModel> allCollectionList) {
        this.allCollectionList = allCollectionList;
    }

    /*列表点击跳转签到监听*/
    @Override
    public void clickChildContentItem(int collectionPosition, int contentPosition, SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel) {
        this.collectionPosition = collectionPosition;
        this.contentPosition = contentPosition;
        /*判断是否是快速签到*/
        if (SharePrefUtils.getBoolean(Constant.IS_QUICK_CHECK)) {
            mPresenter.quickSign(type, signType, mainSchoolContentModel);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.TYPE, type);
            bundle.putSerializable(Constant.SIGN_TYPE, signType);
            bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
            bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
            switch (type) {
                case BUSES:
                case CLASS:
                    openActivity(StudentSignActivity.class, bundle);
                    break;
                case STAFF:
                    openActivity(StaffSignActivity.class, bundle);
                    break;
            }
        }
    }

    /*------扫码结果学生处理跳转签到--------*/
    @Override
    public void qrResultSingStudent(List<QrCodeStudentCollection> modelResultList) {
        if (modelResultList.size()>0){
            /*判断是否有多个学生*/
            if (modelResultList.size()>1){
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.SIGN_TYPE, signType);
                bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
                bundle.putSerializable(Constant.STUDENT_MODEL_LIST, (Serializable) modelResultList);
                openActivity(StudentSignListSelectActivity.class, bundle);
            }else {
                QrCodeStudentCollection collection = modelResultList.get(0);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.TYPE, collection.getType());
                bundle.putSerializable(Constant.SIGN_TYPE, signType);
                bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
                bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, collection.getContentModel());
                openActivity(StudentSignActivity.class, bundle);
            }
        }
    }

    /*------扫码结果老师处理跳转签到--------*/
    @Override
    public void qrResultSignStaff(SchoolMainEnum type, MainSchoolContentModel mainSchoolContentModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TYPE, type);
        bundle.putSerializable(Constant.SIGN_TYPE, signType);
        bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
        bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
        openActivity(StaffSignActivity.class, bundle);
    }

    /*------设置出勤，离校人数--------*/
    @Override
    public void setTitleCount(int totalCount, int signInCount, int signOutCount) {
        mainTopBar.setAllSignInCount(totalCount,signInCount,signOutCount);
    }

    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
        /*签到刷新*/
        switch (event.getCode()){
            case 0:
                /*签到刷新列表*/
                MainSchoolContentModel signContentModel = (MainSchoolContentModel) event.getData();
                mPresenter.signBackRefresh(mainSchoolCollectionAdapter.getDataList(), collectionPosition, contentPosition, signContentModel,allCollectionList);
                break;
            case 3:
                hasConnect = true;
                /*人脸识别中蓝牙连接成功通知*/
                mainTopBar.setConnectStatus(true);
                break;
        }
    }

    /*列表快速签到成功，刷新列表数据*/
    @Override
    public void quickSignResult(MainSchoolContentModel mainSchoolContentModel) {
        mPresenter.signBackRefresh(mainSchoolCollectionAdapter.getDataList(), collectionPosition, contentPosition, mainSchoolContentModel,allCollectionList);
    }

    /*老师考勤开关监听回调*/
    @Override
    public void staffCheckSetting(boolean isCheck) {
        if (isCheck) {
            mainSchoolCollectionAdapter.setDataList(allCollectionList);
        } else {
            mPresenter.removeStaff(allCollectionList);
        }
    }

    /*是否展开全部开关回调*/
    @Override
    public void spreadCheckSetting(boolean isCheck) {
        mPresenter.spreadSetting(mainSchoolCollectionAdapter.getDataList(), isCheck);
    }

    /*隐私政策*/
    @Override
    public void previewPrivacy() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL,AgreementUtils.getPrivacyUrl(this));
        bundle.putString(Constant.TYPE,this.getResources().getString(R.string.txt_privacy_policy));
        openActivity(CustomWebActivity.class,bundle);
    }

    @Override
    public void classCardJump() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL,AgreementUtils.getPrivacyUrl(this));
        bundle.putString(Constant.TYPE,this.getResources().getString(R.string.txt_privacy_policy));
        openActivity(ClassCardPageOneActivity.class,bundle);
    }

    /*用户协议*/
    @Override
    public void previewAgreement() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL,AgreementUtils.getAgreementUrl(this));
        bundle.putString(Constant.TYPE,this.getResources().getString(R.string.txt_use_agreement));
        openActivity(CustomWebActivity.class,bundle);
    }

    /*跳转人脸库同步数据界面*/
    @Override
    public void faceSynchronization() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, (Serializable) allCollectionList);
        bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardiansList);
        openActivity(FaceSynchronizationActivity.class,bundle);
    }

    /*是否隐藏已出勤学生*/
    @Override
    public void hideSignInCheckSetting(boolean isCheck) {
        if (isCheck) {
            mPresenter.hideSignInSetting(mainSchoolCollectionAdapter.getDataList());
        } else {
            mPresenter.getLocalSchoolData(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD));
        }
    }

    /*退出登录*/
    @Override
    public void loginOut() {
        openActivity(LoginActivity.class);
        finish();
    }

    /*------------------------扫码相关------------------------------*/

    @OnClick({R.id.ll_camera_change, R.id.im_go_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_camera_change:
                /*切换扫码摄像头*/
                switch (mCameraId){
                    case Camera.CameraInfo.CAMERA_FACING_BACK:
                        mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        startChangeCamera(mCameraId);
                        break;
                    case Camera.CameraInfo.CAMERA_FACING_FRONT:
                        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        startChangeCamera(mCameraId);
                        break;
                }
                break;
            case R.id.im_go_scan:
                /*重新扫码*/
                goToScanning();
                break;
        }
    }

    /*重新打开扫码处理*/
    private void startChangeCamera(int mCameraId){
        mZBarView.stopCamera();
        mZBarView.startCamera(mCameraId);
        mZBarView.showScanRect();
        mZBarView.startSpot();
    }

    /*扫码UI相关处理*/
    private void goToScanning(){
        startChangeCamera(mCameraId);
        relScanning.setVisibility(View.VISIBLE);
        relScanResult.setVisibility(View.GONE);
        timerUtils.startTimer();
    }

    /*关闭扫码处理*/
    private void closeScan(){
        mZBarView.stopCamera();
        if (timerUtils!=null){
            timerUtils.cancelTimer();
        }
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
        isVisible = false;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        /*对字结果进行解密*/
        mPresenter.decrypt(result,allCollectionList,guardiansList);
        vibrate();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        /* 这里是通过修改提示文案来展示环境是否过暗的状态，
        接入方也可以根据 isDark 的值来实现其他交互效果*/
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showLongToast(R.string.camera_error);
    }

    /*扫码超时倒计时结束*/
    @Override
    public void timerFinish() {
        relScanResult.setVisibility(View.VISIBLE);
        relScanning.setVisibility(View.GONE);
        timerUtils.cancelTimer();
    }

    /*------------------------蓝牙体温测量相关-----------------------*/
    /*连接蓝牙弹窗监听*/
    @Override
    public void connectBluetooth() {
        /*开始旋转动画*/
        mainTopBar.startRotate();
        /*连接蓝牙*/
        checkPermissions();
    }

    /*关闭蓝牙连接*/
    @Override
    public void closeBluetooth() {
        /*取消旋转动画，刷新UI*/
        isCloseBluetooth = true;
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(false);
        closeFastBluetooth();
    }

    /*扫描蓝牙设备结束*/
    @Override
    protected void bluetoothScanFinish() {
        /* (自动连接处理 -》连接过设备判断，存在则自动连接)取消旋转动画，刷新UI*/
        mPresenter.checkConnectBluetooth(resultModelList);
    }

    /*蓝牙列表，手动连接*/
    @Override
    public void showBluetoothListPopup() {
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(false);
        /*展示蓝牙设备列表*/
        closeBluetoothPopup.showPopupWindow();
    }

    /*匹配到之前连过的蓝牙，自动连接*/
    @Override
    public void autoConnectionBluetooth(FastBluetoothModel model) {
        changeConnectBluetooth(model);
    }

    /*蓝牙连接成功刷新UI*/
    @Override
    public void bluetoothConnectSuccess() {
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(true);
    }

    /*蓝牙连接失败刷新UI*/
    @Override
    protected void bluetoothConnectFail() {
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(false);
    }

    /*蓝牙主动断开连接刷新UI*/
    @Override
    protected void bluetoothDisConnect() {
        /*判断是否是关闭蓝牙*/
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(false);
        if (isVisible&&!isCloseBluetooth){
            bluetoothConnectFailPopup.showPopupWindow();
        }
        isCloseBluetooth = false;
    }

    /*蓝牙手动断开连接：即切换连接蓝牙设备*/
    @Override
    protected void bluetoothActiveDisConnected() {
        if (changeModel!=null){
            mainTopBar.startRotate();
            changeConnectBluetooth(changeModel);
        }
    }

    /*扫描蓝牙设备列表*/
    @Override
    protected void bluetoothDeviceList(List<FastBluetoothModel> resultModelList) {
        super.bluetoothDeviceList(resultModelList);
        this.resultModelList = resultModelList;
        closeBluetoothPopup.setDateList(resultModelList);
    }

    /*重新连接设备*/
    @Override
    public void changeConnectDevice(FastBluetoothModel model) {
        changeModel = model;
        /*开始旋转动画/断开所有连接的蓝牙设备*/
        closeBluetoothPopup.dismiss();
        /*是否连接蓝牙设备，连接则先切断再连接，未连接则直接连接*/
        if (hasConnect){
            /*先关闭所有连接切换连接(字断开连接中做切货逻辑)*/
            disConnectFastBluetooth();
        }else {
            if (changeModel!=null){
                mainTopBar.startRotate();
                changeConnectBluetooth(changeModel);
            }
        }
    }

    @Override
    public void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
        timerUtils.release();//释放
    }

}
