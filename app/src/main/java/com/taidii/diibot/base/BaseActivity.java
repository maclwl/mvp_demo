package com.taidii.diibot.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.enums.NetworkType;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.network.NetStateChangeObserver;
import com.taidii.diibot.utils.network.NetStateChangeReceiver;
import com.taidii.diibot.view.progress_load.MyProgressDialog;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity implements NetStateChangeObserver {

    protected Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        NetStateChangeReceiver.registerReceiver(this);
        setStatusBar();
        initView();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause( );
        NetStateChangeReceiver.unRegisterObserver(this);
    }

    /*子类布局文件*/
    protected abstract @LayoutRes
    int getLayoutId();

    /*是否注册event_bus*/
    protected boolean isRegisterEventBus() {
        return false;
    }

    /*初始化view*/
    protected abstract void initView();

    /*初始化相关数据*/
    protected abstract void init();

    /*设置共同沉浸式样式*/
    protected void setStatusBar(){
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.themeWhite)
                .navigationBarColor(R.color.themeWhite)
                .init();
    }

    /*显示加载菊花*/
    protected void showLoadProgressBar(){
        MyProgressDialog.showProgress(this);
    }

    /*隐藏加载菊花*/
    protected void hideLoadProgressBar(){
        MyProgressDialog.dismissProgress();
    }

    @Override
    public void onNetDisconnected() {
        netConnectError();
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        netConnect();
    }

    /*吐司 ---- start*/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    /*吐司 ---- end*/

    /*界面跳转 --- start*/
    protected void openActivity(Class<?> activityClass, Bundle bundle, boolean needReturnResult, int
            requestCode, boolean finish) {
        if (activityClass == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (needReturnResult) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            finish();
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

    /*event_bus*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /*网络连接错误*/
    protected void netConnectError(){}

    /*网络恢复成功*/
    protected void netConnect(){}

    /*普通分发事件*/
    protected void receiveEvent(Event event) { }

    /*粘性分发事件*/
    protected void receiveStickyEvent(Event event) { }

    @Override
    protected void onDestroy() {
        NetStateChangeReceiver.unRegisterReceiver(this);
        super.onDestroy();
        OKHttpUtils.cancel(this);
        MyProgressDialog.dismissProgress();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
