package com.taidii.diibot.base.mvp;

public interface IPresenter <V extends IView> {

    /* 绑定 View*/
    void attachView(V mView);

    /* 解除 View*/
    void detachView();
}

