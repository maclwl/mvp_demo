package com.taidii.diibot.module.school_main.presenter;

import com.taidii.diibot.R;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.RelationBean;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.module.school_main.contract.SignContract;
import com.taidii.diibot.module.school_main.model.SignModel;
import com.taidii.diibot.net.ResultCallback;

import java.util.ArrayList;
import java.util.List;

public class SignPresenter extends BasePresenter<SignContract.Model,SignContract.View> implements SignContract.Presenter{

    @Override
    protected SignContract.Model createModel() {
        return new SignModel();
    }

    /*匹配出孩子的接送家长*/
    @Override
    public void mateGuardian(List<GuardiansBean> guardianList, MainSchoolContentModel mainSchoolContentModel) {
        List<MateGuardianBean> mateGuardianList = new ArrayList<>();//匹配结果
        //通过RelationBea中的studentId匹配
        for (GuardiansBean guardiansBean : guardianList){
            if (guardiansBean.getRelation().size()>0){
                for (RelationBean relationBean : guardiansBean.getRelation()){
                    if (relationBean.getStudentId()==mainSchoolContentModel.getStudentsBean().getCenterStudentId()){
                        MateGuardianBean mateGuardianBean = new MateGuardianBean();
                        mateGuardianBean.setId(guardiansBean.getId());
                        mateGuardianBean.setName(guardiansBean.getName());
                        mateGuardianBean.setGender(guardiansBean.getGender());
                        mateGuardianBean.setAvatar(guardiansBean.getAvatar());
                        mateGuardianBean.setRelation(relationBean.getRelation());
                        mateGuardianList.add(mateGuardianBean);
                    }
                }
            }
        }
        getView().mateGuardianResult(mateGuardianList);
    }

    @Override
    public void checkSelectGuardian(List<MateGuardianBean> guardianBeanList, int position) {
        List<MateGuardianBean> guardianResultList = new ArrayList<>();
        MateGuardianBean clickGuardianBean = guardianBeanList.get(position);
        /*重置所有为未选中,根据position设置选中*/
        for (MateGuardianBean mateGuardianBean : guardianBeanList){
            /*判断点击的接送家长*/
            if (mateGuardianBean.getId()==clickGuardianBean.getId()){
                if (clickGuardianBean.isCheck()){
                    mateGuardianBean.setCheck(false);
                    guardianResultList.add(mateGuardianBean);
                    getView().selectGuard(0);
                }else {
                    mateGuardianBean.setCheck(true);
                    guardianResultList.add(mateGuardianBean);
                    getView().selectGuard(mateGuardianBean.getId());
                }
            }else {
                mateGuardianBean.setCheck(false);
                guardianResultList.add(mateGuardianBean);
            }
        }
        getView().checkSelectGuardianResult(guardianResultList);
    }

    @Override
    public void studentSignRequest(StudentSignBody studentSignBody) {
        getModel().studentSignRequest(studentSignBody, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                switch (signResponse.getStatusCode()){
                    case 0:
                        getView().showMessage(R.string.sign_fail);
                        break;
                    case 1:
                        getView().studentSignSuccess(signResponse);
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void staffSignRequest(StaffSignBody staffSignBody) {
        getModel().staffSignRequest(staffSignBody, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                switch (signResponse.getStatusCode()){
                    case 0:
                        getView().showMessage(R.string.sign_fail);
                        break;
                    case 1:
                        getView().staffSignSuccess(signResponse);
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void studentPictureInput(int studentId, String date, String fileName) {
        getModel().studentPictureInput(studentId, date, fileName, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void staffPictureInput(int staffId, String date, String fileName) {
        getModel().staffPictureInput(staffId, date, fileName, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

}
