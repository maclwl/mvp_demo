package com.taidii.diibot.view.titlebar;

import android.content.Context;
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
import com.taidii.diibot.utils.ImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrganMainTopBarLayout extends RelativeLayout {

    @BindView(R.id.im_logo)
    RoundedImageView imLogo;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.ll_manger)
    LinearLayout llManger;
    @BindView(R.id.im_scan_class)
    ImageView imScanClass;
    @BindView(R.id.im_connect)
    ImageView imConnect;
    @BindView(R.id.im_rotate)
    ImageView imRotate;

    private boolean isScan = false;
    private RotateAnimation rotateAnimation;
    private OrganMainTopBarListener organMainTopBarListener;

    public enum ChangeMainEnum {
        CLASS, SCAN
    }

    public interface  OrganMainTopBarListener{

        void clickLogo();

        void clickContent();

        void clickClassScan(ChangeMainEnum status);

        void clickSetting();

        void clickRefresh();

        void closeBluetooth();
    }

    public void setOrganMainTopBarListener(OrganMainTopBarListener organMainTopBarListener){
        this.organMainTopBarListener = organMainTopBarListener;
    }

    public OrganMainTopBarLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public OrganMainTopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public OrganMainTopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_organ_main_top_bar, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.im_logo, R.id.im_setting, R.id.im_refresh, R.id.im_scan_class, R.id.im_connect, R.id.im_rotate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_logo:
                organMainTopBarListener.clickLogo();
                break;
            case R.id.im_setting:
                organMainTopBarListener.clickSetting();
                break;
            case R.id.im_refresh:
                organMainTopBarListener.clickRefresh();
                break;
            case R.id.im_scan_class:
                if (isScan) {
                    organMainTopBarListener.clickClassScan(ChangeMainEnum.CLASS);
                    imScanClass.setSelected(true);
                    llManger.setVisibility(VISIBLE);
                    isScan = false;
                } else {
                    organMainTopBarListener.clickClassScan(ChangeMainEnum.SCAN);
                    imScanClass.setSelected(false);
                    llManger.setVisibility(GONE);
                    isScan = true;
                }
                break;
            case R.id.im_connect:
                organMainTopBarListener.clickContent();
                break;
            case R.id.im_rotate:
                organMainTopBarListener.closeBluetooth();
                break;
        }
    }

    public void setDefault() {
        imScanClass.setSelected(true);
        llManger.setVisibility(VISIBLE);
        isScan = false;
    }

    public void setCenterInfo(Context context, String url, String content) {
        tvCenter.setText(content);
        ImageViewUtils.loadImage(context, url, imLogo, R.drawable.avatar_loading2);
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

    /*停止动画*/
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

}
