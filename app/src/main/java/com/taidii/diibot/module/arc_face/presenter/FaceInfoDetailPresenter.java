package com.taidii.diibot.module.arc_face.presenter;

import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.entity.school.RelationBean;
import com.taidii.diibot.module.arc_face.contract.FaceInfoDetailContract;
import com.taidii.diibot.module.arc_face.model.FaceInfoDetailModel;

import java.util.ArrayList;
import java.util.List;

public class FaceInfoDetailPresenter extends BasePresenter<FaceInfoDetailContract.Model,FaceInfoDetailContract.View> implements FaceInfoDetailContract.Presenter{

    @Override
    protected FaceInfoDetailContract.Model createModel() {
        return new FaceInfoDetailModel();
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
                        mateGuardianBean.setFaceImage(guardiansBean.getFaceImage());
                        mateGuardianBean.setRegisterFace(guardiansBean.isRegisterFace());
                        mateGuardianBean.setRelation(relationBean.getRelation());
                        mateGuardianList.add(mateGuardianBean);
                    }
                }
            }
        }
        getView().mateGuardianResult(mateGuardianList);
    }

}
