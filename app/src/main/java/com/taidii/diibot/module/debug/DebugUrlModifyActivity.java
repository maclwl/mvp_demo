package com.taidii.diibot.module.debug;

import android.os.Build;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.taidii.diibot.BuildConfig;
import com.taidii.diibot.R;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.utils.LogUtils;
import com.taidii.diibot.utils.SharePrefUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

public class DebugUrlModifyActivity extends BaseActivity {

    public static final String DEBUG_URL_SAVE_KEY = "debugUrlKey";

    @BindView(R.id.edit_url)
    TextInputEditText editUrl;
    @BindView(R.id.til_layout)
    TextInputLayout tilLayout;
    @BindView(R.id.radio_item_http)
    RadioButton radioItemHttp;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindColor(R.color.themeWhite)
    int statusBarColor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debug_url;
    }

    @Override
    protected void initView() {
        if (!BuildConfig.DEBUG) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(statusBarColor);
        }
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        tilLayout.setHint("输入Debug目标Api地址,不需要添加http/https");
        String savedUrl = SharePrefUtils.getString(DEBUG_URL_SAVE_KEY, "");
        LogUtils.d("local saved url=" + savedUrl);
        if (!TextUtils.isEmpty(savedUrl)) {
            if (savedUrl.startsWith("http")) {
                savedUrl = savedUrl.substring(savedUrl.indexOf("/") + 2);
            }
            editUrl.setText(savedUrl);
            editUrl.setSelection(savedUrl.length());
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .titleBar(R.id.toolbar)
                .navigationBarColor(R.color.themeWhite)
                .init();
    }

    @OnClick(R.id.btn_save_url)
    public void onViewClicked() {
        String url = editUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            SharePrefUtils.saveString(DEBUG_URL_SAVE_KEY, "");
            LogUtils.d("clear debug url");
            Toast.makeText(act, "保存成功", Toast.LENGTH_SHORT).show();
            return;
        }
        String prefix = "https://";
        if (radioItemHttp.isChecked()) {
            prefix = "http://";
        }
        if (url.startsWith("http")) {
            url = url.substring(url.indexOf("/") + 2);
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        String finalUrl = prefix + url;
        SharePrefUtils.saveString(DEBUG_URL_SAVE_KEY, finalUrl);
        LogUtils.d("save url to local=" + finalUrl);
        Toast.makeText(act, "保存成功", Toast.LENGTH_SHORT).show();
    }

}
