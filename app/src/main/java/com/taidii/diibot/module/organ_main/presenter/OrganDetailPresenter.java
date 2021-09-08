package com.taidii.diibot.module.organ_main.presenter;

import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.organ.OrganDetailBean;
import com.taidii.diibot.entity.organ.OrganSignResponse;
import com.taidii.diibot.entity.organ.OrganStudentSignBody;
import com.taidii.diibot.entity.school.PictureInputResponse;
import com.taidii.diibot.module.organ_main.contract.OrganDetailContract;
import com.taidii.diibot.module.organ_main.model.OrganDetailModel;
import com.taidii.diibot.net.ResultCallback;

public class OrganDetailPresenter extends BasePresenter<OrganDetailContract.Model,OrganDetailContract.View> implements OrganDetailContract.Presenter{
    @Override
    protected OrganDetailContract.Model createModel() {
        return new OrganDetailModel();
    }

    @Override
    public void getOrganDetail(int student_id, int is_test_child) {
        boolean is_child = false;
        if (is_test_child == 0){
            is_child = false;
        }else {
            is_child = true;
        }
        getModel().getOrganDetail(student_id, is_child, new ResultCallback<OrganDetailBean>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(OrganDetailBean organDetailBean) {
                getView().OrganDetail(organDetailBean);
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }

    @Override
    public void studentSignRequest(OrganStudentSignBody organStudentSignBody) {
        getModel().studentSignRequest(organStudentSignBody, new ResultCallback<OrganSignResponse>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(OrganSignResponse organSignResponse) {
                switch (organSignResponse.getStatusCode()){
                    case 1:
                        getView().SignBack(organSignResponse);
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
    public void organPictureInput(int studentId, int id, String date, String fileName) {
        getModel().organPictureInput(studentId, id, date, fileName, new ResultCallback<PictureInputResponse>() {
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
