package com.taidii.diibot.module.privacy;

import android.os.Build;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomWebActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    WebView webView;

    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_web;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() { }

    @Override
    protected void init() {
        url = getIntent().getStringExtra(Constant.URL);
        String title = getIntent().getStringExtra(Constant.TYPE);
        tvTitle.setText(title);
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

    @OnClick(R.id.im_back)
    public void onViewClicked() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            finish();
        }
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

}
