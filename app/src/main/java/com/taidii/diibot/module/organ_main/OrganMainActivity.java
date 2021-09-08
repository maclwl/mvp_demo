package com.taidii.diibot.module.organ_main;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.organ.ClassOrderBean;
import com.taidii.diibot.entity.organ.MainOrganCollectionModel;
import com.taidii.diibot.entity.organ.StudentsBean;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.module.login.LoginActivity;
import com.taidii.diibot.module.organ_main.adapter.MainOrganCollectionAdapter;
import com.taidii.diibot.module.organ_main.contract.OrganMainContract;
import com.taidii.diibot.module.organ_main.presenter.OrganMainPresenter;
import com.taidii.diibot.module.school_main.BaseManualBluetoothActivity;
import com.taidii.diibot.utils.TimerUtils;
import com.taidii.diibot.view.titlebar.OrganMainTopBarLayout;
import com.taidii.diibot.widget.popup.BluetoothConnectFailPopup;
import com.taidii.diibot.widget.popup.BluetoothTempPopup;
import com.taidii.diibot.widget.popup.CloseBluetoothPopup;
import com.taidii.diibot.widget.popup.LoginOutPopup;
import com.taidii.diibot.widget.popup.OrganSettingPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class OrganMainActivity extends BaseManualBluetoothActivity<OrganMainPresenter> implements QRCodeView.Delegate,TimerUtils.TimerCountListener
            , LoginOutPopup.LoginOutListener,BluetoothTempPopup.BluetoothTempListener,CloseBluetoothPopup.CloseBluetoothListener, OrganMainContract.View
            , MainOrganCollectionAdapter.ItemClickListener{

    @BindView(R.id.organ_list)
    RecyclerView organList;
    @BindView(R.id.rel_class)
    RelativeLayout relClass;
    @BindView(R.id.z_bar_view)
    ZBarView mZBarView;
    @BindView(R.id.rel_scanning)
    RelativeLayout relScanning;
    @BindView(R.id.rel_scan_result)
    RelativeLayout relScanResult;
    @BindView(R.id.rel_scan)
    RelativeLayout relScan;
    @BindView(R.id.main_top_bar)
    OrganMainTopBarLayout mainTopBar;

    private boolean isCloseBluetooth;//是否关闭蓝牙
    private TimerUtils timerUtils;//扫码超时倒计时
    private boolean isVisible = false;//是否可见
    private FastBluetoothModel changeModel;//切换连接蓝牙设备
    private LoginOutPopup loginOutPopup;//退出登录弹窗
    private OrganSettingPopup organSettingPopup;//设置弹窗
    private CloseBluetoothPopup closeBluetoothPopup;//关闭蓝牙
    private BluetoothTempPopup bluetoothTempPopup;//蓝牙测体温弹窗
    protected BluetoothConnectFailPopup bluetoothConnectFailPopup;//蓝牙断开连接
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;//扫码后置摄像头
    private MainOrganCollectionAdapter mainOrganCollectionAdapter;//首页列表Adapter
    private OrganMainTopBarLayout.ChangeMainEnum statusType =  OrganMainTopBarLayout.ChangeMainEnum.CLASS;//默认为机构列表模式

    @Override
    protected OrganMainPresenter createPresenter() {
        return new OrganMainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organ_main;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void initView() {
        initListener();

        mainOrganCollectionAdapter = new MainOrganCollectionAdapter(this);
        organList.setLayoutManager(new LinearLayoutManager(this));
        organList.setAdapter(mainOrganCollectionAdapter);
        /*优化嵌套卡顿*/
        organList.setHasFixedSize(true);
        organList.setNestedScrollingEnabled(false);
        organList.setItemViewCacheSize(600);
        RecyclerView.RecycledViewPool recycledViewPool = new
                RecyclerView.RecycledViewPool();
        organList.setRecycledViewPool(recycledViewPool);

        /*默认显示机构列表*/
        mainTopBar.setDefault();
        /*扫码代理*/
        mZBarView.setDelegate(this);
    }

    @Override
    protected void init() {
        super.init();
        timerUtils = new TimerUtils(this);
        loginOutPopup = new LoginOutPopup(this, this);
        loginOutPopup.setTitle(this.getResources().getString(R.string.login_out));
        organSettingPopup = new OrganSettingPopup(this);
        bluetoothTempPopup = new BluetoothTempPopup(this,this);
        closeBluetoothPopup = new CloseBluetoothPopup(this,this);
        bluetoothConnectFailPopup = new BluetoothConnectFailPopup(this);
    }

    @Override
    protected void initData() {
        super.initData();
        /*获取des解密 key信息*/
        mPresenter.getDesInfo();
        /*获取机构信息*/
        mPresenter.getLocalOrganData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        /*判断当前模式，如果是扫码，则开启二维码扫码*/
        switch (statusType){
            case SCAN:
                goToScanning();
                break;
        }
    }

    /*top_bar监听*/
    private void initListener(){
        mainTopBar.setOrganMainTopBarListener(new OrganMainTopBarLayout.OrganMainTopBarListener() {
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
            public void clickClassScan(OrganMainTopBarLayout.ChangeMainEnum status) {
                statusType = status;
                switch (status) {
                    case CLASS:
                        relClass.setVisibility(View.VISIBLE);
                        relScan.setVisibility(View.GONE);
                        /*关闭扫码*/
                        closeScan();
                        break;
                    case SCAN:
                        relClass.setVisibility(View.GONE);
                        relScan.setVisibility(View.VISIBLE);
                        /*开启扫码*/
                        goToScanning();
                        break;
                }
            }

            @Override
            public void clickSetting() {
                organSettingPopup.showPopupWindow();
            }

            @Override
            public void clickRefresh() {
                /*刷新列表*/
                mPresenter.getLocalOrganData();
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
        });
    }

    /*tabBar展示内容赋值*/
    @Override
    public void topBarInfo(String center_name, String center_logo) {
        mainTopBar.setCenterInfo(this, center_logo, center_name);
    }

    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
        /*签到刷新*/
        switch (event.getCode()){
            case 6:
                /*签到刷新列表*/
                StudentsBean studentsBean = (StudentsBean) event.getData();
                mPresenter.signBackRefresh(mainOrganCollectionAdapter.getDataList(), studentsBean);
                break;
        }
    }

    /*网络返回首页列表数据*/
    @Override
    public void organCollection(List<MainOrganCollectionModel> classModelList) {
        mainOrganCollectionAdapter.setDataList(classModelList);
    }

    /*考勤签到成功刷新列表*/
    @Override
    public void signRefreshBack(List<MainOrganCollectionModel> classModelList) {
        mainOrganCollectionAdapter.notifySetChanged(classModelList);
    }

    /*item监听跳转*/
    @Override
    public void clickChildContentItem(StudentsBean studentsBean, ClassOrderBean classOrder) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORGAN_CONTENT_MODEL,studentsBean);
        bundle.putSerializable(Constant.ORGAN_CLASS_ORDER,classOrder);
        openActivity(OrganDetailSignActivity.class,bundle);
    }

    /*扫码查学生*/
    @Override
    public void qrStudent(StudentsBean qrStudent, ClassOrderBean classOrder) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORGAN_CONTENT_MODEL,qrStudent);
        bundle.putSerializable(Constant.ORGAN_CLASS_ORDER,classOrder);
        openActivity(OrganDetailSignActivity.class,bundle);
    }

    /*退出登录*/
    @Override
    public void loginOut() {
        openActivity(LoginActivity.class);
        finish();
    }

    /*------------------------扫码相关------------------------------*/
    @OnClick(R.id.im_go_scan)
    public void onViewClicked() {
        /*重新扫码*/
        goToScanning();
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
        mPresenter.decrypt(result,mainOrganCollectionAdapter.getDataList());
        vibrate();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) { }

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
        /*取消旋转动画，刷新UI*/
        mainTopBar.stopRotate();
        mainTopBar.setConnectStatus(false);
        /*展示蓝牙设备列表*/
        closeBluetoothPopup.showPopupWindow();
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

    /*蓝牙断开连接刷新UI*/
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
