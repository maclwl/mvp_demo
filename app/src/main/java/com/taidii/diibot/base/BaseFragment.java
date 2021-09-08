package com.taidii.diibot.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import com.taidii.diibot.view.progress_load.MyProgressDialog;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected Activity act;
    protected View contentView;

    /* 懒加载过*/
    private boolean isLazyLoaded;

    /* View加载完毕*/
    private boolean isPrepared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, contentView);
        }
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        init();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    /*子类布局文件*/
    protected abstract @LayoutRes
    int getLayoutId();

    /*初始化view*/
    protected abstract void initView();

    /*初始化相关数据*/
    protected abstract void init();

    /*懒加载*/
    @UiThread
    protected abstract void onLazyLoad();

    /*显示加载菊花*/
    protected void showLoadProgressBar(){
        MyProgressDialog.showProgress(act);
    }

    /*隐藏加载菊花*/
    protected void hideLoadProgressBar(){
        MyProgressDialog.dismissProgress();
    }

    /*吐司 ---- start*/
    protected void showShortToast(int resId) {
        Toast.makeText(act, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(int resId) {
        Toast.makeText(act, getString(resId), Toast.LENGTH_LONG).show();
    }

    protected void showLongToast(String text) {
        Toast.makeText(act, text, Toast.LENGTH_LONG).show();
    }
    /*吐司 ---- end*/

    /*界面跳转 --- start*/
    protected void openActivity(Class<?> activityClass, Bundle bundle, boolean needReturnResult, int
            requestCode, boolean finish) {
        if (activityClass == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(act, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (needReturnResult) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            act.finish();
        }
    }

    protected void openActivity(Class<?> activityClass) {
        openActivity(activityClass, null, false, 0, false);
    }

    protected void openActivity(Class<?> activityClass, boolean isFinish) {
        openActivity(activityClass, null, false, 0, isFinish);
    }

    protected void openActivity(Class<?> activityClass, Bundle bundle) {
        openActivity(activityClass, bundle, false, 0, false);
    }

    protected void openActivity(Class<?> activityClass, Bundle bundle, boolean isFinish) {
        openActivity(activityClass, bundle, false, 0, isFinish);
    }
    /*界面跳转 --- end*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
