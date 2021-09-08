package com.taidii.diibot.view.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.enums.SingTypeEnum;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainTopTabLayout extends RelativeLayout {

    @BindView(R.id.tv_sin_in)
    TextView tvSinIn;
    @BindView(R.id.tv_sin_temp)
    TextView tvSinTemp;
    @BindView(R.id.tv_sin_out)
    TextView tvSinOut;
    @BindView(R.id.ll_tv_sign_in)
    LinearLayout llTvSignIn;
    @BindView(R.id.ll_tv_sign_temp)
    LinearLayout llTvSignTemp;
    @BindView(R.id.ll_tv_sign_out)
    LinearLayout llTvSignOut;
    @BindView(R.id.im_sign_in)
    ImageView imSignIn;
    @BindView(R.id.ll_im_sign_in)
    LinearLayout llImSignIn;
    @BindView(R.id.im_im_sign_temp)
    ImageView imImSignTemp;
    @BindView(R.id.ll_im_sign_temp)
    LinearLayout llImSignTemp;
    @BindView(R.id.im_sign_out)
    ImageView imSignOut;
    @BindView(R.id.ll_sign_out)
    LinearLayout llSignOut;
    @BindView(R.id.ll_ya)
    LinearLayout llYa;

    private MainTabListener mainTabListener;

    public MainTopTabLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public MainTopTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MainTopTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_main_tab, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_sin_in, R.id.tv_sin_temp, R.id.tv_sin_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sin_in:
                cancelSelect();
                imSignIn.setVisibility(VISIBLE);
                tvSinIn.setSelected(true);
                mainTabListener.tabSelect(SingTypeEnum.SIGN_IN);
                break;
            case R.id.tv_sin_temp:
                cancelSelect();
                imImSignTemp.setVisibility(VISIBLE);
                tvSinTemp.setSelected(true);
                mainTabListener.tabSelect(SingTypeEnum.SIGN_TEMP);
                break;
            case R.id.tv_sin_out:
                cancelSelect();
                imSignOut.setVisibility(VISIBLE);
                tvSinOut.setSelected(true);
                mainTabListener.tabSelect(SingTypeEnum.SIGN_OUT);
                break;
        }
    }

    private void cancelSelect() {
        imSignIn.setVisibility(GONE);
        imImSignTemp.setVisibility(GONE);
        imSignOut.setVisibility(GONE);
        tvSinIn.setSelected(false);
        tvSinTemp.setSelected(false);
        tvSinOut.setSelected(false);
    }

    public void setDefault() {
        cancelSelect();
        imSignIn.setVisibility(VISIBLE);
        tvSinIn.setSelected(true);
    }

    public interface MainTabListener {

        void tabSelect(SingTypeEnum signType);
    }

    public void setMainTabListener(MainTabListener mainTabListener) {
        this.mainTabListener = mainTabListener;
    }

    /*班级选择时显示体温签到*/
    public void changeClass() {
        llImSignTemp.setVisibility(VISIBLE);
        llTvSignTemp.setVisibility(VISIBLE);
    }

    /*扫码选择隐藏示体温签到*/
    public void changeScan() {
        llImSignTemp.setVisibility(GONE);
        llTvSignTemp.setVisibility(GONE);
    }

}
