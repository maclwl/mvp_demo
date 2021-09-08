package com.taidii.diibot.module.school_main;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StudentSignListSelectActivity extends BaseActivity implements StudentSignSelectAdapter.ItemClickListener{

    @BindView(R.id.recycler_student)
    RecyclerView recyclerStudent;

    protected SingTypeEnum signType;//签到类型
    private StudentSignSelectAdapter studentSignSelectAdapter;
    protected List<GuardiansBean> guardianList = new ArrayList<>();//接送家长
    private List<QrCodeStudentCollection> modelResultList = new ArrayList<>();//学生列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_sign_list_select;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        studentSignSelectAdapter = new StudentSignSelectAdapter(this);
        recyclerStudent.setLayoutManager(new LinearLayoutManager(this));
        recyclerStudent.setAdapter(studentSignSelectAdapter);
    }

    @Override
    protected void init() {
        signType = (SingTypeEnum) getIntent().getSerializableExtra(Constant.SIGN_TYPE);
        guardianList = (List<GuardiansBean>) getIntent().getSerializableExtra(Constant.GUARDIANS_LIST);
        modelResultList = (List<QrCodeStudentCollection>) getIntent().getSerializableExtra(Constant.STUDENT_MODEL_LIST);
        studentSignSelectAdapter.setDataList(modelResultList);
    }

    @OnClick(R.id.im_back)
    public void onViewClicked() {
        finish();
    }

    /*跳转*/
    @Override
    public void clickItem(int position, QrCodeStudentCollection signHistoryDataModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TYPE, signHistoryDataModel.getType());
        bundle.putSerializable(Constant.SIGN_TYPE, signType);
        bundle.putSerializable(Constant.GUARDIANS_LIST, (Serializable) guardianList);
        bundle.putSerializable(Constant.SCHOOL_CONTENT_MODEL, signHistoryDataModel.getContentModel());
        openActivity(StudentSignActivity.class, bundle);
    }

    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
        MainSchoolContentModel signContentModel = (MainSchoolContentModel) event.getData();
        /*刷新列表*/
        List<QrCodeStudentCollection> ResultList = studentSignSelectAdapter.getDataList();
        List<QrCodeStudentCollection> newResultList = new ArrayList<>();
        if (ResultList.size()>0){
            for (QrCodeStudentCollection studentCollection : ResultList){
                MainSchoolContentModel contentModel = studentCollection.getContentModel();
                /*判断*/
                if (studentCollection.getContentModel().getStudentsBean().getCenterStudentId()==signContentModel.getStudentsBean().getCenterStudentId()){
                    StudentsBean studentsBean = studentCollection.getContentModel().getStudentsBean();
                    studentsBean.setSign(signContentModel.getStudentsBean().isSign());
                    studentsBean.setSingType(signContentModel.getStudentsBean().getSingType());
                    contentModel.setStudentsBean(studentsBean);
                    studentCollection.setContentModel(contentModel);
                }
                newResultList.add(studentCollection);
            }
            studentSignSelectAdapter.notifyDataChanged(newResultList);
        }
    }

}
