package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.suke.widget.SwitchButton;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.utils.SharePrefUtils;

import razerdp.basepopup.BasePopupWindow;

public class OrganSettingPopup extends BasePopupWindow implements View.OnClickListener,SwitchButton.OnCheckedChangeListener{

    private SwitchButton btn_student_check_camera;

    public OrganSettingPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        initEvent();
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
        return createPopupById(R.layout.popup_organ_setting);
    }

    private void initEvent(){
        findViewById(R.id.ll_close).setOnClickListener(this);
        btn_student_check_camera = findViewById(R.id.btn_student_check_camera);
        btn_student_check_camera.setOnCheckedChangeListener(this);
        btn_student_check_camera.setChecked(SharePrefUtils.getBoolean(Constant.IS_ORGAN_STUDENT_CHECK_CAMERA));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close:
                dismiss();
                break;
        }
    }
    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.btn_student_check_camera:
                btn_student_check_camera.setChecked(isChecked);
                SharePrefUtils.saveBoolean(Constant.IS_ORGAN_STUDENT_CHECK_CAMERA,isChecked);
                break;
        }
    }
}

