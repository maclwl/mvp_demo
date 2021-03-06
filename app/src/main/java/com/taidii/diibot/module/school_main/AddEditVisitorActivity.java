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

    private String avatar = "";//????????????
    private String inDate;//????????????
    private String outDate;//????????????
    private String inTime;//????????????
    private String outTime;//????????????
    private int visitor_id;//?????????id
    private int gender = 0;//???????????????
    private int country = 0;//
    private String covid_info = "";//??????????????????
    private VisitorJumpEnum jumpType;//?????????????????????/?????????
    private VisitDeletePopup visitDeletePopup;//????????????
    private QuestionnairePopup questionnairePopup;//????????????
    private VisitTakePhotoPopup visitTakePhotoPopup;//??????????????????
    private VisitDateTimePickerUtil visitDateTimePickerUtil;//????????????????????????
    private HealthSafeEntryData healthSafeEntryData = new HealthSafeEntryData();//????????????????????????

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
        /*????????????????????????*/
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
        /*??????????????????*/
        jumpType = (VisitorJumpEnum) getIntent().getSerializableExtra(Constant.TYPE);
    }

    /*????????????UI?????????????????????????????????  ?????????????????????*/
    @Override
    protected void initData() {
        /*?????????????????????UI?????????*/
        switch (jumpType) {
            case ADD:
                /*????????????*/
                initAddVisitorUI();
                break;
            case EDIT:
                /*??????????????????*/
                visitor_id = getIntent().getIntExtra(Constant.ID, 0);
                if (visitor_id != 0) {
                    /*???????????????????????????UI*/
                    mPresenter.getVisitorDetail(visitor_id);
                }
                break;
        }
    }

    /*????????????*/
    @Override
    protected void receiveEvent(Event event) {
        /*??????????????????*/
        switch (event.getCode()) {
            case 1:
                String tempValue = (String) event.getData();
                editTem.setText(tempValue);
                break;
        }
    }

    /*????????????????????????UI*/
    private void initAddVisitorUI() {
        /*1.???????????????????????????2.??????????????????3.???????????????????????????????????????*/
        imDelete.setVisibility(View.GONE);
        imMale.setSelected(true);
        gender = 0;
        checkIn.setChecked(true);
        imNo.setSelected(true);
        /*??????????????????*/
        String currentDateTime = DateUtil.getStringDate();
        tvDateIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_DATE));
        tvHhIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_HH));
        tvMmIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_MM));
        tvSsIn.setText(DateUtil.formatNewStr(currentDateTime, Constant.VISIT_SS));
        inDate = DateUtil.formatNewStr(currentDateTime, Constant.LONG_DATE);
        inTime = DateUtil.formatNewStr(currentDateTime, Constant.LONG_TIME);
    }

    /*????????????????????????UI*/
    private void initEditVisitorUI(VisitorDetailBean visitorDetailBean) {
        /*1.??????????????????*/
        imDelete.setVisibility(View.VISIBLE);
        imNo.setSelected(true);
        /*??????UI*/
        editName.setText(visitorDetailBean.getName());
        editIc.setText(visitorDetailBean.getIc());
        editContact.setText(visitorDetailBean.getContact());
        editTem.setText(visitorDetailBean.getTemperature());
        editReason.setText(visitorDetailBean.getReason());
        /*??????*/
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
        /*??????*/
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
        /*????????????????????????*/
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
        /*????????????*/
        CovidInfoBean covidInfoBean = visitorDetailBean.getCovid_info();
        if (covidInfoBean!=null){
            if (covidInfoBean.isHave_travel()){
                imNo.setSelected(false);
                imYes.setSelected(true);
            }else {
                imNo.setSelected(true);
                imYes.setSelected(false);
            }
            /*????????????*/
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

    /*????????????????????????????????????*/
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
                /*????????????*/
                visitDeletePopup.showPopupWindow();
                break;
            case R.id.tv_questionnaire:
                /*????????????*/
                questionnairePopup.showPopupWindow();
                questionnairePopup.setSafeEntryData(healthSafeEntryData);
                break;
            case R.id.im_close:
                finish();
                break;
            case R.id.im_confirm:
                /*????????????/????????????*/
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
                /*??????/??????????????????*/
                visitTakePhotoPopup.showPopupWindow();
                break;
            case R.id.ll_male:
                /*?????????*/
                cancelGender();
                imMale.setSelected(true);
                gender = 0;
                break;
            case R.id.ll_female:
                /*?????????*/
                cancelGender();
                imFemale.setSelected(true);
                gender = 1;
                break;
            case R.id.im_clean:
                /*????????????*/
                avatar = "";
                ImageViewUtils.loadImage(this, null, imHead, R.drawable.noicon);
                imPhoto.setVisibility(View.GONE);
                tvTakePhoto.setVisibility(View.VISIBLE);
                relPhotoManage.setVisibility(View.GONE);
                break;
            case R.id.check_in:
                /*??????*/
                signInCheck();
                break;
            case R.id.ll_date_in:
                /*??????????????????*/
                visitDateTimePickerUtil.visitDatePick(this, VisitDateEnum.VISIT_DATE_IN);
                break;
            case R.id.ll_time_in:
                /*??????????????????*/
                visitDateTimePickerUtil.visitTimePick(this, VisitTimeEnum.VISIT_TIME_IN);
                break;
            case R.id.check_out:
                /*??????*/
                signOutCheck();
                break;
            case R.id.ll_date_out:
                /*??????????????????*/
                visitDateTimePickerUtil.visitDatePick(this, VisitDateEnum.VISIT_DATE_OUT);
                break;
            case R.id.ll_time_out:
                /*??????????????????*/
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

    /*??????check*/
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

    /*??????check*/
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

    /*????????????????????????*/
    @Override
    public void visitInDate(String date, String dateTime) {
        inDate = date;
        tvDateIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_DATE));
    }

    /*????????????????????????*/
    @Override
    public void visitOutDate(String date, String dateTime) {
        outDate = date;
        tvDateOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_DATE));
    }

    /*????????????????????????*/
    @Override
    public void visitInTime(String time, String dateTime) {
        inTime = time;
        tvHhIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_HH));
        tvMmIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_MM));
        tvSsIn.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_SS));
    }

    /*????????????????????????*/
    @Override
    public void visitOutTime(String time, String dateTime) {
        outTime = time;
        tvHhOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_HH));
        tvMmOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_MM));
        tvSsOut.setText(DateUtil.formatNewStr(dateTime, Constant.VISIT_SS));
    }

    /*????????????????????????????????????*/
    @Override
    public void questionnaireResult(HealthSafeEntryData healthSafeEntryData) {
        this.healthSafeEntryData = healthSafeEntryData;
    }

    /*????????????*/
    private void cancelGender() {
        imMale.setSelected(false);
        imFemale.setSelected(false);
    }

    /*????????????*/
    private void cancelTravel() {
        imNo.setSelected(false);
        imYes.setSelected(false);
    }

    /*????????????*/
    @Override
    public void visitDelete() {
        mPresenter.deleteVisitor(visitor_id);
    }

    /*???????????? ??????????????????*/
    @Override
    public void visitorRequestBack() {
        finish();
    }

    /*??????????????????*/
    @Override
    public void visitTakePhoto() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PictureSelectUtil.takePhoto(AddEditVisitorActivity.this);
            }
        }, 400);
    }

    /*????????????????????????*/
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
                    // ????????????????????????
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // ?????? LocalMedia ??????????????????path
                    // 1.media.getPath(); ??????path
                    // 2.media.getCutPath();?????????path????????????media.isCut();??????????????????
                    // 3.media.getCompressPath();?????????path????????????media.isCompressed();??????????????????
                    // 4.media.getOriginalPath()); media.isOriginal());???true?????????????????????
                    // 5.media.getAndroidQToPath();Android Q?????????????????????????????????????????????????????????????????????????????????????????????????????????.isAndroidQTransform ???false ?????????????????????
                    // ???????????????????????????????????????????????????????????????????????????????????????
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
