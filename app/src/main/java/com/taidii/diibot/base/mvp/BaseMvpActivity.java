package com.taidii.diibot.base.mvp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taidii.diibot.base.BaseActivity;

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements IView{

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
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

    @Override
    public void showMessage(int msgRes) {
        showShortToast(msgRes);
    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
