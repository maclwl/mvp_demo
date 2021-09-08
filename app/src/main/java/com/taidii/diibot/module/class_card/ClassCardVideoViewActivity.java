package com.taidii.diibot.module.class_card;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.module.class_card.viewJzd.JZMediaExo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class ClassCardVideoViewActivity extends BaseActivity {


    @BindView(R.id.videoController1)
    JzvdStd videoController1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_card_video;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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

    }

    @Override
    protected void init() {
        videoController1.setUp(getIntent().getStringExtra("videoUrl"), "", Jzvd.SCREEN_NORMAL, JZMediaExo.class);
        videoController1.fullscreenButton.setVisibility(View.GONE);
        videoController1.posterImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        videoController1.progressBar.setEnabled(false);
//        long position = 10000;
//        long duration = videoController1.getDuration();
//        int progress = (int) (position * 100 / (duration == 0 ? 1 : duration));
//        videoController1.onProgress(progress, position, duration);
        Glide.with(act).load(getIntent().getStringExtra("imageUrl")).into(videoController1.posterImageView);
//        videoController1.startVideo();

    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //播放器未处于播放状态mediaInterface为空，学习进度未百分百离开界面保存当前视频播放进度
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
    }


}
