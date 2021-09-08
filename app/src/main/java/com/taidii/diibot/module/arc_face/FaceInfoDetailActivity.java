package com.taidii.diibot.module.arc_face;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.gyf.immersionbar.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.face.FaceRegisterResult;
import com.taidii.diibot.entity.school.GuardiansBean;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.StaffsBean;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.module.arc_face.adapter.FaceGuardianAdapter;
import com.taidii.diibot.module.arc_face.contract.FaceInfoDetailContract;
import com.taidii.diibot.module.arc_face.presenter.FaceInfoDetailPresenter;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脸相关有（学生，老师，家长）
 */

public class FaceInfoDetailActivity extends BaseMvpActivity<FaceInfoDetailPresenter> implements FaceInfoDetailContract.View, FaceGuardianAdapter.ItemClickListener {

    @BindView(R.id.rel_top_bar)
    RelativeLayout relTopBar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.im_head)
    RoundedImageView imHead;
    @BindView(R.id.tv_register_title)
    TextView tvRegisterTitle;
    @BindView(R.id.guardians_list_view)
    RecyclerView guardiansListView;
    @BindView(R.id.ll_guardians)
    LinearLayout llGuardians;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;

    /*跳转传值数据*/
    protected SchoolMainEnum type;//跳转类别
    private List<GuardiansBean> guardianList = new ArrayList<>();//接送家长
    protected MainSchoolContentModel mainSchoolContentModel;//学生或老师信息

    /*接送家长*/
    private FaceGuardianAdapter faceGuardianAdapter;

    /*注册刷新标记*/
    private int position;
    private MateGuardianBean mateGuardianBean;
    private List<MateGuardianBean> mateGuardianBeanList = new ArrayList<>();

    @Override
    protected FaceInfoDetailPresenter createPresenter() {
        return new FaceInfoDetailPresenter();
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .titleBar(relTopBar)
                .statusBarAlpha(0)
                .statusBarColor(R.color.themeWhite)
                .navigationBarColor(R.color.themeWhite)
                .init();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_info_detail;
    }

    @Override
    protected void initView() {
        faceGuardianAdapter = new FaceGuardianAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        guardiansListView.setLayoutManager(linearLayoutManager);
        guardiansListView.setAdapter(faceGuardianAdapter);
        guardiansListView.getItemAnimator().setAddDuration(0);
        guardiansListView.getItemAnimator().setChangeDuration(0);
        guardiansListView.getItemAnimator().setMoveDuration(0);
        guardiansListView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) guardiansListView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    protected void init() {
        /*获取跳转传值*/
        type = (SchoolMainEnum) getIntent().getSerializableExtra(Constant.TYPE);
        guardianList = (List<GuardiansBean>) getIntent().getSerializableExtra(Constant.GUARDIANS_LIST);
        mainSchoolContentModel = (MainSchoolContentModel) getIntent().getSerializableExtra(Constant.SCHOOL_CONTENT_MODEL);
        initChangeUI(type);
    }

    private void initChangeUI(SchoolMainEnum type) {
        /*判断类别*/
        switch (type) {
            case CLASS:
            case BUSES:
                if (mainSchoolContentModel.getStudentsBean() != null) {
                    tvName.setText(mainSchoolContentModel.getStudentsBean().getName());
                    ImageViewUtils.loadImage(this, mainSchoolContentModel.getStudentsBean().getFaceImage(), imHead, R.drawable.avatar_loading2);
                }
                if (mainSchoolContentModel.getStudentsBean().isRegisterFace()) {
                    llRegister.setBackground(getResources().getDrawable(R.drawable.sign_avr_layout_bg));
                    tvRegisterTitle.setText(R.string.click_reentry);
                    tvName.setTextColor(getResources().getColor(R.color.colorTheme));
                } else {
                    tvRegisterTitle.setText(R.string.click_entry);
                    llRegister.setBackground(getResources().getDrawable(R.drawable.entry_face_avatar_bg));
                    tvName.setTextColor(getResources().getColor(R.color.colorRed));
                }
                /*展示接送家长*/
                llGuardians.setVisibility(View.VISIBLE);
                break;
            case STAFF:
                if (mainSchoolContentModel.getStaffsBean() != null) {
                    tvName.setText(mainSchoolContentModel.getStaffsBean().getName());
                    ImageViewUtils.loadImage(this, mainSchoolContentModel.getStaffsBean().getFaceImage(), imHead, R.drawable.avatar_loading);
                }
                if (mainSchoolContentModel.getStaffsBean().isRegisterFace()) {
                    llRegister.setBackground(getResources().getDrawable(R.drawable.sign_avr_layout_bg));
                    tvRegisterTitle.setText(R.string.click_reentry);
                    tvName.setTextColor(getResources().getColor(R.color.colorTheme));
                } else {
                    tvRegisterTitle.setText(R.string.click_entry);
                    llRegister.setBackground(getResources().getDrawable(R.drawable.entry_face_avatar_bg));
                    tvName.setTextColor(getResources().getColor(R.color.colorRed));
                }
                /*隐藏接送家长*/
                llGuardians.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void initData() {
        /*判断类别，如果是class和buss匹配家长*/
        switch (type) {
            case CLASS:
            case BUSES:
                mPresenter.mateGuardian(guardianList, mainSchoolContentModel);
                break;
        }
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case 10:
                FaceRegisterResult result = (FaceRegisterResult) event.getData();
                switch (result.getType()){
                    case Constant.FACE_STUDENT:
                    case Constant.FACE_STAFF:
                        llRegister.setBackground(getResources().getDrawable(R.drawable.sign_avr_layout_bg));
                        tvRegisterTitle.setText(R.string.click_reentry);
                        tvName.setTextColor(getResources().getColor(R.color.colorTheme));
                        ImageViewUtils.loadImage(this,result.getImage(),imHead,R.drawable.avatar_loading2);
                        break;
                    case Constant.FACE_GUARDIAN:
                        mateGuardianBean.setRegisterFace(true);
                        mateGuardianBean.setFaceImage(result.getImage());
                        mateGuardianBeanList.set(position,mateGuardianBean);
                        faceGuardianAdapter.notifySetChanged(mateGuardianBeanList);
                        break;
                }
                break;
        }
    }

    @OnClick({R.id.im_close, R.id.im_confirm, R.id.ll_register})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.im_close:
            case R.id.im_confirm:
                finish();
                break;
            case R.id.ll_register:
                switch (type) {
                    case CLASS:
                    case BUSES:
                        StudentsBean student = mainSchoolContentModel.getStudentsBean();
                        jumpToRegister(student.getCenterStudentId(), student.getName(),Constant.FACE_STUDENT);
                        break;
                    case STAFF:
                        StaffsBean staff = mainSchoolContentModel.getStaffsBean();
                        jumpToRegister(staff.getStaffId(), staff.getName(),Constant.FACE_STAFF);
                        break;
                }
                break;
        }
    }

    @Override
    public void mateGuardianResult(List<MateGuardianBean> mateGuardianBeanList) {
        this.mateGuardianBeanList = mateGuardianBeanList;
        faceGuardianAdapter.setDataList(mateGuardianBeanList);
    }

    @Override
    public void itemClick(int position,MateGuardianBean mateGuardianBean) {
        this.position = position;
        this.mateGuardianBean = mateGuardianBean;
        jumpToRegister(mateGuardianBean.getId(), mateGuardianBean.getName(),Constant.FACE_GUARDIAN);
    }

    private void jumpToRegister(int id, String name,String type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID, id);
        bundle.putString(Constant.NAME, name);
        bundle.putString(Constant.TYPE, type);
        openActivity(CollectionFaceActivity.class, bundle);
    }

}
