package com.taidii.diibot.module.organ_main.presenter;
import android.text.TextUtils;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.organ.ClassOrderBean;
import com.taidii.diibot.entity.organ.KlassesBean;
import com.taidii.diibot.entity.organ.LocalOrganData;
import com.taidii.diibot.entity.organ.MainOrganCollectionModel;
import com.taidii.diibot.entity.organ.StudentattendanceBean;
import com.taidii.diibot.entity.organ.StudentsBean;
import com.taidii.diibot.entity.school.DesInfoBean;
import com.taidii.diibot.entity.school.QrCodeDecryptBean;
import com.taidii.diibot.module.organ_main.contract.OrganMainContract;
import com.taidii.diibot.module.organ_main.model.OrganMainModel;
import com.taidii.diibot.net.ResultCallback;
import com.taidii.diibot.utils.DESUtil;
import com.taidii.diibot.utils.GsonUtil;
import com.taidii.diibot.utils.SharePrefUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrganMainPresenter extends BasePresenter<OrganMainContract.Model,OrganMainContract.View> implements OrganMainContract.Presenter{
    @Override
    protected OrganMainContract.Model createModel() {
        return new OrganMainModel();
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

    @Override
    public void getLocalOrganData() {
        getModel().getLocalOrganData(new ResultCallback<LocalOrganData>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(LocalOrganData localOrganData) {
                /*topBar展示数据*/
                getView().topBarInfo(localOrganData.getCenter_name(),localOrganData.getCenter_logo());
                /*重组机构班级数组*/
                mateOrganClassModel(localOrganData);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*重组数组*/
    private void mateOrganClassModel(LocalOrganData localOrganData){
        List<MainOrganCollectionModel> organClassList = new ArrayList<>();//重组首页列表
        List<StudentsBean> studentList = localOrganData.getStudents();//学生列表
        List<ClassOrderBean> classOrderList = localOrganData.getClassOrder();//班级列表

        /*首先通过学生列表及培训班列表筛选出班级id列表*/
        Set<Integer> setId = new HashSet<>();
        for (StudentsBean studentsBean : studentList){
            //学生报班列表
            List<KlassesBean> classList = studentsBean.getKlasses();
            for (KlassesBean klassesBean : classList){
                for (ClassOrderBean classOrderBean : classOrderList){
                    if (klassesBean.getKlassschedule_id()==classOrderBean.getId()){
                        setId.add(classOrderBean.getId());
                    }
                }
            }
        }
        if (setId.size()>0){
            /*通过班级id列表匹配对应的班级组成班级列表*/
            List<ClassOrderBean> mateClassOrderList = new ArrayList<>();
            for (Integer i : setId){
                for (ClassOrderBean classOrderBean : classOrderList){
                    if (classOrderBean.getId()==i){
                        mateClassOrderList.add(classOrderBean);
                    }
                }
            }
            /*通过筛选的班级列表与学生列表匹配对应班级的学生，重组首页列表*/
            for (ClassOrderBean classOrderBean : mateClassOrderList){
                MainOrganCollectionModel mainOrganCollectionModel = new MainOrganCollectionModel();
                mainOrganCollectionModel.setClassOrder(classOrderBean);
                List<StudentsBean> mateStudentsList = new ArrayList<>();
                for (StudentsBean studentsBean : studentList){
                    List<KlassesBean> classList = studentsBean.getKlasses();
                    for (KlassesBean klassesBean : classList){
                        if (klassesBean.getKlassschedule_id() == classOrderBean.getId()){
                            StudentsBean newStudent = new StudentsBean();
                            newStudent.setKlassschedule_id(studentsBean.getKlassschedule_id());
                            newStudent.setSign(studentsBean.isSign());
                            newStudent.setTouch_password(studentsBean.getTouch_password());
                            newStudent.setTouch_able(studentsBean.getTouch_able());
                            newStudent.setRfid(studentsBean.getRfid());
                            newStudent.setGroupname(studentsBean.getGroupname());
                            newStudent.setEmergencyContactNo(studentsBean.getEmergencyContactNo());
                            newStudent.setIc(studentsBean.getIc());
                            newStudent.setName(studentsBean.getName());
                            newStudent.setGender(studentsBean.getGender());
                            newStudent.setCenterStudentId(studentsBean.getCenterStudentId());
                            newStudent.setAvatar(studentsBean.getAvatar());
                            newStudent.setIs_test_child(studentsBean.getIs_test_child());
                            newStudent.setKlasses(studentsBean.getKlasses());
                            mateStudentsList.add(newStudent);
                        }
                    }
                }
                mainOrganCollectionModel.setStudentsList(mateStudentsList);
                organClassList.add(mainOrganCollectionModel);
            }

            /*赋值学生班级id*/
            for (MainOrganCollectionModel model : organClassList){
                List<StudentsBean> studentsBeanList = model.getStudentsList();
                for (StudentsBean student : studentsBeanList){
                    student.setKlassschedule_id(model.getClassOrder().getId());
                }
            }

            //签到学生(匹配签到学生)
            if (localOrganData.getStudentattendance().size()>0){
                for (StudentattendanceBean studentattendanceBean : localOrganData.getStudentattendance()){
                    for (MainOrganCollectionModel model :organClassList){
                        List<StudentsBean> mateStudentList = model.getStudentsList();
                        for (StudentsBean student : mateStudentList){
                            if (studentattendanceBean.getStudentId() == student.getCenterStudentId()
                                &&studentattendanceBean.getKlassschedule_id()==student.getKlassschedule_id()){
                                /*设置签到与签到类型*/
                                student.setSign(true);
                            }
                        }
                    }
                }
            }
        }
        getView().organCollection(organClassList);
    }

    /*扫码解密*/
    @Override
    public void decrypt(String result, List<MainOrganCollectionModel> organClassList) {
        /*首先判断是否有解密的key没有则不可后续操作*/
        String iv = SharePrefUtils.getString(Constant.DES_IV);
        String des_key = SharePrefUtils.getString(Constant.DES_KEY);
        if (!TextUtils.isEmpty(des_key)){
            /*解密*/
            try {
                String desResult = DESUtil.decryptDES(result,iv,des_key);
                /*gson解析*/
                QrCodeDecryptBean decryptBean = GsonUtil.GsonToBean(desResult,QrCodeDecryptBean.class);
                /*通过id匹配出对应的学生*/
                StudentsBean qrStudent = null;
                ClassOrderBean classOrder = null;
                for (MainOrganCollectionModel model : organClassList){
                    List<StudentsBean> studentsList = model.getStudentsList();
                    for (StudentsBean student : studentsList){
                        if (decryptBean.getId_num()==student.getCenterStudentId()){
                            qrStudent = student;
                            classOrder = model.getClassOrder();
                        }
                    }
                }
                if (qrStudent!=null&&classOrder!=null){
                    getView().qrStudent(qrStudent,classOrder);
                }else {
                    getView().showMessage(R.string.qr_no_data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            getView().showMessage(R.string.qr_code_info_error);
        }
    }

    /*签到成功*/
    @Override
    public void signBackRefresh(List<MainOrganCollectionModel> organClassList, StudentsBean studentsBean) {
        for (MainOrganCollectionModel model : organClassList){
            List<StudentsBean> studentList = model.getStudentsList();
            for (StudentsBean student : studentList){
                if (student.getCenterStudentId() == studentsBean.getCenterStudentId() &&
                    student.getKlassschedule_id() == studentsBean.getKlassschedule_id()) {
                    student.setSign(true);
                }
            }
        }
        getView().signRefreshBack(organClassList);
    }

}
