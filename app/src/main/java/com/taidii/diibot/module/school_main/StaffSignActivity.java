package com.taidii.diibot.module.school_main;

import android.os.Bundle;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StaffsBean;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.VibratorUtil;

public class StaffSignActivity extends BaseSignDetailActivity {

    private StaffSignBody staffSignBody = new StaffSignBody();//老师签到请求body

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        /*设置请求参数*/
        StaffsBean staffsBean = mainSchoolContentModel.getStaffsBean();
        staffSignBody.setStaffid(staffsBean.getStaffId());
        staffSignBody.setType(signType.getType());
        /*是否设置温度默认值*/
        if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
            staffSignBody.setTemperature(36.5f);
        }
    }

    @Override
    protected void signRequest() {
        /*判断老师考勤拍照（图片信息上传）*/
        if (!SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK_CAMERA)){
            mPresenter.staffSignRequest(getStaffSignBody());
        }
    }

    @Override
    public void setTempValue(String tempValue) {
        staffSignBody.setTemperature(Float.parseFloat(tempValue));
        if (Float.parseFloat(tempValue)>=37.6f) {
            VibratorUtil.getInstance(this).vibrateAndPlayTone();
        }
    }

    private StaffSignBody getStaffSignBody(){
        staffSignBody.setDate(createTime);
        staffSignBody.setRemarks("");
        staffSignBody.setCommon("");
        return staffSignBody;
    }

    /*签到成功*/
    @Override
    public void staffSignSuccess(SignResponse signResponse) {
        StaffsBean staffSign = mainSchoolContentModel.getStaffsBean();
        staffSign.setSign(true);
        staffSign.setSignType(signType);
        if (signType.equals(SingTypeEnum.SIGN_IN)){
            staffSign.setHasSignIn(true);
        }
        mainSchoolContentModel.setStaffsBean(staffSign);
        EventBusUtil.sendEvent(new Event(0,mainSchoolContentModel));
        finish();
    }

    @Override
    protected void defaultFinish() {
        finish();
    }

    /*拍照返回图片地址*/
    @Override
    protected void cameraFile(String fileName) {
        mPresenter.staffSignRequest(getStaffSignBody());
        if (SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK_CAMERA)){
            mPresenter.staffPictureInput(mainSchoolContentModel.getStaffsBean().getStaffId(),createTime,fileName);
        }
    }

}
