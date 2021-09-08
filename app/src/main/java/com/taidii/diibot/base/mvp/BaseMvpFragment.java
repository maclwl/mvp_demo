package com.taidii.diibot.base.mvp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taidii.diibot.base.BaseFragment;

public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements IView {

    protected T mPresenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter =createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initData();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init() {

    }

    /*初始获取网络数据*/
    protected void initData(){

    }

    @Override
    public void showLoading() {
        showLoadProgressBar();
    }

    @Override
    public void hideLoading() {
        hideLoadProgressBar();
    }

    @Override
    public void showMessage(@NonNull String message) {
        showShortToast(message);
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
