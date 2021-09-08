package com.taidii.diibot.module.school_main;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.enums.GeneralTypeEnum;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.HealthCheckBody;
import com.taidii.diibot.entity.school.HealthCollectionModel;
import com.taidii.diibot.entity.school.HealthContentModel;
import com.taidii.diibot.entity.school.HealthRegionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.module.school_main.adapter.HealthChildAdapter;
import com.taidii.diibot.module.school_main.contract.HealthContract;
import com.taidii.diibot.module.school_main.presenter.HealthPresenter;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.HealthDataManage;
import com.taidii.diibot.utils.SharePrefUtils;
import com.taidii.diibot.view.health.HealthRegionLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 处理的相关条件都是以本地字典中的value为约束条件（列表等相关数据都为本地静态数据）
 */
public class HealthCheckActivity extends BaseMvpActivity<HealthPresenter> implements HealthContract.View, HealthChildAdapter.ItemClickListener {

    @BindView(R.id.rel_health_check)
    RelativeLayout relHealthCheck;
    @BindView(R.id.rel_top)
    RelativeLayout relTop;
    @BindView(R.id.recycler_child_list)
    RecyclerView recyclerChildList;
    @BindView(R.id.health_layout)
    HealthRegionLayout healthLayout;
    @BindView(R.id.edit_remark)
    EditText editRemark;

    private SchoolMainEnum type;//跳转类别
    private SingTypeEnum signType;//签到类型
    private GeneralTypeEnum generalType;//默认是否有疾病
    private HealthChildAdapter healthChildAdapter;
    private MainSchoolContentModel mainSchoolContentModel;//学生或老师信息
    private HealthCheckBody healthCheckBody = new HealthCheckBody();//体侧请求body
    private List<HealthCollectionModel> collectionModelList = new ArrayList<>();//本地初始所有数据

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_check;
    }

    @Override
    protected HealthPresenter createPresenter() {
        return new HealthPresenter();
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .titleBar(relTop)
                .statusBarAlpha(0)
                .statusBarColor(R.color.themeWhite)
                .navigationBarColor(R.color.themeWhite)
                .init();
    }

    @Override
    protected void initView() {
        healthChildAdapter = new HealthChildAdapter(this);
        recyclerChildList.setLayoutManager(new LinearLayoutManager(this));
        recyclerChildList.setAdapter(healthChildAdapter);
    }

    @Override
    protected void init() {
        collectionModelList = HealthDataManage.getHealthCollectionList();//初始原始本地数据
        type = (SchoolMainEnum) getIntent().getSerializableExtra(Constant.TYPE);
        signType = (SingTypeEnum) getIntent().getSerializableExtra(Constant.SIGN_TYPE);
        generalType = (GeneralTypeEnum) getIntent().getSerializableExtra(Constant.GENERAL_TYPE);
        mainSchoolContentModel = (MainSchoolContentModel) getIntent().getSerializableExtra(Constant.SCHOOL_CONTENT_MODEL);
        initChangeUI(signType);
        initListener();

        /*设置请求参数*/
        StudentsBean student = mainSchoolContentModel.getStudentsBean();
        healthCheckBody.setStudentid(student.getCenterStudentId());
        healthCheckBody.setType(3);
        switch (type) {
            case BUSES:
                healthCheckBody.setBusId(student.getBusId());
                break;
            default:
                healthCheckBody.setBusId(0);
                break;
        }
        /*是否设置温度默认值*/
        if (SharePrefUtils.getBoolean(Constant.IS_TEMP_DEFAULT)) {
            healthCheckBody.setTemperature(36.5f);
        }
        /**/
        healthLayout.setGender(mainSchoolContentModel.getStudentsBean().getGender());
    }

    @Override
    protected void initData() {
        /*通过跳转判断初始默认*/
        switch (generalType){
            case IS_GENERAL:
                mPresenter.initGeneralIsDisease(collectionModelList);
                break;
            case NO_GENERAL:
                mPresenter.initGeneralNoDisease(collectionModelList);
                break;
        }
    }

    /*左上标bg*/
    private void initChangeUI(SingTypeEnum signType) {
        switch (signType) {
            case SIGN_IN:
                relHealthCheck.setBackground(this.getResources().getDrawable(R.drawable.bg_class_green));
                break;
            case SIGN_TEMP:
                relHealthCheck.setBackground(this.getResources().getDrawable(R.drawable.bg_class_orange));
                break;
            case SIGN_OUT:
                relHealthCheck.setBackground(this.getResources().getDrawable(R.drawable.bg_class_blue));
                break;
        }
    }

    /*部位button监听*/
    public void initListener() {
        healthLayout.setHealthRegionListener(new HealthRegionLayout.HealthRegionListener() {
            @Override
            public void clickRegionButton(String key) {
                mPresenter.clickRegionButton(key, collectionModelList);
            }
        });
    }

    /*本地修改之后的数据*/
    @Override
    public void localChangeData(List<HealthCollectionModel> collectionModelList) {
        this.collectionModelList = collectionModelList;
    }

    /*部位button刷新*/
    @Override
    public void regionButtonChange(HealthRegionModel eyeRegion, HealthRegionModel mouthRegion, HealthRegionModel handRegion, HealthRegionModel buttockRegion, HealthRegionModel generalRegion, HealthRegionModel faceRegion, HealthRegionModel neckRegion, HealthRegionModel chestRegion, HealthRegionModel footRegion) {
        healthLayout.regionSetChange(eyeRegion, mouthRegion, handRegion, buttockRegion, generalRegion, faceRegion, neckRegion, chestRegion, footRegion);
    }

    /*切换刷新病症列表*/
    @Override
    public void symptomListSetChange(String key, List<HealthContentModel> contentModelList) {
        healthChildAdapter.setDataList(contentModelList, key);
    }

    /*体侧上传成功*/
    @Override
    public void healthCheckSuccess() {
        /*设置值*/
        StudentsBean studentSign = mainSchoolContentModel.getStudentsBean();
        studentSign.setHealthCheck(true);
        mainSchoolContentModel.setStudentsBean(studentSign);
        EventBusUtil.sendEvent(new Event(0,mainSchoolContentModel));
        finish();
    }

    /*病症列表item点击*/
    @Override
    public void clickContentItem(String key) {
        mPresenter.diseaseListChange(key, healthChildAdapter.getDataList(), collectionModelList);
    }

    @OnClick({R.id.im_back, R.id.im_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.im_confirm:
                mPresenter.healthCheckRequest(editRemark.getText().toString(),getHealthCheckBody(),collectionModelList);
                break;
        }
    }

    private HealthCheckBody getHealthCheckBody() {
        healthCheckBody.setDate(DateUtil.getStringDate());
        healthCheckBody.setRemarks("");
        healthCheckBody.setCommon("");
        return healthCheckBody;
    }

}
