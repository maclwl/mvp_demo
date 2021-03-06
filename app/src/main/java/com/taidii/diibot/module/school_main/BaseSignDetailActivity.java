package com.taidii.diibot.module.school_main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.gyf.immersionbar.ImmersionBar;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.enums.GeneralTypeEnum;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.module.school_main.adapter.GuardianAdapter;
import com.taidii.diibot.module.school_main.contract.SignContract;
import com.taidii.diibot.module.school_main.presenter.SignPresenter;
import com.taidii.diibot.utils.CameraThreadPool;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.FileUtil;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.MathUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.SignDateTimePickerUtil;
import com.taidii.diibot.widget.popup.BluetoothConnectFailPopup;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BaseSignDetailActivity extends BaseMvpActivity<SignPresenter> implements SignContract.View, GuardianAdapter.ItemClickListener
                ,SignDateTimePickerUtil.SignDateTimeListener{

    @BindView(R.id.rel_top)
    RelativeLayout relTop;
    @BindView(R.id.im_head)
    RoundedImageView imHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.im_sex)
    ImageView imSex;
    @BindView(R.id.tv_hours)
    TextView tvHours;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.guardians_list_view)
    RecyclerView guardiansListView;
    @BindView(R.id.ll_guardians)
    LinearLayout llGuardians;
    @BindView(R.id.im_health)
    ImageView imHealth;
    @BindView(R.id.seek_bar_temp)
    VerticalRangeSeekBar seekBarTemp;
    @BindView(R.id.tv_sign_type)
    TextView tvSignType;
    @BindView(R.id.rel_sign_type)
    RelativeLayout relSignType;
    @BindView(R.id.im_history)
    ImageView imHistory;
    @BindView(R.id.im_camera_change)
    ImageView imCameraChange;
    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.card_camera)
    CardView cardCamera;
    @BindView(R.id.im_sign)
    ImageView imSign;

    protected String createTime;//??????????????????
    protected SchoolMainEnum type;//????????????
    protected SingTypeEnum signType;//????????????
    protected GuardianAdapter guardianAdapter;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    protected SignDateTimePickerUtil signDateTimePickerUtil;//??????????????????
    protected MainSchoolContentModel mainSchoolContentModel;//?????????????????????
    protected List<GuardiansBean> guardianList = new ArrayList<>();//????????????
    protected BluetoothConnectFailPopup bluetoothConnectFailPopup;//??????????????????

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_detail;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .titleBar(relTop)
                .statusBarAlpha(0)
                .statusBarColor(R.color.themeWhite)
                .navigationBarColor(R.color.themeWhite)
                .init();
    }

    @Override
    protected void initView() {
        /*??????*/
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera.setLifecycleOwner(this);
        /*???????????????*/
        guardianAdapter = new GuardianAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        guardiansListView.setLayoutManager(linearLayoutManager);
        guardiansListView.setAdapter(guardianAdapter);
        guardiansListView.getItemAnimator().setAddDuration(0);
        guardiansListView.getItemAnimator().setChangeDuration(0);
        guardiansListView.getItemAnimator().setMoveDuration(0);
        guardiansListView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) guardiansListView.getItemAnimator()).setSupportsChangeAnimations(false);

        /*????????????*/
        tvHours.setText(DateUtil.getHour());
        tvMinute.setText(DateUtil.getTime());
        /*????????????????????????????????????????????????*/
        createTime = DateUtil.getStringDate();
        /*????????????????????????*/
        seekBarTemp.setRange(35.0f, 40.0f, 0.1f);
        seekBarTemp.setProgress(37.6f);
        initListener();
        /*??????????????????????????????*/
        if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)) {
            tvTemp.setText(R.string.default_temp);
        }
    }

    @Override
    protected void init() {
        super.init();
        bluetoothConnectFailPopup = new BluetoothConnectFailPopup(this);
        signDateTimePickerUtil = new SignDateTimePickerUtil(this);
        /*??????????????????*/
        type = (SchoolMainEnum) getIntent().getSerializableExtra(Constant.TYPE);
        signType = (SingTypeEnum) getIntent().getSerializableExtra(Constant.SIGN_TYPE);
        guardianList = (List<GuardiansBean>) getIntent().getSerializableExtra(Constant.GUARDIANS_LIST);
        mainSchoolContentModel = (MainSchoolContentModel) getIntent().getSerializableExtra(Constant.SCHOOL_CONTENT_MODEL);
        if (type != null) {
            initChangeUI(type, signType);
        }
    }

    private void initListener() {
        /*??????????????????*/
        seekBarTemp.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                tvTemp.setText(MathUtil.formatNumber(leftValue, MathUtil.PLACE_ONE));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                setTempValue(MathUtil.formatNumber(view.getLeftSeekBar().getProgress(), MathUtil.PLACE_ONE));
            }
        });
        /*????????????*/
        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                if (camera.isTakingVideo()) {
                    return;
                }
                handleCameraData(result.getData());
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected SignPresenter createPresenter() {
        return new SignPresenter();
    }

    /*???????????????????????????button??????*/
    protected void healthCheckButtonChange(){
        if (SharePrefUtils.getBoolean(Constant.HEALTH_CHECK, false)) {
            imSign.setImageDrawable(getResources().getDrawable(R.drawable.btn_next));
        } else {
            imSign.setImageDrawable(getResources().getDrawable(R.drawable.btn_check));
        }
    }

    /*??????UI*/
    private void initChangeUI(SchoolMainEnum type, SingTypeEnum signType) {
        /*????????????*/
        switch (signType) {
            case SIGN_IN:
                tvSignType.setText(R.string.sign_in);
                relSignType.setBackground(this.getResources().getDrawable(R.drawable.bg_class_green));
                break;
            case SIGN_TEMP:
                tvSignType.setText(R.string.sign_temp);
                relSignType.setBackground(this.getResources().getDrawable(R.drawable.bg_class_orange));
                break;
            case SIGN_OUT:
                tvSignType.setText(R.string.sign_out);
                relSignType.setBackground(this.getResources().getDrawable(R.drawable.bg_class_blue));
                break;
        }
        /*????????????*/
        switch (type) {
            case CLASS:
            case BUSES:
                if (mainSchoolContentModel.getStudentsBean() != null) {
                    tvName.setText(mainSchoolContentModel.getStudentsBean().getName());
                    ImageViewUtils.loadImage(this, mainSchoolContentModel.getStudentsBean().getAvatar(), imHead, R.drawable.avatar_loading2);
                    sexIcon(mainSchoolContentModel.getStudentsBean().getGender());
                }
                /*??????????????????*/
                llGuardians.setVisibility(View.VISIBLE);
                /*??????????????????*/
                imHealth.setVisibility(View.VISIBLE);
                /*??????????????????*/
                imHistory.setVisibility(View.VISIBLE);
                /*??????????????????*/
                visibleCamera(SharePrefUtils.getBoolean(Constant.IS_STUDENT_CHECK_CAMERA));
                break;
            case STAFF:
                if (mainSchoolContentModel.getStaffsBean() != null) {
                    tvName.setText(mainSchoolContentModel.getStaffsBean().getName());
                    ImageViewUtils.loadImage(this, mainSchoolContentModel.getStaffsBean().getAvatar(), imHead, R.drawable.avatar_loading);
                    sexIcon(mainSchoolContentModel.getStaffsBean().getGender());
                }
                /*??????????????????*/
                llGuardians.setVisibility(View.GONE);
                /*??????????????????*/
                imHealth.setVisibility(View.GONE);
                /*??????????????????*/
                imHistory.setVisibility(View.GONE);
                /*??????????????????*/
                visibleCamera(SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK_CAMERA));
                break;
        }
    }

    /*??????????????????*/
    private void visibleCamera(boolean isVisible) {
        if (isVisible) {
            imHead.setVisibility(View.GONE);
            cardCamera.setVisibility(View.VISIBLE);
            imCameraChange.setVisibility(View.VISIBLE);
        } else {
            imHead.setVisibility(View.VISIBLE);
            cardCamera.setVisibility(View.GONE);
            imCameraChange.setVisibility(View.GONE);
        }
    }

    private void sexIcon(int gender) {
        switch (gender) {
            case 0:
                imSex.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_boy));
                break;
            case 1:
                imSex.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_girl));
                break;
        }
    }

    @Override
    protected void initData() {
        /*????????????????????????class???buss????????????*/
        switch (type) {
            case CLASS:
            case BUSES:
                mPresenter.mateGuardian(guardianList, mainSchoolContentModel);
                break;
        }
    }

    /*????????????????????????*/
    @Override
    public void mateGuardianResult(List<MateGuardianBean> guardianList) {
        guardianAdapter.setDataList(guardianList);
    }

    /*item??????????????????*/
    @Override
    public void itemClick(List<MateGuardianBean> guardianBeanList, int position) {
        mPresenter.checkSelectGuardian(guardianBeanList, position);
    }

    /*?????????????????????????????????UI*/
    @Override
    public void checkSelectGuardianResult(List<MateGuardianBean> mateGuardianBeanList) {
        guardianAdapter.notifySetChanged(mateGuardianBeanList);
    }

    /*??????????????????*/
    @Override
    public void studentSignSuccess(SignResponse signResponse) {
    }

    /*??????????????????*/
    @Override
    public void staffSignSuccess(SignResponse signResponse) {
    }

    /*??????????????????*/
    @Override
    public void selectGuard(int id) {
        pickUpId(id);
    }

    public void pickUpId(int id) {
    }

    public void setTempValue(String tempValue) {

    }

    /*???????????????????????????????????????*/
    @Override
    protected void receiveEvent(Event event) {
        /*??????????????????*/
        switch (event.getCode()) {
            case 1:
                String tempValue = (String) event.getData();
                tvTemp.setText(tempValue);
                setTempValue(tempValue);
                break;
            case 2:
                bluetoothConnectFailPopup.showPopupWindow();
                break;
        }
    }

    @OnClick({R.id.im_health, R.id.im_close, R.id.im_sign, R.id.im_history, R.id.im_camera_change, R.id.tv_hours, R.id.tv_minute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_health:
                Bundle bundleHealth = new Bundle();
                bundleHealth.putSerializable(Constant.TYPE, type);
                bundleHealth.putSerializable(Constant.SIGN_TYPE, signType);
                bundleHealth.putSerializable(Constant.GENERAL_TYPE, GeneralTypeEnum.NO_GENERAL);
                bundleHealth.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
                openActivity(HealthCheckActivity.class, bundleHealth);
                break;
            case R.id.im_close:
                defaultFinish();
                break;
            case R.id.im_sign:
                signRequest();
                /*??????????????????????????????????????????*/
                if (SharePrefUtils.getBoolean(Constant.IS_STUDENT_CHECK_CAMERA) || SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK_CAMERA)) {
                    capturePicture();
                }
                break;
            case R.id.im_history:
                Bundle bundleHistory = new Bundle();
                bundleHistory.putSerializable(Constant.TYPE, type);
                bundleHistory.putSerializable(Constant.SIGN_TYPE, signType);
                bundleHistory.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
                openActivity(SignHistoryActivity.class, bundleHistory);
                break;
            case R.id.im_camera_change:
                toggleCamera();
                break;
            case R.id.tv_hours:
            case R.id.tv_minute:
                signDateTimePickerUtil.signTimePick(this);
                break;
        }
    }

    /*------------????????????------------------*/

    @Override
    public void signDateTime(String time, String dateTime) {
        createTime = dateTime;
        tvHours.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_HH));
        tvMinute.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_MM));
    }

    /*--------------------????????????start---------------------*/
    //??????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

    //??????activity???????????? ??????????????????????????????
    @Override
    protected void onResume() {
        super.onResume();
        //????????????????????????????????????????????????????????????????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (!camera.isOpened()) {
                camera.open();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
        /*??????????????????*/
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //????????????????????????
        camera.destroy();
    }

    /*???????????????*/
    private void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                break;
            case FRONT:
                break;
        }
    }

    /*??????*/
    private void capturePicture() {
        if (camera.getMode() == Mode.VIDEO) {
            return;
        }
        if (camera.isTakingPicture()) return;
        camera.takePicture();
    }

    private void handleCameraData(final byte[] data) {
        CameraThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                FileUtil.createFileDir(Constant.IMAGE_CACHE_DATA);
                String fileName = "Diibot" + System.currentTimeMillis() + ".jpeg";
                String fileUri = Constant.IMAGE_CACHE_DATA + fileName;
                File file = new File(fileUri);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = fileUri;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String uri = (String) msg.obj;
                    cameraFile(uri);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    /*--------------------????????????end---------------------*/

    protected abstract void signRequest();

    protected abstract void defaultFinish();

    protected abstract void cameraFile(String fileName);
}
