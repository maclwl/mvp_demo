package com.taidii.diibot.base.mvp;

public abstract class BasePresenter<M extends IModel,V extends IView> implements IPresenter<V>{

    private M mModel;
    private V mView;

    public BasePresenter() {
        this.mModel =createModel();
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }

    @Override
    public void attachView(V mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    /* 获取 Model*/
    protected abstract M createModel();

}
