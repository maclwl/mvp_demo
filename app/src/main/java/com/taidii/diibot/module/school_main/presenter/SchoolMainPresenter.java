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

    /*获取解密相关信息*/
    @Override
    public void getDesInfo() {
        getModel().getDesInfo(new ResultCallback<DesInfoBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DesInfoBean desInfoBean) {
                /*保存解密相关信息*/
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

    /*扫码解密处理*/
    @Override
    public void decrypt(String result, List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardiansList) {
        /*首先判断是否有解密的key没有则不可后续操作*/
        String iv = SharePrefUtils.getString(Constant.DES_IV);
        String des_key = SharePrefUtils.getString(Constant.DES_KEY);
        if (!TextUtils.isEmpty(des_key)){
            /*解密*/
            try {
                String desResult = DESUtil.decryptDES(result,iv,des_key);
                /*gson解析*/
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

    /*搜索学生匹配*/
    @Override
    public void mateSearch(String keyword, List<MainSchoolCollectionModel> currentModelList) {
        List<MainSchoolCollectionModel> newModelList = new ArrayList<>();//重组首页列表
        /*通过关键词与学生姓名进行匹配*/
        for (MainSchoolCollectionModel model : currentModelList){
            MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
            List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
            /*判断类别*/
            switch (model.getType()){
                case CLASS:
                case BUSES:
                    mainSchoolCollectionModel.setModelName(model.getModelName());
                    mainSchoolCollectionModel.setType(model.getType());
                    for (MainSchoolContentModel contentModel : model.getMainSchoolContentModelList()){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*班级名与学生列表中的班级名为匹配条件（备注：把字符中大写转小写，统一为模糊搜索）*/
                        if (Utils.exChangeLower(contentModel.getStudentsBean().getName()).contains(Utils.exChangeLower(keyword))){
                            mainSchoolContentModel.setStudentsBean(contentModel.getStudentsBean());
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*如果存在子列表则加入，反之*/
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
                        /*班级名与学生列表中的班级名为匹配条件*/
                        if (Utils.exChangeLower(contentModel.getStaffsBean().getName()).contains(Utils.exChangeLower(keyword))){
                            mainSchoolContentModel.setStaffsBean(contentModel.getStaffsBean());
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*如果存在子列表则加入，反之*/
                    if (mainSchoolContentModelList.size()>0){
                        mainSchoolCollectionModel.setSpread(true);
                        newModelList.add(mainSchoolCollectionModel);
                    }
                    break;
            }
        }
        getView().mateSearchResult(newModelList,keyword);
    }

    /*扫码，家长类型，匹配出对应的学生或者学生列表*/
    private void mateParent(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList,List<GuardiansBean> guardiansList){
        /*首先通过扫码id与家长匹配，匹配出对应的家长，再通过家长匹配出对应的学生*/
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
                getView().qrResultSingStudent(qrCodeStudentList);
            }else {
                getView().showMessage(R.string.qr_code_info_error);
            }
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*扫码，学生类型，匹配出对应的学生或者学生列表*/
    private void mateStudent(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList){
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
        /*判断是否匹配出学生*/
        if (qrCodeStudentList.size()>0){
            getView().qrResultSingStudent(qrCodeStudentList);
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*扫码，老师类型，匹配出对应的学生或者学生列表*/
    private void mateTeacher(QrCodeDecryptBean decryptBean,List<MainSchoolCollectionModel> allCollectionList){
        SchoolMainEnum type = null;
        MainSchoolContentModel schoolContentModel = null;
        for (MainSchoolCollectionModel collectionModel : allCollectionList) {
            /*仅匹配学生*/
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
                /*保存用户是否强制健康体测*/
                SharePrefUtils.saveBoolean(Constant.HEALTH_CHECK, localSchoolData.isMandatory_health_check());
                /*保存用户是否可提交问卷调查*/
                SharePrefUtils.saveBoolean(Constant.SAFE_ACCESS, localSchoolData.isCovid_safe_access());
                /*topBar展示数据*/
                getView().topBarInfo(localSchoolData.getCenter_name(),localSchoolData.getCenter_logo());
                GlobalParams.center_logo = localSchoolData.getCenter_logo();
                GlobalParams.center_name = localSchoolData.getCenter_name();
                /*返回接送家长数组---增加监护人*/
                anllGuardiansList.addAll(localSchoolData.getGuardians());
                if (localSchoolData.getAuthorizedPickups()!=null&&localSchoolData.getAuthorizedPickups().size()>0){
                    anllGuardiansList.addAll(localSchoolData.getAuthorizedPickups());
                }
                getView().guardiansList(anllGuardiansList);
                /*重组班级数组*/
                mateClassModel(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*设置数量*/
    @Override
    public void getLocalSchoolDataCount(String username, String password) {
        getModel().getLocalSchoolData(username, password, new ResultCallback<LocalSchoolData>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(LocalSchoolData localSchoolData) {
                /*重组班级数组获取数量*/
                mateClassModelCount(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*获取列表，初始设置值（判断是否展开，是否老师考勤）*/
    @Override
    public void initSettingList(List<MainSchoolCollectionModel> allCollectionList) {
        List<MainSchoolCollectionModel> initAllList = new ArrayList<>();
        if (allCollectionList.size()>0){
            /*是否展开*/
            for (MainSchoolCollectionModel collectionModel : allCollectionList){
                collectionModel.setSpread(SharePrefUtils.getBoolean(Constant.IS_SPREAD_ALL));
                initAllList.add(collectionModel);
            }
            /*是否老师考勤*/
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

    /*剔除老师考勤*/
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

    /*是否展开设置*/
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

    /*重组，匹配出学生*/
    private void mateClassModel(LocalSchoolData localSchoolData){
        List<MainSchoolCollectionModel> classModelList = new ArrayList<>();//重组首页列表
        List<StudentsBean> studentList = localSchoolData.getStudents();//学生列表
        List<StaffsBean> staffList = localSchoolData.getStaffs();//老师列表
        List<StudentsBean> signStudentList = new ArrayList<>();//签到学生列表
        List<StudentsBean> signedStudentList = new ArrayList<>();//已签到学生列表
        List<StudentsBean> signedOutStudentList = new ArrayList<>();//已签到学生列表
        List<StaffsBean> signStaffList = new ArrayList<>();//签到老师列表
        int totalCount = studentList.size();
        int signInCount = 0;
        int signoutCount = 0;
        /*重置学生列表和老师列表（是否已签到）*/
        //签到学生,已出勤人数
        if (localSchoolData.getStudentattendance().size()>0){
            for (StudentsBean studentsBean : studentList){
                for (StudentattendanceBean studentattendanceBean : localSchoolData.getStudentattendance()){
                    if (studentattendanceBean.getStudentId() == studentsBean.getCenterStudentId()){
                        /*设置签到与签到类型*/
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
        //签到老师
        if (localSchoolData.getStaffattendance().size()>0){
            for (StaffsBean staffsBean : staffList){
                for (StaffattendanceBean staffattendanceBean : localSchoolData.getStaffattendance()){
                    if (staffsBean.getStaffId() == staffattendanceBean.getStaffId()){
                        /*设置签到和签到类型*/
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

        /*三种类型学生组，1.班级（classOrder） 2.校车（buses） 3.老师（staffs）*/
        if (localSchoolData!=null){
            //1.班级组：班级组中匹配出对应班级的学生
            if (localSchoolData.getClassOrder().size()>0){
                for (String className : localSchoolData.getClassOrder()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(className);
                    mainSchoolCollectionModel.setType(SchoolMainEnum.CLASS);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*班级名与学生列表中的班级名为匹配条件*/
                        if (className.equals(studentsBean.getClassname())){
                            mainSchoolContentModel.setStudentsBean(studentsBean);
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    /*如果存在子列表则加入，反之*/
                    if (mainSchoolContentModelList.size()>0){
                        classModelList.add(mainSchoolCollectionModel);
                    }
                }
            }
            //2.校车组：通过其中学生id组与学生列表匹配
            if (localSchoolData.getBuses().size()>0){
                for (BusesBean busesBean : localSchoolData.getBuses()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(busesBean.getDescription());
                    mainSchoolCollectionModel.setType(SchoolMainEnum.BUSES);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*通过bus中的学生id组匹配*/
                        for (Integer id : busesBean.getStudentIds()){
                            if (id == studentsBean.getCenterStudentId()){
                                /*设置校车ID*/
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
            //3.老师组
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

    /*重组，匹配出学生*/
    private void mateClassModelCount(LocalSchoolData localSchoolData){
        List<MainSchoolCollectionModel> classModelList = new ArrayList<>();//重组首页列表
        List<StudentsBean> studentList = localSchoolData.getStudents();//学生列表
        List<StaffsBean> staffList = localSchoolData.getStaffs();//老师列表
        List<StudentsBean> signStudentList = new ArrayList<>();//签到学生列表
        List<StudentsBean> signedStudentList = new ArrayList<>();//已签到学生列表
        List<StudentsBean> signedOutStudentList = new ArrayList<>();//已签到学生列表
        List<StaffsBean> signStaffList = new ArrayList<>();//签到老师列表
        int totalCount = studentList.size();
        int signInCount = 0;
        int signoutCount = 0;
        /*重置学生列表和老师列表（是否已签到）*/
        //签到学生,已出勤人数
        if (localSchoolData.getStudentattendance().size()>0){
            for (StudentsBean studentsBean : studentList){
                for (StudentattendanceBean studentattendanceBean : localSchoolData.getStudentattendance()){
                    if (studentattendanceBean.getStudentId() == studentsBean.getCenterStudentId()){
                        /*设置签到与签到类型*/
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
        //签到老师
        if (localSchoolData.getStaffattendance().size()>0){
            for (StaffsBean staffsBean : staffList){
                for (StaffattendanceBean staffattendanceBean : localSchoolData.getStaffattendance()){
                    if (staffsBean.getStaffId() == staffattendanceBean.getStaffId()){
                        /*设置签到和签到类型*/
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

        /*三种类型学生组，1.班级（classOrder） 2.校车（buses） 3.老师（staffs）*/
        if (localSchoolData!=null){
            //1.班级组：班级组中匹配出对应班级的学生
            if (localSchoolData.getClassOrder().size()>0){
                for (String className : localSchoolData.getClassOrder()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(className);
                    mainSchoolCollectionModel.setType(SchoolMainEnum.CLASS);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*班级名与学生列表中的班级名为匹配条件*/
                        if (className.equals(studentsBean.getClassname())){
                            mainSchoolContentModel.setStudentsBean(studentsBean);
                            mainSchoolContentModelList.add(mainSchoolContentModel);
                        }
                    }
                    mainSchoolCollectionModel.setMainSchoolContentModelList(mainSchoolContentModelList);
                    classModelList.add(mainSchoolCollectionModel);
                }
            }
            //2.校车组：通过其中学生id组与学生列表匹配
            if (localSchoolData.getBuses().size()>0){
                for (BusesBean busesBean : localSchoolData.getBuses()){
                    MainSchoolCollectionModel mainSchoolCollectionModel = new MainSchoolCollectionModel();
                    List<MainSchoolContentModel> mainSchoolContentModelList = new ArrayList<>();
                    mainSchoolCollectionModel.setModelName(busesBean.getDescription());
                    mainSchoolCollectionModel.setType(SchoolMainEnum.BUSES);
                    for (StudentsBean studentsBean : signStudentList){
                        MainSchoolContentModel mainSchoolContentModel = new MainSchoolContentModel();
                        /*通过bus中的学生id组匹配*/
                        for (Integer id : busesBean.getStudentIds()){
                            if (id == studentsBean.getCenterStudentId()){
                                /*设置校车ID*/
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
            //3.老师组
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

    /*签到成功返回刷新*/
    @Override
    public void signBackRefresh(List<MainSchoolCollectionModel> collectionModelList, int collectionPosition, int contentPosition, MainSchoolContentModel signContentModel,List<MainSchoolCollectionModel> allCollectionList) {
        /*通过学生id 老师id修改*/
        List<MainSchoolCollectionModel> newCollectionModelList = new ArrayList<>();
        if (signContentModel.getStudentsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : collectionModelList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStudentsBean()!=null){
                        StudentsBean studentsBean = contentModel.getStudentsBean();
                        /*判断学生id*/
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
        /*老师签到刷新*/
        if (signContentModel.getStaffsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : collectionModelList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStaffsBean()!=null){
                        StaffsBean staffsBean = contentModel.getStaffsBean();
                        /*判断老师id*/
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

        /*扫码老师签到，老师未展开情况。需刷新所有数据（并不只刷新列表展示数据）*/
        List<MainSchoolCollectionModel> newAllCollectionList = new ArrayList<>();
        if (!SharePrefUtils.getBoolean(Constant.IS_STAFF_CHECK) && signContentModel.getStaffsBean()!=null){
            for (MainSchoolCollectionModel collectionModel : allCollectionList) {
                List<MainSchoolContentModel> newContentModelList = new ArrayList<>();
                List<MainSchoolContentModel> contentModelList = collectionModel.getMainSchoolContentModelList();
                for (MainSchoolContentModel contentModel : contentModelList){
                    if (contentModel.getStaffsBean()!=null){
                        StaffsBean staffsBean = contentModel.getStaffsBean();
                        /*判断老师id*/
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

    /*快速考勤*/
    @Override
    public void quickSign(SchoolMainEnum type, SingTypeEnum signType, MainSchoolContentModel mainSchoolContentModel) {
        /*判断老师和学生*/
        switch (type){
            case CLASS:
            case BUSES:
                StudentSignBody studentSignBody = new StudentSignBody();
                StudentsBean student = mainSchoolContentModel.getStudentsBean();
                /*设置请求参数*/
                studentSignBody.setStudentid(student.getCenterStudentId());
                studentSignBody.setType(signType.getType());
                studentSignBody.setBusId(student.getBusId());
                studentSignBody.setDate(DateUtil.getStringDate());
                studentSignBody.setRemarks("");
                studentSignBody.setCommon("");
                /*是否设置温度默认值*/
                if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
                    studentSignBody.setTemperature(36.5f);
                }
                studentSignRequest(studentSignBody,mainSchoolContentModel,signType);
                break;
            case STAFF:
                StaffSignBody staffSignBody = new StaffSignBody();
                /*设置请求参数*/
                StaffsBean staffsBean = mainSchoolContentModel.getStaffsBean();
                staffSignBody.setStaffid(staffsBean.getStaffId());
                staffSignBody.setType(signType.getType());
                staffSignBody.setDate(DateUtil.getStringDate());
                staffSignBody.setRemarks("");
                staffSignBody.setCommon("");
                /*是否设置温度默认值*/
                if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)){
                    staffSignBody.setTemperature(36.5f);
                }
                staffSignRequest(staffSignBody,mainSchoolContentModel,signType);
                break;
        }
    }

    /*学生快速签到*/
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
                        /*设置值*/
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

    /*老师快速签到*/
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

    /*离线数据上传处理*/
    @Override
    public void localOffLineInput() {
        /*离线考勤数据包含：1.学生考勤数据；2.老师考勤数据；3.学生考勤图片；4老师考勤图片；5健康体测
        * 处理：从数据库中取出数据，遍历循环链式请求（成功删除数据库缓存数据）*/
        List<StudentSignCacheData> studentSignCacheList = LocalOffLineDataUtil.getStudentSignCacheList();//学生考勤
        List<StaffSignCacheData> staffSignCacheList = LocalOffLineDataUtil.getStaffSignCacheList();//老师考勤
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//学生图片
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//老师图片
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//健康体测
        /*存在离线数据则上传*/
        if (LocalOffLineDataUtil.isExistOffLineData()) {
            /*显示加载菊花*/
            getView().showLoading();
            /*学生考勤离线数据*/
            if (studentSignCacheList.size()>0){
                for (StudentSignCacheData signCacheData : studentSignCacheList){
                    offLineStudentSign(studentSignCacheList,signCacheData);
                }
            }else {
                /*老师考勤离线数据*/
                if (staffSignCacheList.size()>0){
                    for (StaffSignCacheData signCacheData : staffSignCacheList){
                        offLineStaffSign(staffSignCacheList,signCacheData);
                    }
                }else {
                    /*判断离线图片数据*/
                    if (studentPictureCacheList.size()>0){
                        for (StudentPictureCacheData pictureCacheData : studentPictureCacheList){
                            offLineStudentPicture(studentPictureCacheList,pictureCacheData);
                        }
                    }else {
                        /*老师考勤图片离线上传*/
                        if (staffPictureCacheList.size()>0){
                            for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                                offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                            }
                        }else {
                            /*健康体测离线上传*/
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

    /*学生离线考勤网络上传*/
    private void offLineStudentSign(List<StudentSignCacheData> studentSignCacheList,StudentSignCacheData signCacheData){
        List<StaffSignCacheData> staffSignCacheList = LocalOffLineDataUtil.getStaffSignCacheList();//老师考勤
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//学生图片
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//老师图片
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//健康体测
        getModel().offLineStudentSign(studentSignCacheList, signCacheData, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                /*判断是否还有其他离线考勤数据*/
                if (staffSignCacheList.size()>0||studentPictureCacheList.size()>0||staffPictureCacheList.size()>0){
                    /*老师考勤离线数据*/
                    if (staffSignCacheList.size()>0){
                        for (StaffSignCacheData signCacheData : staffSignCacheList){
                            offLineStaffSign(staffSignCacheList,signCacheData);
                        }
                    }else {
                        /*判断离线图片数据*/
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
                                /*健康体测离线上传*/
                                if (healthCheckCacheList.size()>0){
                                    for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                                        offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                                    }
                                }else {
                                    /*获取首页列表刷新*/
                                    offLineRefresh();
                                }
                            }
                        }
                    }
                }else {
                    /*获取首页列表刷新*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*老师离线考勤网络上传*/
    private void offLineStaffSign(List<StaffSignCacheData> staffSignCacheList, StaffSignCacheData signCacheData){
        List<StudentPictureCacheData> studentPictureCacheList = LocalOffLineDataUtil.getsStudentPictureCacheList();//学生图片
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//老师图片
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//健康体测
        getModel().offLineStaffSign(staffSignCacheList, signCacheData, new ResultCallback<SignResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(SignResponse signResponse) {
                /*判断是否还有其他离线考勤数据*/
                if (studentPictureCacheList.size()>0||staffPictureCacheList.size()>0){
                    /*学生图片考勤离线数据*/
                    if (studentPictureCacheList.size()>0){
                        for (StudentPictureCacheData pictureCacheData : studentPictureCacheList){
                            offLineStudentPicture(studentPictureCacheList,pictureCacheData);
                        }
                    }else {
                        /*判断离线老师图片数据*/
                        if (staffPictureCacheList.size()>0){
                            for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                                offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                            }
                        }else {
                            /*健康体测离线上传*/
                            if (healthCheckCacheList.size()>0){
                                for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                                    offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                                }
                            }else {
                                /*获取首页列表刷新*/
                                offLineRefresh();
                            }
                        }
                    }
                }else {
                    /*获取首页列表刷新*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*学生考勤图片网络上传*/
    private void offLineStudentPicture(List<StudentPictureCacheData> studentPictureCacheList,StudentPictureCacheData pictureCacheData){
        List<StaffPictureCacheData> staffPictureCacheList = LocalOffLineDataUtil.getStaffPictureCacheList();//老师图片
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//健康体测
        getModel().offLineStudentPicture(studentPictureCacheList, pictureCacheData, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {
                /*判断是否还有其他离线考勤数据*/
                if (staffPictureCacheList.size()>0){
                    for (StaffPictureCacheData pictureCacheData : staffPictureCacheList){
                        offLineStaffPicture(staffPictureCacheList,pictureCacheData);
                    }
                }else {
                    /*健康体测离线上传*/
                    if (healthCheckCacheList.size()>0){
                        for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                            offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                        }
                    }else {
                        /*获取首页列表刷新*/
                        offLineRefresh();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*老师考勤图片网络上传*/
    private void offLineStaffPicture(List<StaffPictureCacheData> staffPictureCacheList, StaffPictureCacheData pictureCacheData){
        List<HealthCheckCacheDate> healthCheckCacheList = LocalOffLineDataUtil.getHealthCheckCacheList();//健康体测
        getModel().offLineStaffPicture(staffPictureCacheList, pictureCacheData, new ResultCallback<PictureInputResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(PictureInputResponse pictureInputResponse) {
                /*健康体测离线上传*/
                if (healthCheckCacheList.size()>0){
                    for (HealthCheckCacheDate healthCheckCacheDate : healthCheckCacheList){
                        offLineHealthCheck(healthCheckCacheList,healthCheckCacheDate);
                    }
                }else {
                    /*获取首页学校信息*/
                    offLineRefresh();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*健康体测离线上传*/
    private void offLineHealthCheck(List<HealthCheckCacheDate> healthCheckCacheList,HealthCheckCacheDate healthCheckCacheDate){
        getModel().offLineHealthCheck(healthCheckCacheList, healthCheckCacheDate, new ResultCallback<HealthCheckResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(HealthCheckResponse healthCheckResponse) {
                /*获取首页学校信息*/
                offLineRefresh();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /*离线考勤数据上传成功刷新列表*/
    private void offLineRefresh(){
        getModel().getLocalSchoolData(SharePrefUtils.getString(Constant.USERNAME), SharePrefUtils.getString(Constant.PASSWORD), new ResultCallback<LocalSchoolData>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(LocalSchoolData localSchoolData) {
                /*topBar展示数据*/
                getView().topBarInfo(localSchoolData.getCenter_name(),localSchoolData.getCenter_logo());
                /*返回接送家长数组*/
                getView().guardiansList(localSchoolData.getGuardians());
                /*重组班级数组*/
                mateClassModel(localSchoolData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*蓝牙自动连接检测 -》 通过扫描的列表，与缓存中连接过的蓝牙名匹配*/
    @Override
    public void checkConnectBluetooth(List<FastBluetoothModel> resultModelList) {
        //判断缓存中是否存在连接过的蓝牙
        if (TextUtils.isEmpty(SharePrefUtils.getString(Constant.CONNECT_BLUETOOTH_NAME))){
            getView().showBluetoothListPopup();
        }else {
            /*通过蓝牙名进行匹配*/
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
