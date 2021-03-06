package com.taidii.diibot.module.school_main.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.school.BusesBean;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.HealthCheckCacheDate;
import com.taidii.diibot.entity.school.HealthCheckResponse;
import com.taidii.diibot.entity.school.LocalSchoolData;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.entity.school.QrCodeDecryptBean;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.RelationBean;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.SignResponse;
import com.taidii.diibot.entity.school.StaffPictureCacheData;
import com.taidii.diibot.entity.school.StaffSignBody;
import com.taidii.diibot.entity.school.StaffSignCacheData;
import com.taidii.diibot.entity.school.StaffattendanceBean;
import com.taidii.diibot.entity.school.StaffsBean;
import com.taidii.diibot.entity.school.StudentPictureCacheData;
import com.taidii.diibot.entity.school.StudentSignBody;
import com.taidii.diibot.entity.school.StudentSignCacheData;
import com.taidii.diibot.entity.school.StudentattendanceBean;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.module.school_main.contract.SchoolMainContract;
import com.taidii.diibot.module.school_main.model.SchoolMainModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.DESUtil;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.GsonUtil;
import com.taidii.diibot.utils.LocalOffLineDataUtil;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SchoolMainPresenter extends BasePresenter<SchoolMainContract.Model,SchoolMainContract.View> implements SchoolMainContract.Presenter{

    @Override
    protected SchoolMainContract.Model createModel() {
        return new SchoolMainModel();
    }

    /*????????????????????????*/
    @Override
    public void getDesInfo() {
        getModel().getDesInfo(new ResultCallback<DesInfoBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DesInfoBean desInfoBean) {
                /*????????????????????????*/
                if (desInfoBean!=null){
                    SharePrefUtils.saveString(Constant.DES_KEY, desInfoBean.getDes_key());
                    SharePrefUtils.saveString(Constant.DES_IV, desInfoBean.getDes_iv());
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*??????????????????*/
    @Override
    public void decrypt(String result, List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardiansList) {
        /*??????????????????????????????key???????????????????????????*/
        String iv = SharePrefUtils.getString(Constant.DES_IV);
        String des_key = SharePrefUtils.getString(Constant.DES_KEY);
        if (!TextUtils.isEmpty(des_key)){
            /*??????*/
            try {
                String desResult = DESUtil.decryptDES(result,iv,des_key);
                /*gson??????*/
                QrCodeDecryptBean decryptBean = GsonUtil.GsonToBean(desResult,QrCodeDecryptBean.class);
                switch (decryptBean.getType()){
                    case PARENT:
                        mateParent(decryptBean,allCollectionList,guardiansList);
                        break;
                    case TEACHER:
                        mateTeacher(decryptBean,allCollectionList);
                        break;
                    case STUDENT:
                        mateStudent(decryptBean,allCollectionList);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*??????????????????*/
    @Override
    public void mateSearch(String keyword, List<MainSchoolCollectionModel> currentModelList) {
        List<MainSchoolCollectionModel> newModelList = new ArrayList<>();//??????????????????
        /*??????????????????????????????????????????*/
        for (MainSchoolCollectionModel model : currentModelList){
            MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
            List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
            /*????????????*/
            switch (model.getType()){
                case CLASS:
                case BUSES:
                    mainSchoolCollectionModel.setModelName(model.getModelName());
                    mainSchoolCollectionModel.setType(model.getType());
                    for (MainSchoolContentModel contentModel : model.getMainSchoolContentModelList()){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/
                        if (Utils.exChangeLower(contentModel.getStudentsBean().getName()).contains(Utils.exChangeLower(keyword))){
                            mainSchoolContentModel.setStudentsBean(contentModel.getStudentsBean());
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*???????????????????????????????????????*/
                    if (mainSchoolContentModelList.size()>0){
                        mainSchoolCollectionModel.setSpread(true);
                        newModelList.add(mainSchoolCollectionModel);
                    }
                    break;
                case STAFF:
                    mainSchoolCollectionModel.setModelName(model.getModelName());
                    mainSchoolCollectionModel.setType(model.getType());
                    for (MainSchoolContentModel contentModel : model.getMainSchoolContentModelList()){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*??????????????????????????????????????????????????????*/
                        if (Utils.exChangeLower(contentModel.getStaffsBean().getName()).contains(Utils.exChangeLower(keyword))){
                            mainSchoolContentModel.setStaffsBean(contentModel.getStaffsBean());
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*???????????????????????????????????????*/
                    if (mainSchoolContentModelList.size()>0){
                        mainSchoolCollectionModel.setSpread(true);
                        newModelList.add(mainSchoolCollectionModel);
                    }
                    break;
            }
        }
        getView().mateSearchResult(newModelList,keyword);
    }

    /*??????????????????????????????????????????????????????????????????*/
    private void mateParent(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardiansList){
        /*??????????????????id????????????????????????????????????????????????????????????????????????????????????*/
        GuardiansBean mateGuardians = null;
        if (guardiansList.size()>0){
            for (GuardiansBean guardiansBean : guardiansList) {
                if (guardiansBean.getId() == decryptBean.getId_num()){
                    mateGuardians = guardiansBean;
                }
            }
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
        /*???????????????????????????*/
        if (mateGuardians!=null){
            /*??????????????????*/
            List<QrCodeStudentCollection> qrCodeStudentList = new ArrayList<>();
            /*????????????????????????*/
            for (MainSchoolCollectionModel collectionModel : allCollectionList){
                /*???????????????*/
                switch (collectionModel.getType()){
                    case CLASS:
                        /*??????????????????????????????????????????????????????????????????*/
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
            /*???????????????????????????*/
            if (qrCodeStudentList.size()>0){
                getView().qrResultSingStudent(qrCodeStudentList);
            }else {
                getView().showMessage(R.string.qr_code_info_error);
            }
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*??????????????????????????????????????????????????????????????????*/
    private void mateStudent(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList){
        /*??????????????????*/
        List<QrCodeStudentCollection> qrCodeStudentList = new ArrayList<>();
        for (MainSchoolCollectionModel collectionModel : allCollectionList) {
            /*???????????????*/
            switch (collectionModel.getType()) {
                case CLASS:
                    List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                    if (contentModelList.size() > 0) {
                        for (MainSchoolContentModel contentModel : contentModelList) {
                            QrCodeStudentCollection collection = new QrCodeStudentCollection();
                            if (contentModel.getStudentsBean()!=null&&contentModel.getStudentsBean().getCenterStudentId() == decryptBean.getId_num()) {
                                collection.setContentModel(contentModel);
                                collection.setType(collectionModel.getType());
                                qrCodeStudentList.add(collection);
                            }
                        }
                    }
                    break;
            }
        }
        /*???????????????????????????*/
        if (qrCodeStudentList.size()>0){
            getView().qrResultSingStudent(qrCodeStudentList);
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*??????????????????????????????????????????????????????????????????*/
    private void mateTeacher(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList){
        SchoolMainEnum type = null;
        MainSchoolContentModel schoolContentModel = null;
        for (MainSchoolCollectionModel collectionModel : allCollectionList) {
            /*???????????????*/
            switch (collectionModel.getType()) {
                case STAFF:
                    List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                    if (contentModelList.size() > 0) {
                        for (MainSchoolContentModel contentModel : contentModelList) {
                            if (contentModel.getStaffsBean()!=null&&contentModel.getStaffsBean().getStaffId() == decryptBean.getId_num()) {
                                type = collectionModel.getType();
                                schoolContentModel = contentModel;
                            }
                        }
                    }
                    break;
            }
        }
        if (type!=null && schoolContentModel!=null){
            getView().qrResultSignStaff(type,schoolContentModel);
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    @Override
    public void getLocalSchoolData(String username, String password) {
        List<GuardiansBean> anllGuardiansList = new ArrayList<>();
        getModel().getLocalSchoolData(username, password, new ResultCallback<LocalSchoolData>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(LocalSchoolData localSchoolData) {
                /*????????????????????????????????????*/
                SharePrefUtils.saveBoolean(Constant.HEALTH_CHECK, localSchoolData.isMandatory_health_check());
                /*???????????????????????????????????????*/
                SharePrefUtils.saveBoolean(Constant.SAFE_ACCESS, localSchoolData.isCovid_safe_access());
                /*topBar????????????*/
                getView().topBarInfo(localSchoolData.getCenter_name(),localSchoolData.getCenter_logo());
                GlobalParams.center_logo = localSchoolData.getCenter_logo();
                GlobalParams.center_name = localSchoolData.getCenter_name();
                /*????????????????????????---???????????????*/
                anllGuardiansList.addAll(localSchoolData.getGuardians());
                if (localSchoolData.getAuthorizedPickups()!=null&&localSchoolData.getAuthorizedPickups().size()>0){
                    anllGuardiansList.addAll(localSchoolData.getAuthorizedPickups());
                }
                getView().guardiansList(anllGuardiansList);
                /*??????????????????*/
                mateClassModel(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*????????????*/
    @Override
    public void getLocalSchoolDataCount(String username, String password) {
        getModel().getLocalSchoolData(username, password, new ResultCallback<LocalSchoolData>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(LocalSchoolData localSchoolData) {
                /*??????????????????????????????*/
                mateClassModelCount(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*???????????????????????????????????????????????????????????????????????????*/
    @Override
    public void initSettingList(List<MainSchoolCollectionModel> allCollectionList) {
        List<MainSchoolCollectionModel> initAllList = new ArrayList<>();
        if (allCollectionList.size()>0){
            /*????????????*/
            for (MainSchoolCollectionModel collectionModel : allCollectionList){
                collectionModel.setSpread(SharePrefUtils.getBoolean(Constant.IS_SPREAD_ALL));
                initAllList.add(collectionModel);
            }
            /*??????????????????*/
            if (!SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK)){
                for (int i=0;i<initAllList.size();i++){
                    if (initAllList.get(i).getModelName().equals("Staff")){
                        initAllList.remove(i);
                    }
                }
            }
        }
       getView().settingCollectionResult(initAllList);
    }

    /*??????????????????*/
    @Override
    public void removeStaff(List<MainSchoolCollectionModel> allCollectionList) {
        List<MainSchoolCollectionModel> removeStaffList = new ArrayList<>();
        for (MainSchoolCollectionModel mainSchoolCollectionModel : allCollectionList){
            if (!mainSchoolCollectionModel.getModelName().equals("Staff")){
                removeStaffList.add(mainSchoolCollectionModel);
            }
        }
        getView().settingCollectionResult(removeStaffList);
    }

    /*??????????????????*/
    @Override
    public void spreadSetting(List<MainSchoolCollectionModel> allCollectionList, boolean isCheck) {
        List<MainSchoolCollectionModel> spreadList = new ArrayList<>();
        for (MainSchoolCollectionModel collectionModel : allCollectionList){
            collectionModel.setSpread(isCheck);
            spreadList.add(collectionModel);
        }
        getView().settingCollectionResult(spreadList);
    }

    @Override
    public void hideSignInSetting(List<MainSchoolCollectionModel> allCollectionList) {
        List<MainSchoolCollectionModel> hideSignInList = new ArrayList<>();
        for (MainSchoolCollectionModel mainSchoolCollectionModel : allCollectionList){
            switch (mainSchoolCollectionModel.getType()){
                case CLASS:
                case BUSES:
                    List<MainSchoolContentModel> studentList = new ArrayList<>();
                    for (MainSchoolContentModel mainSchoolContentModel:mainSchoolCollectionModel.getMainSchoolContentModelList()){
                        if ((! mainSchoolContentModel.getStudentsBean().isSign()
                                || ! mainSchoolContentModel.getStudentsBean().getSingType().equals(SingTypeEnum.SIGN_IN))
                                && ! mainSchoolContentModel.getStudentsBean().isHasSignIn()){
                            studentList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelListHide(studentList);
                    break;
                case STAFF:
                    List<MainSchoolContentModel> studentList1 = new ArrayList<>();
                    for (MainSchoolContentModel mainSchoolContentModel:mainSchoolCollectionModel.getMainSchoolContentModelList()){
                        if ((! mainSchoolContentModel.getStaffsBean().isSign()
                                || ! mainSchoolContentModel.getStaffsBean().getSignType().equals(SingTypeEnum.SIGN_IN))
                                && ! mainSchoolContentModel.getStaffsBean().isHasSignIn()){
                            studentList1.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelListHide(studentList1);
                    break;
            }
            hideSignInList.add(mainSchoolCollectionModel);
        }
        getView().hintSignInList(hideSignInList);
    }

    /*????????????????????????*/
    private void mateClassModel(LocalSchoolData localSchoolData){
        List<MainSchoolCollectionModel> classModelList = new ArrayList<>();//??????????????????
        List<StudentsBean> studentList = localSchoolData.getStudents();//????????????
        List<StaffsBean> staffList = localSchoolData.getStaffs();//????????????
        List<StudentsBean> signStudentList = new ArrayList<>();//??????????????????
        List<StudentsBean> signedStudentList = new ArrayList<>();//?????????????????????
        List<StudentsBean> signedOutStudentList = new ArrayList<>();//?????????????????????
        List<StaffsBean> signStaffList = new ArrayList<>();//??????????????????
        int totalCount = studentList.size();
        int signInCount = 0;
        int signoutCount = 0;
        /*??????????????????????????????????????????????????????*/
        //????????????,???????????????
        if (localSchoolData.getStudentattendance().size()>0){
            for (StudentsBean studentsBean : studentList){
                for (StudentattendanceBean studentattendanceBean : localSchoolData.getStudentattendance()){
                    if (studentattendanceBean.getStudentId() == studentsBean.getCenterStudentId()){
                        /*???????????????????????????*/
                        switch (studentattendanceBean.getType()){
                            case 0:
                                studentsBean.setSign(true);
                                studentsBean.setSingType(SingTypeEnum.SIGN_IN);
                                boolean hasAdd = false;
                                for (StudentsBean studentsBean1 : signedStudentList){
                                    if (studentattendanceBean.getStudentId() == studentsBean1.getCenterStudentId()){
                                        hasAdd = true;
                                        break;
                                    }
                                }
                                if (!hasAdd){
                                    studentsBean.setHasSignIn(true);
                                    signedStudentList.add(studentsBean);
                                    signInCount++;
                                }
                                break;
                            case 1:
                                studentsBean.setSign(true);
                                studentsBean.setSingType(SingTypeEnum.SIGN_OUT);
                                boolean hasAdd1 = false;
                                for (StudentsBean studentsBean1 : signedOutStudentList){
                                    if (studentattendanceBean.getStudentId() == studentsBean1.getCenterStudentId()){
                                        hasAdd1 = true;
                                        break;
                                    }
                                }
                                if (!hasAdd1){
                                    signedOutStudentList.add(studentsBean);
                                    signoutCount++;
                                }
                                break;
                            case 4:
                                studentsBean.setSign(true);
                                studentsBean.setSingType(SingTypeEnum.SIGN_TEMP);
                                break;
                            case 3:
                            case 2:
                                studentsBean.setHealthCheck(true);
                                break;
                        }
                    }
                }
                signStudentList.add(studentsBean);
            }
        }else {
            signStudentList = studentList;
        }
        //????????????
        if (localSchoolData.getStaffattendance().size()>0){
            for (StaffsBean staffsBean : staffList){
                for (StaffattendanceBean staffattendanceBean : localSchoolData.getStaffattendance()){
                    if (staffsBean.getStaffId() == staffattendanceBean.getStaffId()){
                        /*???????????????????????????*/
                        staffsBean.setSign(true);
                        switch (staffattendanceBean.getType()){
                            case 0:
                                staffsBean.setSignType(SingTypeEnum.SIGN_IN);
                                break;
                            case 1:
                                staffsBean.setSignType(SingTypeEnum.SIGN_OUT);
                                break;
                            case 4:
                                staffsBean.setSignType(SingTypeEnum.SIGN_TEMP);
                                break;
                        }
                    }
                }
                signStaffList.add(staffsBean);
            }
        }else {
            signStaffList = staffList;
        }

        /*????????????????????????1.?????????classOrder??? 2.?????????buses??? 3.?????????staffs???*/
        if (localSchoolData!=null){
            //1.??????????????????????????????????????????????????????
            if (localSchoolData.getClassOrder().size()>0){
                for (String className : localSchoolData.getClassOrder()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(className);
                    mainSchoolCollectionModel.setType(SchoolMainEnum.CLASS);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*??????????????????????????????????????????????????????*/
                        if (className.equals(studentsBean.getClassname())){
                            mainSchoolContentModel.setStudentsBean(studentsBean);
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*???????????????????????????????????????*/
                    if (mainSchoolContentModelList.size()>0){
                        classModelList.add(mainSchoolCollectionModel);
                    }
                }
            }
            //2.??????????????????????????????id????????????????????????
            if (localSchoolData.getBuses().size()>0){
                for (BusesBean busesBean : localSchoolData.getBuses()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(busesBean.getDescription());
                    mainSchoolCollectionModel.setType(SchoolMainEnum.BUSES);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*??????bus????????????id?????????*/
                        for (Integer id : busesBean.getStudentIds()){
                            if (id == studentsBean.getCenterStudentId()){
                                /*????????????ID*/
                                studentsBean.setBusId(busesBean.getBusId());
                                mainSchoolContentModel.setStudentsBean(studentsBean);
                                mainSchoolContentModelList.add(mainSchoolContentModel);
                            }
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    if (mainSchoolContentModelList.size()>0){
                        classModelList.add(mainSchoolCollectionModel);
                    }
                }
            }
            //3.?????????
            if (signStaffList.size()>0){
                MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                mainSchoolCollectionModel.setModelName("Staff");
                mainSchoolCollectionModel.setType(SchoolMainEnum.STAFF);
                for (StaffsBean staffsBean : signStaffList){
                    MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                    mainSchoolContentModel.setStaffsBean(staffsBean);
                    mainSchoolContentModelList.add(mainSchoolContentModel);
                }
                mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                if (mainSchoolContentModelList.size()>0){
                    classModelList.add(mainSchoolCollectionModel);
                }
            }
            getView().schoolCollection(classModelList);
            getView().setTitleCount(totalCount,signInCount,signoutCount);
        }
    }

    /*????????????????????????*/
    private void mateClassModelCount(LocalSchoolData localSchoolData){
        List<MainSchoolCollectionModel> classModelList = new ArrayList<>();//??????????????????
        List<StudentsBean> studentList = localSchoolData.getStudents();//????????????
        List<StaffsBean> staffList = localSchoolData.getStaffs();//????????????
        List<StudentsBean> signStudentList = new ArrayList<>();//??????????????????
        List<StudentsBean> signedStudentList = new ArrayList<>();//?????????????????????
        List<StudentsBean> signedOutStudentList = new ArrayList<>();//?????????????????????
        List<StaffsBean> signStaffList = new ArrayList<>();//??????????????????
        int totalCount = studentList.size();
        int signInCount = 0;
        int signoutCount = 0;
        /*??????????????????????????????????????????????????????*/
        //????????????,???????????????
        if (localSchoolData.getStudentattendance().size()>0){
            for (StudentsBean studentsBean : studentList){
                for (StudentattendanceBean studentattendanceBean : localSchoolData.getStudentattendance()){
                    if (studentattendanceBean.getStudentId() == studentsBean.getCenterStudentId()){
                        /*???????????????????????????*/
                        studentsBean.setSign(true);
                        switch (studentattendanceBean.getType()){
                            case 0:
                                studentsBean.setSingType(SingTypeEnum.SIGN_IN);
                                boolean hasAdd = false;
                                for (StudentsBean studentsBean1 : signedStudentList){
                                    if (studentattendanceBean.getStudentId() == studentsBean1.getCenterStudentId()){
                                        hasAdd = true;
                                        break;
                                    }
                                }
                                if (!hasAdd){
                                    signedStudentList.add(studentsBean);
                                    signInCount++;
                                }
                                break;
                            case 1:
                                studentsBean.setSingType(SingTypeEnum.SIGN_OUT);
                                boolean hasAdd1 = false;
                                for (StudentsBean studentsBean1 : signedOutStudentList){
                                    if (studentattendanceBean.getStudentId() == studentsBean1.getCenterStudentId()){
                                        hasAdd1 = true;
                                        break;
                                    }
                                }
                                if (!hasAdd1){
                                    signedOutStudentList.add(studentsBean);
                                    signoutCount++;
                                }
                                break;
                            case 4:
                                studentsBean.setSingType(SingTypeEnum.SIGN_TEMP);
                                break;
                        }
                    }
                }
                signStudentList.add(studentsBean);
            }
        }else {
            signStudentList = studentList;
        }
        //????????????
        if (localSchoolData.getStaffattendance().size()>0){
            for (StaffsBean staffsBean : staffList){
                for (StaffattendanceBean staffattendanceBean : localSchoolData.getStaffattendance()){
                    if (staffsBean.getStaffId() == staffattendanceBean.getStaffId()){
                        /*???????????????????????????*/
                        staffsBean.setSign(true);
                        switch (staffattendanceBean.getType()){
                            case 0:
                                staffsBean.setSignType(SingTypeEnum.SIGN_IN);
                                break;
                            case 1:
                                staffsBean.setSignType(SingTypeEnum.SIGN_OUT);
                                break;
                            case 4:
                                staffsBean.setSignType(SingTypeEnum.SIGN_TEMP);
                                break;
                        }
                    }
                }
                signStaffList.add(staffsBean);
            }
        }else {
            signStaffList = staffList;
        }

        /*????????????????????????1.?????????classOrder??? 2.?????????buses??? 3.?????????staffs???*/
        if (localSchoolData!=null){
            //1.??????????????????????????????????????????????????????
            if (localSchoolData.getClassOrder().size()>0){
                for (String className : localSchoolData.getClassOrder()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(className);
                    mainSchoolCollectionModel.setType(SchoolMainEnum.CLASS);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*??????????????????????????????????????????????????????*/
                        if (className.equals(studentsBean.getClassname())){
                            mainSchoolContentModel.setStudentsBean(studentsBean);
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    classModelList.add(mainSchoolCollectionModel);
                }
            }
            //2.??????????????????????????????id????????????????????????
            if (localSchoolData.getBuses().size()>0){
                for (BusesBean busesBean : localSchoolData.getBuses()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(busesBean.getDescription());
                    mainSchoolCollectionModel.setType(SchoolMainEnum.BUSES);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*??????bus????????????id?????????*/
                        for (Integer id : busesBean.getStudentIds()){
                            if (id == studentsBean.getCenterStudentId()){
                                /*????????????ID*/
                                studentsBean.setBusId(busesBean.getBusId());
                                mainSchoolContentModel.setStudentsBean(studentsBean);
                                mainSchoolContentModelList.add(mainSchoolContentModel);
                            }
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    classModelList.add(mainSchoolCollectionModel);
                }
            }
            //3.?????????
            if (signStaffList.size()>0){
                MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                mainSchoolCollectionModel.setModelName("Staff");
                mainSchoolCollectionModel.setType(SchoolMainEnum.STAFF);
                for (StaffsBean staffsBean : signStaffList){
                    MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                    mainSchoolContentModel.setStaffsBean(staffsBean);
                    mainSchoolContentModelList.add(mainSchoolContentModel);
                }
                mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                classModelList.add(mainSchoolCollectionModel);
            }
            getView().setTitleCount(totalCount,signInCount,signoutCount);
        }
    }

    /*????????????????????????*/
    @Override
    public void signBackRefresh(List<MainSchoolCollectionModel> collectionModelList, int collectionPosition, int contentPosition, MainSchoolContentModel signContentModel,List<MainSchoolCollectionModel> allCollectionList) {
        /*????????????id ??????id??????*/
        List<MainSchoolCollectionModel> newCollectionModelList = new ArrayList<>();
        if (signContentModel.getStudentsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : collectionModelList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStudentsBean()!=null){
                        StudentsBean studentsBean = contentModel.getStudentsBean();
                        /*????????????id*/
                        if (signContentModel.getStudentsBean().getCenterStudentId()== studentsBean.getCenterStudentId()){
                            studentsBean.setSign(signContentModel.getStudentsBean().isSign());
                            studentsBean.setHealthCheck(signContentModel.getStudentsBean().isHealthCheck());
                            studentsBean.setSingType(signContentModel.getStudentsBean().getSingType());
                            contentModel.setStudentsBean(studentsBean);
                            newContentModelList.add(contentModel);
                        }else {
                            newContentModelList.add(contentModel);
                        }
                    }else {
                        if (contentModel.getStaffsBean()!=null){
                            newContentModelList.add(contentModel);
                        }
                    }
                }
                collectionModel.setMainSchoolContentModelList(newContentModelList);
                newCollectionModelList.add(collectionModel);
            }
            getView().signBackRefreshList(newCollectionModelList);
        }
        /*??????????????????*/
        if (signContentModel.getStaffsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : collectionModelList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStaffsBean()!=null){
                        StaffsBean staffsBean = contentModel.getStaffsBean();
                        /*????????????id*/
                        if (signContentModel.getStaffsBean().getStaffId()== staffsBean.getStaffId()){
                            staffsBean.setSign(signContentModel.getStaffsBean().isSign());
                            staffsBean.setSignType(signContentModel.getStaffsBean().getSignType());
                            contentModel.setStaffsBean(staffsBean);
                            newContentModelList.add(contentModel);
                        }else {
                            newContentModelList.add(contentModel);
                        }
                    }else {
                        if (contentModel.getStudentsBean()!=null){
                            newContentModelList.add(contentModel);
                        }
                    }
                }
                collectionModel.setMainSchoolContentModelList(newContentModelList);
                newCollectionModelList.add(collectionModel);
            }
            getView().signBackRefreshList(newCollectionModelList);
        }

        /*?????????????????????????????????????????????????????????????????????????????????????????????????????????*/
        List<MainSchoolCollectionModel> newAllCollectionList = new ArrayList<>();
        if (!SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK) && signContentModel.getStaffsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : allCollectionList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStaffsBean()!=null){
                        StaffsBean staffsBean = contentModel.getStaffsBean();
                        /*????????????id*/
                        if (signContentModel.getStaffsBean().getStaffId()== staffsBean.getStaffId()){
                            staffsBean.setSign(signContentModel.getStaffsBean().isSign());
                            staffsBean.setSignType(signContentModel.getStaffsBean().getSignType());
                            contentModel.setStaffsBean(staffsBean);
                            newContentModelList.add(contentModel);
                        }else {
                            newContentModelList.add(contentModel);
                        }
                    }else {
                        if (contentModel.getStudentsBean()!=null){
                            newContentModelList.add(contentModel);
                        }
                    }
                }
                collectionModel.setMainSchoolContentModelList(newContentModelList);
                newAllCollectionList.add(collectionModel);
            }
            getView().signBackAllCollectionModelList(newAllCollectionList);
        }
    }

    /*????????????*/
    @Override
    public void quickSign(SchoolMainEnum type, SingTypeEnum signType, MainSchoolContentModel mainSchoolContentModel) {
        /*?????????????????????*/
        switch (type){
            case CLASS:
            case BUSES:
                StudentSignBody studentSignBody = new StudentSignBody();
                StudentsBean student = mainSchoolContentModel.getStudentsBean();
                /*??????????????????*/
                studentSignBody.setStudentid(student.getCenterStudentId());
                studentSignBody.setType(signType.getType());
                studentSignBody.setBusId(student.getBusId());
                studentSignBody.setDate(DateUtil.getStringDate());
                studentSignBody.setRemarks("");
                studentSignBody.setCommon("");
                /*???????????????????????????*/
                if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
                    studentSignBody.setTemperature(36.5f);
                }
                studentSignRequest(studentSignBody,mainSchoolContentModel,signType);
                break;
            case STAFF:
                StaffSignBody staffSignBody = new StaffSignBody();
                /*??????????????????*/
                StaffsBean staffsBean = mainSchoolContentModel.getStaffsBean();
                staffSignBody.setStaffid(staffsBean.getStaffId());
                staffSignBody.setType(signType.getType());
                staffSignBody.setDate(DateUtil.getStringDate());
                staffSignBody.setRemarks("");
                staffSignBody.setCommon("");
                /*???????????????????????????*/
                if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
                    staffSignBody.setTemperature(36.5f);
                }
                staffSignRequest(staffSignBody,mainSchoolContentModel,signType);
                break;
        }
    }

    /*??????????????????*/
    private void studentSignRequest(StudentSignBody studentSignBody,MainSchoolContentModel signContentModel,SingTypeEnum signType) {
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
                        /*?????????*/
                        StudentsBean studentSign = signContentModel.getStudentsBean();
                        studentSign.setSign(true);
                        studentSign.setSingType(signType);
                        signContentModel.setStudentsBean(studentSign);
                        getView().quickSignResult(signContentModel);
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*??????????????????*/
    private void staffSignRequest(StaffSignBody staffSignBody,MainSchoolContentModel signContentModel,SingTypeEnum signType) {
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
                        StaffsBean staffSign = signContentModel.getStaffsBean();
                        staffSign.setSign(true);
                        staffSign.setSignType(signType);
                        signContentModel.setStaffsBean(staffSign);
                        getView().quickSignResult(signContentModel);
                        break;
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*????????????????????????*/
    @Override
    public void localOffLineInput() {
        /*???????????????????????????1.?????????????????????2.?????????????????????3.?????????????????????4?????????????????????5????????????
        * ??????????????????????????????????????????????????????????????????????????????????????????????????????*/
        List<StudentSignCacheData> studentSignCacheList = LocalOffLineDataUtil.getStudentSignCacheList();//????????????
        List<StaffSignCacheData> staffSignCacheList = LocalOffLineDataUtil.getStaffSignCacheList();//????????????
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//????????????
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//????????????
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//????????????
        /*???????????????????????????*/
        if (LocalOffLineDataUtil.isExistOffLineData()) {
            /*??????????????????*/
            getView().showLoading();
            /*????????????????????????*/
            if (studentSignCacheList.size()>0){
                for (StudentSignCacheData signCacheData : studentSignCacheList){
                    offLineStudentSign(studentSignCacheList,signCacheData);
                }
            }else {
                /*????????????????????????*/
                if (staffSignCacheList.size()>0){
                    for (StaffSignCacheData signCacheData : staffSignCacheList){
                        offLineStaffSign(staffSignCacheList,signCacheData);
                    }
                }else {
                    /*????????????????????????*/
                    if (studentPictureCacheList.size()>0){
                        for (StudentPictureCacheData pictureCacheData : studentPictureCacheList){
                            offLineStudentPicture(studentPictureCacheList,pictureCacheData);
                        }
                    }else {
                        /*??????????????????????????????*/
                        if (staffPictureCacheList.size()>0){
                            for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                                offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                            }
                        }else {
                            /*????????????????????????*/
                            if (healthCheckCacheList.size()>0){
                                for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                                    offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*??????????????????????????????*/
    private void offLineStudentSign(List<StudentSignCacheData> studentSignCacheList,StudentSignCacheData signCacheData){
        List<StaffSignCacheData> staffSignCacheList = LocalOffLineDataUtil.getStaffSignCacheList();//????????????
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//????????????
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//????????????
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//????????????
        getModel().offLineStudentSign(studentSignCacheList, signCacheData, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                /*??????????????????????????????????????????*/
                if (staffSignCacheList.size()>0||studentPictureCacheList.size()>0||staffPictureCacheList.size()>0){
                    /*????????????????????????*/
                    if (staffSignCacheList.size()>0){
                        for (StaffSignCacheData signCacheData : staffSignCacheList){
                            offLineStaffSign(staffSignCacheList,signCacheData);
                        }
                    }else {
                        /*????????????????????????*/
                        if (studentPictureCacheList.size()>0){
                            for (StudentPictureCacheData pictureCacheData : studentPictureCacheList){
                                offLineStudentPicture(studentPictureCacheList,pictureCacheData);
                            }
                        }else {
                            if (staffPictureCacheList.size()>0){
                                for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                                    offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                                }
                            }else {
                                /*????????????????????????*/
                                if (healthCheckCacheList.size()>0){
                                    for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                                        offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                                    }
                                }else {
                                    /*????????????????????????*/
                                    offLineRefresh();
                                }
                            }
                        }
                    }
                }else {
                    /*????????????????????????*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*??????????????????????????????*/
    private void offLineStaffSign(List<StaffSignCacheData> staffSignCacheList, StaffSignCacheData signCacheData){
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//????????????
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//????????????
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//????????????
        getModel().offLineStaffSign(staffSignCacheList, signCacheData, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                /*??????????????????????????????????????????*/
                if (studentPictureCacheList.size()>0||staffPictureCacheList.size()>0){
                    /*??????????????????????????????*/
                    if (studentPictureCacheList.size()>0){
                        for (StudentPictureCacheData pictureCacheData : studentPictureCacheList){
                            offLineStudentPicture(studentPictureCacheList,pictureCacheData);
                        }
                    }else {
                        /*??????????????????????????????*/
                        if (staffPictureCacheList.size()>0){
                            for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                                offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                            }
                        }else {
                            /*????????????????????????*/
                            if (healthCheckCacheList.size()>0){
                                for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                                    offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                                }
                            }else {
                                /*????????????????????????*/
                                offLineRefresh();
                            }
                        }
                    }
                }else {
                    /*????????????????????????*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*??????????????????????????????*/
    private void offLineStudentPicture(List<StudentPictureCacheData> studentPictureCacheList,StudentPictureCacheData pictureCacheData){
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//????????????
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//????????????
        getModel().offLineStudentPicture(studentPictureCacheList, pictureCacheData, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {
                /*??????????????????????????????????????????*/
                if (staffPictureCacheList.size()>0){
                    for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                        offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                    }
                }else {
                    /*????????????????????????*/
                    if (healthCheckCacheList.size()>0){
                        for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                            offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                        }
                    }else {
                        /*????????????????????????*/
                        offLineRefresh();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*??????????????????????????????*/
    private void offLineStaffPicture(List<StaffPictureCacheData> staffPictureCacheList, StaffPictureCacheData pictureCacheData){
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//????????????
        getModel().offLineStaffPicture(staffPictureCacheList, pictureCacheData, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {
                /*????????????????????????*/
                if (healthCheckCacheList.size()>0){
                    for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                        offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                    }
                }else {
                    /*????????????????????????*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*????????????????????????*/
    private void offLineHealthCheck(List<HealthCheckCacheDate> healthCheckCacheList,HealthCheckCacheDate healthCheckCacheDate){
        getModel().offLineHealthCheck(healthCheckCacheList, healthCheckCacheDate, new ResultCallback<HealthCheckResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(HealthCheckResponse healthCheckResponse) {
                /*????????????????????????*/
                offLineRefresh();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*??????????????????????????????????????????*/
    private void offLineRefresh(){
        getModel().getLocalSchoolData(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD), new ResultCallback<LocalSchoolData>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(LocalSchoolData localSchoolData) {
                /*topBar????????????*/
                getView().topBarInfo(localSchoolData.getCenter_name(),localSchoolData.getCenter_logo());
                /*????????????????????????*/
                getView().guardiansList(localSchoolData.getGuardians());
                /*??????????????????*/
                mateClassModel(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*???????????????????????? -??? ???????????????????????????????????????????????????????????????*/
    @Override
    public void checkConnectBluetooth(List<FastBluetoothModel> resultModelList) {
        //?????????????????????????????????????????????
        if (TextUtils.isEmpty(SharePrefUtils.getString(Constant.CONNECT_BLUETOOTH_NAME))){
            getView().showBluetoothListPopup();
        }else {
            /*???????????????????????????*/
            if (resultModelList.size()>0){
                String localBluetoothName = SharePrefUtils.getString(Constant.CONNECT_BLUETOOTH_NAME);
                FastBluetoothModel bluetoothModel = null;
                for (FastBluetoothModel model : resultModelList){
                    if (model.getBleDevice().getName().equals(localBluetoothName)){
                        bluetoothModel = model;
                        break;
                    }
                }
                if (bluetoothModel!=null){
                    getView().autoConnectionBluetooth(bluetoothModel);
                }else {
                    getView().showBluetoothListPopup();
                }
            }else {
                getView().showBluetoothListPopup();
            }
        }
    }

}
