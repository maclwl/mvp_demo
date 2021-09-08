package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.utils.SharePrefUtils;
import com.suke.widget.SwitchButton;

import razerdp.basepopup.BasePopupWindow;

public class SettingPopup extends BasePopupWindow implements View.OnClickListener, SwitchButton.OnCheckedChangeListener {

    private SwitchButton btn_staff_check,btn_staff_check_camera,btn_quick_check,btn_student_check_camera,
            btn_spread_all,btn_out_line,btn_temp_default,btn_visible,btn_hint_sign_student;

    private SettingPopupListener settingPopupListener;

    public SettingPopup(Context context,SettingPopupListener settingPopupListener) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        this.settingPopupListener = settingPopupListener;
        initEvent();
    }

    public interface SettingPopupListener{

        void staffCheckSetting(boolean isCheck);

        void spreadCheckSetting(boolean isCheck);

        void previewPrivacy();

        void classCardJump();

        void previewAgreement();

        void faceSynchronization();

        void hideSignInCheckSetting(boolean isCheck);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_setting);
    }

    private void initEvent(){
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.rel_privacy).setOnClickListener(this);
        findViewById(R.id.rel_agreement).setOnClickListener(this);
        findViewById(R.id.rel_synchronization).setOnClickListener(this);
        findViewById(R.id.rel_class_card).setOnClickListener(this);
        btn_visible = findViewById(R.id.btn_visible);
        btn_out_line = findViewById(R.id.btn_out_line);
        btn_spread_all = findViewById(R.id.btn_spread_all);
        btn_quick_check = findViewById(R.id.btn_quick_check);
        btn_staff_check = findViewById(R.id.btn_staff_check);
        btn_temp_default = findViewById(R.id.btn_temp_default);
        btn_staff_check_camera = findViewById(R.id.btn_staff_check_camera);
        btn_student_check_camera = findViewById(R.id.btn_student_check_camera);
        btn_hint_sign_student = findViewById(R.id.btn_hint_sign_student);
        btn_visible.setOnCheckedChangeListener(this);
        btn_out_line.setOnCheckedChangeListener(this);
        btn_spread_all.setOnCheckedChangeListener(this);
        btn_quick_check.setOnCheckedChangeListener(this);
        btn_staff_check.setOnCheckedChangeListener(this);
        btn_temp_default.setOnCheckedChangeListener(this);
        btn_staff_check_camera.setOnCheckedChangeListener(this);
        btn_student_check_camera.setOnCheckedChangeListener(this);
        btn_hint_sign_student.setOnCheckedChangeListener(this);
        /*初始设置值*/
        btn_visible.setChecked(SharePrefUtils.getBoolean(Constant.IS_VISIBLE));
        btn_out_line.setChecked(SharePrefUtils.getBoolean(Constant.IS_OUT_LINE));
        btn_spread_all.setChecked(SharePrefUtils.getBoolean(Constant.IS_SPREAD_ALL));
        btn_quick_check.setChecked(SharePrefUtils.getBoolean(Constant.IS_QUICK_CHECK));
        btn_staff_check.setChecked(SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK));
        btn_temp_default.setChecked(SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT));
        btn_staff_check_camera.setChecked(SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK_CAMERA));
        btn_student_check_camera.setChecked(SharePrefUtils.getBoolean(Constant.IS_STUDENT_CHECK_CAMERA));
        btn_hint_sign_student.setChecked(SharePrefUtils.getBoolean(Constant.IS_HIDE_SIGN_IN));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.rel_privacy:
                settingPopupListener.previewPrivacy();
                break;
            case R.id.rel_agreement:
                settingPopupListener.previewAgreement();
                break;
            case R.id.rel_synchronization:
                settingPopupListener.faceSynchronization();
                break;
            case R.id.rel_class_card:
                settingPopupListener.classCardJump();
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()){
            case R.id.btn_visible:
                break;
            case R.id.btn_out_line:
                btn_out_line.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_OUT_LINE,isChecked);
                break;
            case R.id.btn_spread_all:
                btn_spread_all.setChecked(isChecked);
                settingPopupListener.spreadCheckSetting(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_SPREAD_ALL,isChecked);
                break;
            case R.id.btn_quick_check:
                btn_quick_check.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_QUICK_CHECK,isChecked);
                break;
            case R.id.btn_staff_check:
                btn_staff_check.setChecked(isChecked);
                settingPopupListener.staffCheckSetting(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_STAFF_CHECK,isChecked);
                break;
            case R.id.btn_temp_default:
                btn_temp_default.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_TEMP_DEFAULT,isChecked);
                break;
            case R.id.btn_staff_check_camera:
                btn_staff_check_camera.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_STAFF_CHECK_CAMERA,isChecked);
                break;
            case R.id.btn_student_check_camera:
                btn_student_check_camera.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_STUDENT_CHECK_CAMERA,isChecked);
                break;
            case R.id.btn_hint_sign_student:
                btn_hint_sign_student.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_HIDE_SIGN_IN,isChecked);
                settingPopupListener.hideSignInCheckSetting(isChecked);
                break;
        }
    }

}
