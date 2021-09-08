package com.taidii.diibot.module.school_main.presenter;

import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.AttendancesBean;
import com.taidii.diibot.entity.school.DeleteHistoryResponse;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.SignHistoryBean;
import com.taidii.diibot.entity.school.SignHistoryDataModel;
import com.taidii.diibot.module.school_main.contract.SignHistoryContract;
import com.taidii.diibot.module.school_main.model.SignHistoryModel;
import com.taidii.diibot.net.ResultCallback;

import java.util.ArrayList;
import java.util.List;

public class SignHistoryPresenter extends BasePresenter<SignHistoryContract.Model,SignHistoryContract.View> implements SignHistoryContract.Presenter{

    @Override
    protected SignHistoryContract.Model createModel() {
        return new SignHistoryModel();
    }

    @Override
    public void getStudentHistory(int studentId, String fromDate, String toDate) {
        getModel().getStudentHistory(studentId, fromDate, toDate, new ResultCallback<SignHistoryBean>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(SignHistoryBean signHistoryBean) {
                /*重组列表数据*/
                mateGuardians(signHistoryBean);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    /*匹配列表数据*/
    private void mateGuardians(SignHistoryBean signHistoryBean){
        List<SignHistoryDataModel> historyDataModelList = new ArrayList<>();
        List<AttendancesBean> attendancesList = signHistoryBean.getAttendances();
        List<GuardiansBean> guardiansList = signHistoryBean.getGuardians();
        if (attendancesList.size()>0){
            for (AttendancesBean attendancesBean : attendancesList){
                SignHistoryDataModel signHistoryDataModel = new SignHistoryDataModel();
                signHistoryDataModel.setAttendances(attendancesBean);
                /*判断是否有接送人 再去匹配接送人*/
                if (attendancesBean.getAuthorizedPickUpId()!=0){
                    if (guardiansList.size()>0){
                        for (GuardiansBean guardiansBean : guardiansList){
                            if (guardiansBean.getId()==attendancesBean.getAuthorizedPickUpId()){
                                signHistoryDataModel.setGuardians(guardiansBean);
                            }
                        }
                    }
                }
                historyDataModelList.add(signHistoryDataModel);
            }
        }
        getView().signHistoryList(historyDataModelList);
    }

    /*删除历史记录*/
    @Override
    public void deleteSignHistory(int id, int type) {
        getModel().deleteSignHistory(id, type, new ResultCallback<DeleteHistoryResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(DeleteHistoryResponse deleteHistoryResponse) {
                getView().deleteSuccess();
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void refreshHistory(int position, List<SignHistoryDataModel> signHistoryList) {
        for (int i=0;i<signHistoryList.size();i++){
            if (position == i){
                signHistoryList.remove(i);
            }
        }
        getView().refreshHistoryList(signHistoryList);
    }

}
