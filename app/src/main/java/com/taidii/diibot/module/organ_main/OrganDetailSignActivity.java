package com.taidii.diibot.module.organ_main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.organ.ClassOrderBean;
import com.taidii.diibot.entity.organ.KlassesBean;
import com.taidii.diibot.entity.organ.OrganDetailBean;
import com.taidii.diibot.entity.organ.OrganDetailData;
import com.taidii.diibot.entity.organ.OrganSignResponse;
import com.taidii.diibot.entity.organ.OrganStudentSignBody;
import com.taidii.diibot.entity.organ.StudentsBean;
import com.taidii.diibot.module.organ_main.contract.OrganDetailContract;
import com.taidii.diibot.module.organ_main.presenter.OrganDetailPresenter;
import com.taidii.diibot.utils.CameraThreadPool;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.FileUtil;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.MathUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.Utils;
import com.taidii.diibot.widget.popup.BluetoothConnectFailPopup;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *????????????  ????????????????????????????????? ?????????
 * */

public class OrganDetailSignActivity extends BaseMvpActivity<OrganDetailPresenter> implements OrganDetailContract.View {

    @BindView(R.id.tv_organ)
    TextView tvOrgan;
    @BindView(R.id.im_camera_change)
    ImageView imCameraChange;
    @BindView(R.id.im_head)
    RoundedImageView imHead;
    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.rel_avr)
    RelativeLayout relAvr;
    @BindView(R.id.seek_bar_temp)
    VerticalRangeSeekBar seekBarTemp;
    @BindView(R.id.rel_temp)
    RelativeLayout relTemp;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.im_sex)
    ImageView imSex;
    @BindView(R.id.tv_classroom)
    TextView tvClassroom;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_remaining)
    TextView tvRemaining;
    @BindView(R.id.rel_no_sign)
    RelativeLayout relNoSign;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.im_head_ed)
    RoundedImageView imHeadEd;
    @BindView(R.id.tv_name_ed)
    TextView tvNameEd;
    @BindView(R.id.im_sex_ed)
    ImageView imSexEd;
    @BindView(R.id.tv_classroom_ed)
    TextView tvClassroomEd;
    @BindView(R.id.tv_start_time_ed)
    TextView tvStartTimeEd;
    @BindView(R.id.tv_remaining_ed)
    TextView tvRemainingEd;
    @BindView(R.id.rel_signed)
    RelativeLayout relSigned;
    @BindView(R.id.im_close)
    ImageView imClose;
    @BindView(R.id.card_camera)
    CardView cardCamera;
    @BindView(R.id.im_sign)
    ImageView imSign;

    private String fileName;//??????????????????
    private StudentsBean studentsBean;//??????item??????
    private ClassOrderBean classOrder;//????????????
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    protected BluetoothConnectFailPopup bluetoothConnectFailPopup;//??????????????????
    private OrganStudentSignBody organStudentSignBody = new OrganStudentSignBody();//????????????body

    @Override
    protected OrganDetailPresenter createPresenter() {
        return new OrganDetailPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organ_detail_sign;
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
        /*??????*/
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera.setLifecycleOwner(this);
        /*????????????????????????*/
        seekBarTemp.setRange(35.0f, 40.0f, 0.1f);
        seekBarTemp.setProgress(37.6f);
        initListener();
    }

    @Override
    protected void init() {
        classOrder = (ClassOrderBean) getIntent().getSerializableExtra(Constant.ORGAN_CLASS_ORDER);
        studentsBean = (StudentsBean) getIntent().getSerializableExtra(Constant.ORGAN_CONTENT_MODEL);
        bluetoothConnectFailPopup = new BluetoothConnectFailPopup(this);
    }

    @Override
    protected void initData() {
        if (studentsBean != null) {
            mPresenter.getOrganDetail(studentsBean.getCenterStudentId(), studentsBean.getIs_test_child());
        }
    }

    /*??????*/
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

    /*??????*/
    private void setTempValue(String tempValue) {
        organStudentSignBody.setTemperature(Float.parseFloat(tempValue));
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

    /*????????????*/
    @Override
    public void OrganDetail(OrganDetailBean organDetailBean) {
        /*??????????????????????????????(????????????)*/
        if (studentsBean.isSign()){
            setChangeSigned(organDetailBean);
        }else {
            setChangeNoSign(organDetailBean);
        }
    }

    /*????????????????????????????????????*/
    @Override
    public void SignBack(OrganSignResponse organSignResponse) {
        /*???????????????????????????????????????????????????*/
        studentsBean.setSign(true);
        EventBusUtil.sendEvent(new Event(6,studentsBean));
        mPresenter.getOrganDetail(studentsBean.getCenterStudentId(), studentsBean.getIs_test_child());
        /*??????????????????????????????*/
        if (SharePrefUtils.getBoolean(Constant.IS_ORGAN_STUDENT_CHECK_CAMERA)){
            if (organSignResponse.getData()!=null){
                mPresenter.organPictureInput(studentsBean.getCenterStudentId(),organSignResponse.getData().getId(), DateUtil.getStringDate(),fileName);
            }
        }
    }

    /*?????????????????????*/
    private void setChangeNoSign(OrganDetailBean organDetailBean){
        relNoSign.setVisibility(View.VISIBLE);
        relSigned.setVisibility(View.GONE);
        imSign.setVisibility(View.VISIBLE);
        imClose.setVisibility(View.VISIBLE);
        imClose.setImageDrawable(getResources().getDrawable(R.drawable.btn_close));
        /*??????????????????*/
        visibleCamera(SharePrefUtils.getBoolean(Constant.IS_ORGAN_STUDENT_CHECK_CAMERA));
        OrganDetailData data = organDetailBean.getData();
        tvName.setText(data.getName());
        switch (data.getGender()){
            case 0:
                imSex.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_boy));
                break;
            case 1:
                imSex.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_girl));
                break;
        }
        ImageViewUtils.loadImage(this,data.getAvatar(),imHead,R.drawable.avatar_loading2);
        if (classOrder!=null){
            tvOrgan.setText(classOrder.getClassname());
            tvClassroom.setText(String.format(getResources().getString(R.string.class_room),classOrder.getRoomname()));
            /*??????????????????*/
            if (Utils.isZh(this)){
                tvStartTime.setText(String.format(getResources().getString(R.string.start_time),classOrder.getKlass_schedule_cn()));
            }else {
                tvStartTime.setText(String.format(getResources().getString(R.string.start_time),classOrder.getKlass_schedule()));
            }
        }

        if (data.getKlasses().size()>0&&classOrder!=null){
            KlassesBean currentKlasses = null;
            for (KlassesBean klassesBean : data.getKlasses()){
                if (klassesBean.getKlassschedule_id()==classOrder.getId()){
                    currentKlasses = klassesBean;
                }
            }
            if (currentKlasses!=null){
                tvRemaining.setText(String.format(getResources().getString(R.string.remaining)
                        ,String.valueOf(currentKlasses.getBalance_of_class()),String.valueOf(currentKlasses.getNumber_of_class())));
                /*??????body?????????*/
                if (!TextUtils.isEmpty(currentKlasses.getStudentklass_id())){
                    organStudentSignBody.setStudentklass_id(Integer.parseInt(currentKlasses.getStudentklass_id()));
                }
                organStudentSignBody.setKlassschedule_id(currentKlasses.getKlassschedule_id());
                organStudentSignBody.setIs_secondary_card(currentKlasses.getIs_secondary_card());
                organStudentSignBody.setStudent_class_bag_id(currentKlasses.getStudent_class_bag_id());
                organStudentSignBody.setClass_time(currentKlasses.getClass_time());
            }
        }
        /*??????body?????????*/
        organStudentSignBody.setStudentid(studentsBean.getCenterStudentId());
    }

    /*?????????????????????*/
    private void setChangeSigned(OrganDetailBean organDetailBean){
        relNoSign.setVisibility(View.GONE);
        relSigned.setVisibility(View.VISIBLE);
        imSign.setVisibility(View.GONE);
        imClose.setVisibility(View.VISIBLE);
        imClose.setImageDrawable(getResources().getDrawable(R.drawable.btn_back));
        OrganDetailData data = organDetailBean.getData();
        tvTitle.setText(classOrder.getClassname());
        tvNameEd.setText(data.getName());
        ImageViewUtils.loadImage(this,data.getAvatar(),imHeadEd,R.drawable.avatar_loading2);
        switch (data.getGender()){
            case 0:
                imSexEd.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_boy));
                break;
            case 1:
                imSexEd.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_girl));
                break;
        }
        if (classOrder!=null){
            tvClassroomEd.setText(String.format(getResources().getString(R.string.class_room),classOrder.getRoomname()));
            /*??????????????????*/
            if (Utils.isZh(this)){
                tvStartTimeEd.setText(String.format(getResources().getString(R.string.start_time),classOrder.getKlass_schedule_cn()));
            }else {
                tvStartTimeEd.setText(String.format(getResources().getString(R.string.start_time),classOrder.getKlass_schedule()));
            }
        }
        if (data.getKlasses().size()>0&&classOrder!=null){
            KlassesBean currentKlasses = null;
            for (KlassesBean klassesBean : data.getKlasses()){
                if (klassesBean.getKlassschedule_id()==classOrder.getId()){
                    currentKlasses = klassesBean;
                }
            }
            if (currentKlasses!=null){
                tvTime.setText(currentKlasses.getRecorded_on());
                tvRemainingEd.setText(String.format(getResources().getString(R.string.remaining)
                        ,String.valueOf(currentKlasses.getBalance_of_class()),String.valueOf(currentKlasses.getNumber_of_class())));
            }
        }
    }

    @OnClick({R.id.im_camera_change, R.id.im_close, R.id.im_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_camera_change:
                toggleCamera();
                break;
            case R.id.im_close:
                finish();
                break;
            case R.id.im_sign:
                /*??????????????????*/
                if (SharePrefUtils.getBoolean(Constant.IS_ORGAN_STUDENT_CHECK_CAMERA)) {
                    capturePicture();
                }else {
                    mPresenter.studentSignRequest(organStudentSignBody);
                }
                break;
        }
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
                    fileName = uri;
                    /*????????????????????????????????????*/
                    mPresenter.studentSignRequest(organStudentSignBody);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    /*--------------------????????????end---------------------*/

}
