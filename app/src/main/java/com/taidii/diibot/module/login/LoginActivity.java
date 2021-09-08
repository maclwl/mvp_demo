package com.taidii.diibot.module.login;

import android.Manifest;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.taidii.diibot.BuildConfig;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.module.debug.DebugUrlModifyActivity;
import com.taidii.diibot.module.login.conract.LoginContract;
import com.taidii.diibot.module.login.presenter.LoginPresenter;
import com.taidii.diibot.module.organ_main.OrganMainActivity;
import com.taidii.diibot.module.school_main.SchoolMainActivity;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.widget.popup.RegionSelectPopup;
import com.gyf.immersionbar.ImmersionBar;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements RegionSelectPopup.RegionSelectListener
            , LoginContract.View, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_psw)
    EditText editPsw;
    @BindView(R.id.im_region)
    ImageView imRegion;

    private RegionSelectPopup regionSelectPopup;//地区选择弹窗
    private static final int REQUEST_CODE_QR_CODE_PERMISSIONS = 1;//扫码权限

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        /*默认区域中国*/
        imRegion.setSelected(SharePrefUtils.getBoolean(Constant.IS_ABROAD,false));
    }

    @Override
    protected void init() {
        regionSelectPopup = new RegionSelectPopup(this,this);

    }

    @Override
    protected void initData() {
        /*判断本地是否存在用户名和密码（存在则自动登录）*/
        String username = SharePrefUtils.getString(Constant.USERNAME);
        String password = SharePrefUtils.getString(Constant.PASSWORD);
        if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            editName.setText(username);
            editPsw.setText(password);
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.btn_login, R.id.im_region})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                /*首先获取centerType*/
                mPresenter.getCenterType(editName.getText().toString(),editPsw.getText().toString());
                break;
            case R.id.im_region:
                regionSelectPopup.showPopupWindow();
                break;
        }
    }

    /*区域选择回调*/
    @Override
    public void chooseRegion(RegionSelectPopup.RegionEnum region) {

        regionSelectPopup.dismiss();
        switch (region){
            case CHINA:
                imRegion.setSelected(false);
                SharePrefUtils.saveBoolean(Constant.IS_ABROAD,false);
                changeBaseUrl();
                break;
            case SINGAPORE:
                imRegion.setSelected(true);
                SharePrefUtils.saveBoolean(Constant.IS_ABROAD,true);
                changeBaseUrl();
                break;
        }
    }

    /*环境切换*/
    private void changeBaseUrl(){
        String debugApiHost = SharePrefUtils.getString
                (DebugUrlModifyActivity.DEBUG_URL_SAVE_KEY, "");
        if (!BuildConfig.DEBUG) {
            if (SharePrefUtils.getBoolean(Constant.IS_ABROAD,false)){
                ApiContainer.API_HOST = ApiContainer.Base_Url_EN;
            }else {
                ApiContainer.API_HOST = ApiContainer.Base_Url_CN;
            }
        } else {
            if (SharePrefUtils.getBoolean(Constant.IS_ABROAD,false)){
                ApiContainer.API_HOST = TextUtils.isEmpty(debugApiHost) ? ApiContainer.Base_Url_EN : debugApiHost;
            }else {
                ApiContainer.API_HOST = TextUtils.isEmpty(debugApiHost) ? ApiContainer.Base_Url_CN : debugApiHost;
            }
        }
    }

    /*跳转学校首页*/
    @Override
    public void jumpToSchoolMain() {
        openActivity(SchoolMainActivity.class);
        finish();
    }

    /*跳转机构首页*/
    @Override
    public void jumpToOrganMain() {
        openActivity(OrganMainActivity.class);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QR_CODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, this.getResources().getString(R.string.scan_permission), REQUEST_CODE_QR_CODE_PERMISSIONS, perms);
        }
    }

}
