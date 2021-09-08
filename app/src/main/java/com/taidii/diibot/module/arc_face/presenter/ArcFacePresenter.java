package com.taidii.diibot.module.arc_face.presenter;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.RelationBean;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.module.arc_face.contract.ArcFaceContract;
import com.taidii.diibot.module.arc_face.faceserver.CompareResult;
import com.taidii.diibot.module.arc_face.model.ArcFaceModel;

import java.util.ArrayList;
import java.util.List;

public class ArcFacePresenter extends BasePresenter<ArcFaceContract.Model,ArcFaceContract.View> implements ArcFaceContract.Presenter{

    @Override
    protected ArcFaceContract.Model createModel() {
        return new ArcFaceModel();
    }

    @Override
    public void mateFace(CompareResult compareResult, List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardianList) {
        /*通过类型进行比对*/
        switch (compareResult.getType()){
            case Constant.FACE_STUDENT:
                mateStudent(compareResult,allCollectionList);
                break;
            case Constant.FACE_STAFF:
                mateTeacher(compareResult,allCollectionList);
                break;
            case Constant.FACE_GUARDIAN:
                mateParent(compareResult,allCollectionList,guardianList);
                break;
        }
    }

    /*扫码，家长类型，匹配出对应的学生或者学生列表*/
    private void mateParent(CompareResult compareResult, List<MainSchoolCollectionModel> allCollectionList, List<GuardiansBean> guardiansList){
        /*首先通过扫码id与家长匹配，匹配出对应的家长，再通过家长匹配出对应的学生*/
        GuardiansBean mateGuardians = null;
        if (guardiansList.size()>0){
            for (GuardiansBean guardiansBean : guardiansList) {
                if (guardiansBean.getId()==compareResult.getId()){
                    mateGuardians = guardiansBean;
                }
            }
        }else {
            getView().showMessage(R.string.face_mate_fail);
        }
        /*判断是否匹配到家长*/
        if (mateGuardians!=null){
            /*匹配出的学生*/
            List<QrCodeStudentCollection> qrCodeStudentList = new ArrayList<>();
            /*通过家长反查学生*/
            for (MainSchoolCollectionModel collectionModel : allCollectionList){
                /*仅匹配学生*/
                switch (collectionModel.getType()){
                    case CLASS:
                        /*开始匹配学生（问题一、如果校车中有该孩子呢）*/
                        List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                        if (contentModelList.size()>0){
                            for (MainSchoolContentModel contentModel : contentModelList){
                                QrCodeStudentCollection collection = new QrCodeStudentCollection();
                                if (mateGuardians.getRelation().size()>0){
                                    for (RelationBean relationBean : mateGuardians.getRelation()){
                                        if (contentModel.getStudentsBean().getCenterStudentId() == relationBean.getStudentId()){
                                            collection.setContentModel(contentModel);
                                            collection.setType(collectionModel.getType());
                                            qrCodeStudentList.add(collection);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }
            }
            /*判断是否匹配出学生*/
            if (qrCodeStudentList.size()>0){
                getView().faceResultSingStudent(qrCodeStudentList);
            }else {
                getView().showMessage(R.string.face_mate_fail);
            }
        }else {
            getView().showMessage(R.string.face_mate_fail);
        }
    }

    /*扫码，学生类型，匹配出对应的学生或者学生列表*/
    private void mateStudent(CompareResult compareResult,List<MainSchoolCollectionModel> allCollectionList){
        /*匹配出的学生*/
        List<QrCodeStudentCollection> qrCodeStudentList = new ArrayList<>();
        for (MainSchoolCollectionModel collectionModel : allCollectionList) {
            /*仅匹配学生*/
            switch (collectionModel.getType()) {
                case CLASS:
                    List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                    if (contentModelList.size() > 0) {
                        for (MainSchoolContentModel contentModel : contentModelList) {
                            QrCodeStudentCollection collection = new QrCodeStudentCollection();
                            if (contentModel.getStudentsBean()!=null&&contentModel.getStudentsBean().getCenterStudentId()==compareResult.getId()) {
                                collection.setContentModel(contentModel);
                                collection.setType(collectionModel.getType());
                                qrCodeStudentList.add(collection);
                            }
                        }
                    }
                    break;
            }
        }
        /*判断是否匹配出学生*/
        if (qrCodeStudentList.size()>0){
            getView().faceResultSingStudent(qrCodeStudentList);
        }else {
            getView().showMessage(R.string.face_mate_fail);
        }
    }

    /*扫码，老师类型，匹配出对应的学生或者学生列表*/
    private void mateTeacher(CompareResult compareResult,List<MainSchoolCollectionModel> allCollectionList){
        SchoolMainEnum type = null;
        MainSchoolContentModel schoolContentModel = null;
        for (MainSchoolCollectionModel collectionModel : allCollectionList) {
            /*仅匹配学生*/
            switch (collectionModel.getType()) {
                case STAFF:
                    List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                    if (contentModelList.size() > 0) {
                        for (MainSchoolContentModel contentModel : contentModelList) {
                            if (contentModel.getStaffsBean()!=null&&contentModel.getStaffsBean().getStaffId()==compareResult.getId()) {
                                type = collectionModel.getType();
                                schoolContentModel = contentModel;
                            }
                        }
                    }
                    break;
            }
        }
        if (type!=null && schoolContentModel!=null){
            getView().faceResultSignStaff(type,schoolContentModel);
        }else {
            getView().showMessage(R.string.face_mate_fail);
        }
    }

}
