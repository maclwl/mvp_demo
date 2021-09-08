package com.taidii.diibot.module.school_main;

import android.os.Bundle;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.enums.GeneralTypeEnum;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.VibratorUtil;

public class StudentSignActivity extends BaseSignDetailActivity {

    private StudentSignBody studentSignBody = new StudentSignBody();//签到请求body

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        healthCheckButtonChange();
        StudentsBean student = mainSchoolContentModel.getStudentsBean();
        /*设置请求参数*/
        studentSignBody.setStudentid(student.getCenterStudentId());
        studentSignBody.setType(signType.getType());
        switch (type){
            case BUSES:
                studentSignBody.setBusId(student.getBusId());
                break;
            default:
                studentSignBody.setBusId(0);
                break;
        }
        /*是否设置温度默认值*/
        if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
            studentSignBody.setTemperature(36.5f);
        }
    }

    /*接送家长ID*/
    @Override
    public void pickUpId(int id) {
        studentSignBody.setPickupid(id);
    }

    @Override
    public void setTempValue(String tempValue) {
        studentSignBody.setTemperature(Float.parseFloat(tempValue));
        if (Float.parseFloat(tempValue)>=37.6f){
            VibratorUtil.getInstance(this).vibrateAndPlayTone();
            if (!SharePrefUtils.getBoolean(Constant.HEALTH_CHECK,false)){
                Bundle bundleHealth = new Bundle();
                bundleHealth.putSerializable(Constant.TYPE, type);
                bundleHealth.putSerializable(Constant.SIGN_TYPE, signType);
                bundleHealth.putSerializable(Constant.GENERAL_TYPE, GeneralTypeEnum.IS_GENERAL);
                bundleHealth.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
                openActivity(HealthCheckActivity.class, bundleHealth);
            }
        }
    }

    @Override
    protected void signRequest() {
        /*判断学生考勤拍照（图片信息上传）*/
        if (!SharePrefUtils.getBoolean(Constant.IS_STUDENT_CHECK_CAMERA)){
            mPresenter.studentSignRequest(getSignRequestBody());
        }
    }

    private StudentSignBody getSignRequestBody() {
        studentSignBody.setDate(createTime);
        studentSignBody.setRemarks("");
        studentSignBody.setCommon("");
        return studentSignBody;
    }

    /*签到成功*/
    @Override
    public void studentSignSuccess(SignResponse signResponse) {
        /*设置值*/
        StudentsBean studentSign = mainSchoolContentModel.getStudentsBean();
        studentSign.setSign(true);
        studentSign.setSingType(signType);
        if (signType.equals(SingTypeEnum.SIGN_IN)){
            studentSign.setHasSignIn(true);
        }
        mainSchoolContentModel.setStudentsBean(studentSign);
        EventBusUtil.sendEvent(new Event(0,mainSchoolContentModel));
        /*判断如果是强制体测则跳转健康监测界面，反之销毁*/
        if (SharePrefUtils.getBoolean(Constant.HEALTH_CHECK, false)) {
            Bundle bundleHealth = new Bundle();
            bundleHealth.putSerializable(Constant.TYPE, type);
            bundleHealth.putSerializable(Constant.SIGN_TYPE, signType);
            bundleHealth.putSerializable(Constant.GENERAL_TYPE, GeneralTypeEnum.NO_GENERAL);
            bundleHealth.putSerializable(Constant.SCHOOL_CONTENT_MODEL, mainSchoolContentModel);
            openActivity(HealthCheckActivity.class, bundleHealth);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void defaultFinish() {
        finish();
    }

    /*拍照返回图片地址*/
    @Override
    protected void cameraFile(String fileName) {
        /*考勤拍照（图片上传）（是否需要链式调用接口）*/
        mPresenter.studentSignRequest(getSignRequestBody());
        if (SharePrefUtils.getBoolean(Constant.IS_STUDENT_CHECK_CAMERA)){
            mPresenter.studentPictureInput(mainSchoolContentModel.getStudentsBean().getCenterStudentId(),createTime,fileName);
        }
    }

}
