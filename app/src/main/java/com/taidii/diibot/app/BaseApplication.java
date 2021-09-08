package com.taidii.diibot.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.clj.fastble.BleManager;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.taidii.diibot.R;
import com.taidii.diibot.view.picture.PictureSelectorEngineImp;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class BaseApplication extends MultiDexApplication implements IApp {

    public static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        /*初始化蓝牙*/
        BleManager.getInstance().init(this);
        /*全局字体*/
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/DFPHannotateW5-GB.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        /*Picture全局绑定*/
        PictureAppMaster.getInstance().setApp(this);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

}
