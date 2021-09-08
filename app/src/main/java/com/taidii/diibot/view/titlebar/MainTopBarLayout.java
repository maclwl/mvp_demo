package com.taidii.diibot.view.titlebar;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.taidii.diibot.R;
import com.taidii.diibot.app.BaseApplication;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainTopBarLayout extends RelativeLayout {

    @BindView(R.id.im_logo)
    RoundedImageView imLogo;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.ll_manger)
    LinearLayout llManger;
    @BindView(R.id.im_scan_class)
    ImageView imScanClass;
    @BindView(R.id.im_input_tip)
    ImageView imInputTip;
    @BindView(R.id.im_input_anim)
    ImageView imInputAnim;
    @BindView(R.id.im_input)
    ImageView imInput;
    @BindView(R.id.im_rotate)
    ImageView imRotate;
    @BindView(R.id.im_connect)
    ImageView imConnect;
    @BindView(R.id.ll_screen)
    LinearLayout llScreen;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search_result)
    TextView tvSearchResult;
    @BindView(R.id.tv_scan_class)
    TextView tvScanClass;
    @BindView(R.id.tv_sign_in_content)
    TextView tvSignInContent;
    @BindView(R.id.tv_sign_out_content)
    TextView tvSignOutContent;

    private boolean isScan = true;
    private RotateAnimation rotateAnimation;
    private AnimationDrawable animationDrawable;
    private MainTopBarListener mainTopBarListener;

    public enum ChangeMainEnum {
        CLASS, SCAN
    }

    public interface MainTopBarListener {

        void clickLogo();

        void clickContent();

        void clickClassScan(ChangeMainEnum status);

        void clickVisitor();

        void clickSetting();

        void clickRefresh();

        void input();

        void closeBluetooth();

        void search();

        void closeSearch();

        void faceRecognize();
    }

    public void setMainTopBarListener(MainTopBarListener mainTopBarListener) {
        this.mainTopBarListener = mainTopBarListener;
    }

    public MainTopBarLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public MainTopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MainTopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_main_top_bar, this);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.im_logo, R.id.im_connect, R.id.im_scan_class, R.id.im_visitor, R.id.im_setting
            , R.id.im_refresh, R.id.im_input, R.id.im_rotate, R.id.im_search, R.id.im_close_search, R.id.ll_recognize})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_logo:
                mainTopBarListener.clickLogo();
                break;
            case R.id.im_connect:
                mainTopBarListener.clickContent();
                break;
            case R.id.im_scan_class:
                if (isScan) {
                    mainTopBarListener.clickClassScan(ChangeMainEnum.CLASS);
                    imScanClass.setSelected(true);
                    llManger.setVisibility(VISIBLE);
                    tvScanClass.setText(BaseApplication.getInstance().getResources().getString(R.string.scan_attendance));
                    isScan = false;
                } else {
                    mainTopBarListener.clickClassScan(ChangeMainEnum.SCAN);
                    imScanClass.setSelected(false);
                    llManger.setVisibility(GONE);
                    tvScanClass.setText(BaseApplication.getInstance().getResources().getString(R.string.class_list));
                    isScan = true;
                }
                break;
            case R.id.im_visitor:
                mainTopBarListener.clickVisitor();
                break;
            case R.id.im_setting:
                mainTopBarListener.clickSetting();
                break;
            case R.id.im_refresh:
                mainTopBarListener.clickRefresh();
                break;
            case R.id.im_input:
                mainTopBarListener.input();
                break;
            case R.id.im_rotate:
                mainTopBarListener.closeBluetooth();
                break;
            case R.id.im_search:
                mainTopBarListener.search();
                break;
            case R.id.im_close_search:
                mainTopBarListener.closeSearch();
                break;
            case R.id.ll_recognize:
                mainTopBarListener.faceRecognize();
                break;
        }
    }

    public void setDefault() {
        imScanClass.setSelected(false);
        llManger.setVisibility(GONE);
        isScan = true;
    }

    public void setCenterInfo(Context context, String url, String content) {
        tvCenter.setText(content);
        ImageViewUtils.loadImage(context, url, imLogo, R.drawable.avatar_loading2);
    }

    public void setImInputTipChange(boolean isVisible) {
        if (isVisible) {
            imInputTip.setVisibility(VISIBLE);
        } else {
            imInputTip.setVisibility(GONE);
        }
    }

    /*上传帧动画*/
    public void startInputAnim() {
        imInput.setVisibility(GONE);
        imInputAnim.setVisibility(VISIBLE);
        animationDrawable = (AnimationDrawable) imInputAnim.getBackground();
        animationDrawable.start();
    }

    public void stopInputAnim() {
        imInput.setVisibility(VISIBLE);
        imInputAnim.setVisibility(GONE);
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }
    }

    /*蓝牙连接旋转动画*/
    public void startRotate() {
        imRotate.setVisibility(VISIBLE);
        imConnect.setVisibility(GONE);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setDuration(2000);//设置动画持续周期
        rotateAnimation.setRepeatCount(-1);//设置重复次数
        rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotateAnimation.setStartOffset(10);//执行前的等待时间
        imRotate.setAnimation(rotateAnimation);
    }

    public void stopRotate() {
        imRotate.setVisibility(GONE);
        imConnect.setVisibility(VISIBLE);
        imRotate.clearAnimation();
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            rotateAnimation = null;
        }
    }

    /*蓝牙连接状态*/
    public void setConnectStatus(boolean isConnect) {
        imConnect.setSelected(isConnect);
    }

    /*切换搜索结果*/
    public void setChangeSearch(String searchResult) {
        llScreen.setVisibility(GONE);
        llSearch.setVisibility(VISIBLE);
        tvSearchResult.setText(searchResult);
    }

    /*切换选项功能*/
    public void setChangeScreen() {
        tvSearchResult.setText("");
        llSearch.setVisibility(GONE);
        llScreen.setVisibility(VISIBLE);
    }


    public void setAllSignInCount(int totalCount, int signInCount, int signOutCount){
        String text = "<font color='#3ed499'>"+signInCount
                +"</font>"+"/"+"<font color='#2b2b2b'>"+totalCount
                +"</font>";
        tvSignInContent.setText(Html.fromHtml(text));
        String text1 = "<font color='#f0a20d'>"+signOutCount
                +"</font>"+"/"+"<font color='#2b2b2b'>"+signInCount
                +"</font>";
        tvSignOutContent.setText(Html.fromHtml(text1));
    }
}
