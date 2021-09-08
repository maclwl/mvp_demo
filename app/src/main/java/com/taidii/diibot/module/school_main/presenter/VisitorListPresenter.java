package com.taidii.diibot.module.school_main.presenter;

import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.school.VisitorListBean;
import com.taidii.diibot.module.school_main.contract.VisitorListContract;
import com.taidii.diibot.module.school_main.model.VisitorListModel;
import com.taidii.diibot.net.ResultCallback;

public class VisitorListPresenter extends BasePresenter<VisitorListContract.Model,VisitorListContract.View> implements VisitorListContract.Presenter{
    @Override
    protected VisitorListContract.Model createModel() {
        return new VisitorListModel();
    }

    @Override
    public void getVisitorList() {
        getModel().getVisitorList(new ResultCallback<VisitorListBean>() {
            @Override
            public void onStart() {
                getView().showLoading();
            }

            @Override
            public void onSuccess(VisitorListBean visitorListBean) {
                if (visitorListBean!=null){
                    getView().visitorList(visitorListBean.getVisitors());
                }
            }

            @Override
            public void onFinish() {
                getView().hideLoading();
            }
        });
    }
}
