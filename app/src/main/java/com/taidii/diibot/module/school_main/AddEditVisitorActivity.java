package com.taidii.diibot.module.school_main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.enums.VisitDateEnum;
import com.taidii.diibot.entity.enums.VisitTimeEnum;
import com.taidii.diibot.entity.enums.VisitorJumpEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.CovidInfoBean;
import com.taidii.diibot.entity.school.HealthSafeEntryData;
import com.taidii.diibot.entity.school.VisitorDetailBean;
import com.taidii.diibot.module.school_main.contract.VisitorDetailContract;
import com.taidii.diibot.module.school_main.presenter.VisitorDetailPresenter;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.DateUtils;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.PictureSelectUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.VisitDateTimePickerUtil;
import com.taidii.diibot.widget.popup.QuestionnairePopup;
import com.taidii.diibot.widget.popup.VisitDeletePopup;
import com.taidii.diibot.widget.popup.VisitTakePhotoPopup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditVisitorActivity extends BaseMvpActivity<VisitorDetailPresenter> implements VisitDateTimePickerUtil.VisitDateTimeListener
        , VisitorDetailContract.View, VisitDeletePopup.VisitDeleteListener, VisitTakePhotoPopup.VisitTakePhotoListener,QuestionnairePopup.QuestionnairePopupListener {

    @BindView(R.id.rel_top)
    RelativeLayout relTop;
    @BindView(R.id.im_head)
    ImageView imHead;
    @BindView(R.id.im_photo)
    ImageView imPhoto;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_ic)
    EditText editIc;
    @BindView(R.id.edit_contact)
    EditText editContact;
    @BindView(R.id.edit_tem)
    EditText editTem;
    @BindView(R.id.im_male)
    ImageView imMale;
    @BindView(R.id.im_female)
    ImageView imFemale;
    @BindView(R.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R.id.rel_photo_manage)
    RelativeLayout relPhotoManage;
    @BindView(R.id.edit_reason)
    EditText editReason;
    @BindView(R.id.check_in)
    CheckBox checkIn;
    @BindView(R.id.tv_date_in)
    TextView tvDateIn;
    @BindView(R.id.tv_hh_in)
    TextView tvHhIn;
    @BindView(R.id.tv_mm_in)
    TextView tvMmIn;
    @BindView(R.id.tv_ss_in)
    TextView tvSsIn;
    @BindView(R.id.check_out)
    CheckBox checkOut;
    @BindView(R.id.tv_date_out)
    TextView tvDateOut;
    @BindView(R.id.tv_hh_out)
    TextView tvHhOut;
    @BindView(R.id.tv_mm_out)
    TextView tvMmOut;
    @BindView(R.id.tv_ss_out)
    TextView tvSsOut;
    @BindView(R.id.im_delete)
    ImageView imDelete;
    @BindView(R.id.tv_questionnaire)
    TextView tvQuestionnaire;
    @BindView(R.id.im_no)
    ImageView imNo;
    @BindView(R.id.im_yes)
    ImageView imYes;
    @BindView(R.id.ll_travel)
    LinearLayout llTravel;

    private String avatar = "";//头像地址
    private String inDate;//签入日期
    private String outDate;//签出日期
    private String inTime;//签入时间
    private String outTime;//签出时间
    private int visitor_id;//访客者id
    private int gender = 0;//默认性别男
    private int country = 0;//
    private String covid_info = "";//问卷相关信息
    private VisitorJumpEnum jumpType;//跳转类别（添加/编辑）
    private VisitDeletePopup visitDeletePopup;//删除弹窗
    private QuestionnairePopup questionnairePopup;//问卷弹窗
    private VisitTakePhotoPopup visitTakePhotoPopup;//拍照选择弹窗
    private VisitDateTimePickerUtil visitDateTimePickerUtil;//时间日期选择管理
    private HealthSafeEntryData healthSafeEntryData = new HealthSafeEntryData();//问卷相关上传数据

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_edit_visitor;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected VisitorDetailPresenter createPresenter() {
        return new VisitorDetailPresenter();
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
        /*问卷调查按钮显示*/
        if (SharePrefUtils.getBoolean(Constant.SAFE_ACCESS, false)) {
            tvQuestionnaire.setVisibility(View.VISIBLE);
            llTravel.setVisibility(View.VISIBLE);
        } else {
            tvQuestionnaire.setVisibility(View.GONE);
            llTravel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void init() {
        questionnairePopup = new QuestionnairePopup(this,this);
        visitDeletePopup = new VisitDeletePopup(this, this);
        visitTakePhotoPopup = new VisitTakePhotoPopup(this, this);
        visitDateTimePickerUtil = new VisitDateTimePickerUtil(this);
        /*获取跳转传值*/
        jumpType = (VisitorJumpEnum) getIntent().getSerializableExtra(Constant.TYPE);
    }

    /*初始界面UI两种情况（一、添加访客  二、编辑访客）*/
    @Override
    protected void initData() {
        /*通过类别做界面UI初始化*/
        switch (jumpType) {
            case ADD:
                /*添加访客*/
                initAddVisitorUI();
                break;
            case EDIT:
                /*编辑访客信息*/
                visitor_id = getIntent().getIntExtra(Constant.ID, 0);
                if (visitor_id != 0) {
                    /*获取访客信息，刷新UI*/
                    mPresenter.getVisitorDetail(visitor_id);
                }
                break;
        }
    }

    /*蓝牙体温*/
    @Override
    protected void receiveEvent(Event event) {
        /*蓝牙体温状态*/
        switch (event.getCode()) {
            case 1:
                String tempValue = (String) event.getData();
                editTem.setText(tempValue);
                break;
        }
    }

    /*初始添加访客界面UI*/
    private void initAddVisitorUI() {
        /*1.没有删除访客按钮，2.默认性别男，3.默认签入，默认当前时间日期*/
        imDelete.setVisibility(View.GONE);
        imMale.setSelected(true);
        gender = 0;
        checkIn.setChecked(true);
        imNo.setSelected(true);
        /*当前日期时间*/
        String currentDateTime = DateUtil.getStringDate();
        tvDateIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_DATE));
        tvHhIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_HH));
        tvMmIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_MM));
        tvSsIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_SS));
        inDate = DateUtil.formatNewStr(currentDateTime, Constant.LONG_DATE);
        inTime = DateUtil.formatNewStr(currentDateTime, Constant.LONG_TIME);
    }

    /*初始编辑访客界面UI*/
    private void initEditVisitorUI(VisitorDetailBean visitorDetailBean) {
        /*1.展示访客按钮*/
        imDelete.setVisibility(View.VISIBLE);
        imNo.setSelected(true);
        /*赋值UI*/
        editName.setText(visitorDetailBean.getName());
        editIc.setText(visitorDetailBean.getIc());
        editContact.setText(visitorDetailBean.getContact());
        editTem.setText(visitorDetailBean.getTemperature());
        editReason.setText(visitorDetailBean.getReason());
        /*性别*/
        switch (visitorDetailBean.getGender()) {
            case 0:
                cancelGender();
                imMale.setSelected(true);
                break;
            case 1:
                cancelGender();
                imFemale.setSelected(true);
                break;
        }
        /*头像*/
        if (!TextUtils.isEmpty(visitorDetailBean.getAvatar())) {
            imPhoto.setVisibility(View.GONE);
            tvTakePhoto.setVisibility(View.GONE);
            relPhotoManage.setVisibility(View.VISIBLE);
            ImageViewUtils.loadImage(this, visitorDetailBean.getAvatar(), imHead, 0);
        } else {
            imPhoto.setVisibility(View.VISIBLE);
            tvTakePhoto.setVisibility(View.VISIBLE);
            relPhotoManage.setVisibility(View.GONE);
            ImageViewUtils.loadImage(this, visitorDetailBean.getAvatar(), imHead, 0);
        }
        /*签入签出时间日期*/
        if (!TextUtils.isEmpty(visitorDetailBean.getSign_in()) && !TextUtils.isEmpty(visitorDetailBean.getSign_out())) {
            String inDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorDetailBean.getSign_in()), Constant.DATE_TIME_RULE));
            String outDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorDetailBean.getSign_out()), Constant.DATE_TIME_RULE));
            checkIn.setChecked(true);
            checkOut.setChecked(true);
            tvDateIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_DATE));
            tvHhIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_HH));
            tvMmIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_MM));
            tvSsIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_SS));
            tvDateOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_DATE));
            tvHhOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_HH));
            tvMmOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_MM));
            tvSsOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_SS));
            inDate = DateUtil.formatNewStr(inDatetime, Constant.LONG_DATE);
            inTime = DateUtil.formatNewStr(inDatetime, Constant.LONG_TIME);
            outDate = DateUtil.formatNewStr(outDatetime, Constant.LONG_DATE);
            outTime = DateUtil.formatNewStr(outDatetime, Constant.LONG_TIME);
        } else {
            if (!TextUtils.isEmpty(visitorDetailBean.getSign_in())) {
                String inDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorDetailBean.getSign_in()), Constant.DATE_TIME_RULE));
                checkIn.setChecked(true);
                checkOut.setChecked(false);
                tvDateIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_DATE));
                tvHhIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_HH));
                tvMmIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_MM));
                tvSsIn.setText(DateUtil.formatNewStr(inDatetime, Constant.VISIT_SS));
                inDate = DateUtil.formatNewStr(inDatetime, Constant.LONG_DATE);
                inTime = DateUtil.formatNewStr(inDatetime, Constant.LONG_TIME);
            } else if (!TextUtils.isEmpty(visitorDetailBean.getSign_out())) {
                String outDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorDetailBean.getSign_out()), Constant.DATE_TIME_RULE));
                checkIn.setChecked(false);
                checkOut.setChecked(true);
                tvDateOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_DATE));
                tvHhOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_HH));
                tvMmOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_MM));
                tvSsOut.setText(DateUtil.formatNewStr(outDatetime, Constant.VISIT_SS));
                outDate = DateUtil.formatNewStr(outDatetime, Constant.LONG_DATE);
                outTime = DateUtil.formatNewStr(outDatetime, Constant.LONG_TIME);
            } else {
                checkIn.setChecked(false);
                checkOut.setChecked(false);
            }
        }
        /*问卷相关*/
        CovidInfoBean covidInfoBean = visitorDetailBean.getCovid_info();
        if (covidInfoBean!=null){
            if (covidInfoBean.isHave_travel()){
                imNo.setSelected(false);
                imYes.setSelected(true);
            }else {
                imNo.setSelected(true);
                imYes.setSelected(false);
            }
            /*上传信息*/
            healthSafeEntryData.setHave_travel(covidInfoBean.isHave_travel());
            healthSafeEntryData.setHave_fever(covidInfoBean.isHave_fever());
            healthSafeEntryData.setCough(covidInfoBean.isCough());
            healthSafeEntryData.setSore_throat(covidInfoBean.isSore_throat());
            healthSafeEntryData.setRunny_nose(covidInfoBean.isRunny_nose());
            healthSafeEntryData.setShortness_breath(covidInfoBean.isShortness_breath());
            healthSafeEntryData.setSense_smell(covidInfoBean.isSense_smell());
            healthSafeEntryData.setUnwell(covidInfoBean.isUnwell());
            healthSafeEntryData.setAdult_trouble(covidInfoBean.isAdult_trouble());
            healthSafeEntryData.setUnwell_content(covidInfoBean.getUnwell_content());
        }
    }

    /*编辑访客（获取信息返回）*/
    @Override
    public void visitorDetail(VisitorDetailBean visitorDetailBean) {
        if (visitorDetailBean != null) {
            initEditVisitorUI(visitorDetailBean);
        }
    }

    @OnClick({R.id.im_delete, R.id.im_close, R.id.im_confirm, R.id.rel_take_photo, R.id.ll_male, R.id.ll_female, R.id.im_clean,R.id.ll_yes,R.id.ll_no
            , R.id.im_reset, R.id.check_in, R.id.ll_date_in, R.id.ll_time_in, R.id.check_out, R.id.ll_date_out, R.id.ll_time_out, R.id.tv_questionnaire})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_delete:
                /*删除访客*/
                visitDeletePopup.showPopupWindow();
                break;
            case R.id.tv_questionnaire:
                /*问卷调查*/
                questionnairePopup.showPopupWindow();
                questionnairePopup.setSafeEntryData(healthSafeEntryData);
                break;
            case R.id.im_close:
                finish();
                break;
            case R.id.im_confirm:
                /*添加访客/编辑访客*/
                if (SharePrefUtils.getBoolean(Constant.SAFE_ACCESS)){
                    covid_info = new Gson().toJson(healthSafeEntryData);
                }
                switch (jumpType) {
                    case ADD:
                        mPresenter.addVisitor(DateUtil.getStringDate(), editName.getText().toString(), editIc.getText().toString(), editTem.getText().toString(),
                                editReason.getText().toString(), editContact.getText().toString(), gender, country,
                                inDate, inTime, outDate, outTime, checkIn.isChecked(), checkOut.isChecked(), avatar,covid_info);
                        break;
                    case EDIT:
                        mPresenter.editVisitor(DateUtil.getStringDate(), editName.getText().toString(), editIc.getText().toString(), editTem.getText().toString(),
                                editReason.getText().toString(), editContact.getText().toString(), gender, country,
                                inDate, inTime, outDate, outTime, checkIn.isChecked(), checkOut.isChecked(), avatar,covid_info, visitor_id);
                        break;
                }
                break;
            case R.id.im_reset:
            case R.id.rel_take_photo:
                /*拍照/相册选择头像*/
                visitTakePhotoPopup.showPopupWindow();
                break;
            case R.id.ll_male:
                /*性别男*/
                cancelGender();
                imMale.setSelected(true);
                gender = 0;
                break;
            case R.id.ll_female:
                /*性别女*/
                cancelGender();
                imFemale.setSelected(true);
                gender = 1;
                break;
            case R.id.im_clean:
                /*清除头像*/
                avatar = "";
                ImageViewUtils.loadImage(this, null, imHead, R.drawable.noicon);
                imPhoto.setVisibility(View.GONE);
                tvTakePhoto.setVisibility(View.VISIBLE);
                relPhotoManage.setVisibility(View.GONE);
                break;
            case R.id.check_in:
                /*签入*/
                signInCheck();
                break;
            case R.id.ll_date_in:
                /*签入日期选择*/
                visitDateTimePickerUtil.visitDatePick(this, VisitDateEnum.VISIT_DATE_IN);
                break;
            case R.id.ll_time_in:
                /*签入时间选择*/
                visitDateTimePickerUtil.visitTimePick(this, VisitTimeEnum.VISIT_TIME_IN);
                break;
            case R.id.check_out:
                /*签出*/
                signOutCheck();
                break;
            case R.id.ll_date_out:
                /*签出日期选择*/
                visitDateTimePickerUtil.visitDatePick(this, VisitDateEnum.VISIT_DATE_OUT);
                break;
            case R.id.ll_time_out:
                /*签出时间选择*/
                visitDateTimePickerUtil.visitTimePick(this, VisitTimeEnum.VISIT_TIME_OUT);
                break;
            case R.id.ll_yes:
                cancelTravel();
                imYes.setSelected(true);
                healthSafeEntryData.setHave_travel(true);
                break;
            case R.id.ll_no:
                cancelTravel();
                imNo.setSelected(true);
                healthSafeEntryData.setHave_travel(false);
                break;
        }
    }

    /*签入check*/
    private void signInCheck() {
        if (TextUtils.isEmpty(inDate) || TextUtils.isEmpty(inTime)) {
            String currentDateTime = DateUtil.getStringDate();
            tvDateIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_DATE));
            tvHhIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_HH));
            tvMmIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_MM));
            tvSsIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_SS));
            inDate = DateUtil.formatNewStr(currentDateTime, Constant.LONG_DATE);
            inTime = DateUtil.formatNewStr(currentDateTime, Constant.LONG_TIME);
        }
    }

    /*签出check*/
    private void signOutCheck() {
        if (TextUtils.isEmpty(outDate) || TextUtils.isEmpty(outTime)) {
            String currentDateTime = DateUtil.getStringDate();
            tvDateOut.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_DATE));
            tvHhOut.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_HH));
            tvMmOut.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_MM));
            tvSsOut.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_SS));
            outDate = DateUtil.formatNewStr(currentDateTime, Constant.LONG_DATE);
            outTime = DateUtil.formatNewStr(currentDateTime, Constant.LONG_TIME);
        }
    }

    /*签入日期选择回调*/
    @Override
    public void visitInDate(String date, String dateTime) {
        inDate = date;
        tvDateIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_DATE));
    }

    /*签入时间选择回调*/
    @Override
    public void visitOutDate(String date, String dateTime) {
        outDate = date;
        tvDateOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_DATE));
    }

    /*签出日期选择回调*/
    @Override
    public void visitInTime(String time, String dateTime) {
        inTime = time;
        tvHhIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_HH));
        tvMmIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_MM));
        tvSsIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_SS));
    }

    /*签出时间选择回调*/
    @Override
    public void visitOutTime(String time, String dateTime) {
        outTime = time;
        tvHhOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_HH));
        tvMmOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_MM));
        tvSsOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_SS));
    }

    /*问卷调查弹窗结果选择回调*/
    @Override
    public void questionnaireResult(HealthSafeEntryData healthSafeEntryData) {
        this.healthSafeEntryData = healthSafeEntryData;
    }

    /*性别取消*/
    private void cancelGender() {
        imMale.setSelected(false);
        imFemale.setSelected(false);
    }

    /*取消外出*/
    private void cancelTravel() {
        imNo.setSelected(false);
        imYes.setSelected(false);
    }

    /*删除访客*/
    @Override
    public void visitDelete() {
        mPresenter.deleteVisitor(visitor_id);
    }

    /*访客添加 编辑提交返回*/
    @Override
    public void visitorRequestBack() {
        finish();
    }

    /*上传头像拍照*/
    @Override
    public void visitTakePhoto() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PictureSelectUtil.takePhoto(AddEditVisitorActivity.this);
            }
        }, 400);
    }

    /*上传头像相册选择*/
    @Override
    public void visitSelectPhoto() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PictureSelectUtil.pictureSelect(AddEditVisitorActivity.this);
            }
        }, 400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 原图path
                    // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                    // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    for (LocalMedia media : selectList) {
                        ImageViewUtils.loadImage(this, media.getPath(), imHead, R.drawable.noicon);
                        avatar = media.getCompressPath();
                        imPhoto.setVisibility(View.GONE);
                        tvTakePhoto.setVisibility(View.GONE);
                        relPhotoManage.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}
