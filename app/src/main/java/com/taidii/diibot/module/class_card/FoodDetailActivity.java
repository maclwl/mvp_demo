package com.taidii.diibot.module.class_card;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.module.class_card.adapter.ClassCardTeacherDetailAdapter;
import com.taidii.diibot.module.class_card.viewJzd.SwitchClassCardPopup;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.ImageViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;
import xiao.free.horizontalrefreshlayout.refreshhead.NiceRefreshHeader;

public class FoodDetailActivity extends BaseActivity implements SwitchClassCardPopup.SettingPopupListener {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.web_view)
    WebView webView;
    private String url="https://www.baidu.com";
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.top_name)
    ImageView topName;
    @BindView(R.id.tv_name)
    TextView tvName;
    private int[] weeks = new int[]{R.string.txt_course_sunday
            , R.string.txt_course_monday
            , R.string.txt_course_tuesday
            , R.string.txt_course_wednesday
            , R.string.txt_course_thursday
            , R.string.txt_course_friday
            , R.string.txt_course_saturday};
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private SwitchClassCardPopup switchClassCardPopup;//设置弹窗
    @Override
    protected int getLayoutId() {
        return R.layout.activity_food_detail;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        ImageViewUtils.loadImage(act, GlobalParams.center_logo, topName, R.drawable.avatar_loading2);
        tvName.setText(GlobalParams.center_name);
    }


    @Override
    protected void init() {
        Calendar calendar = Calendar.getInstance();
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String week = getResources().getString(weeks[weekIndex - 1]);
        String dateTime = sdf.format(new Date());
        tvTime.setText(dateTime.substring(0, 10) + "   " + week + "   " + dateTime.substring(10, 16));
        initWebView();
    }


    private void initWebView(){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setTextZoom(100);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if (webView.canGoBack())
            {
                webView.goBack();
                return true;
            }else
            {
                finish();
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.iv_setting,R.id.tv_class_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                if(switchClassCardPopup ==null){
                    switchClassCardPopup = new SwitchClassCardPopup(this, this);
                    switchClassCardPopup.showPopupWindow();
                }else{
                    switchClassCardPopup.showPopupWindow();
                }
                break;
            case R.id.tv_class_name:
                if (webView.canGoBack()) {
                    webView.goBack();
                }else {
                    finish();
                }
                break;
        }
    }


    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }




    private View getEmptyView() {
        View view = LayoutInflater.from(act).inflate(R.layout.class_card_empty_resource, null);
        TextView name = view.findViewById(R.id.name);
        name.setVisibility(View.VISIBLE);
        return view;
    }


    private int classId =-1;
    @Override
    public void choiceClass(ClassCardClassListBean.DataBean ben) {
        tvClassName.setText(ben.getName());
        classId = ben.getId();
    }

    @Override
    public void switchAttendance() {

    }
}
