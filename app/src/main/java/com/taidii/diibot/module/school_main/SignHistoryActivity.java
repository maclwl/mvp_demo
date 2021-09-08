package com.taidii.diibot.module.school_main;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.SignHistoryDataModel;
import com.taidii.diibot.module.school_main.adapter.SignHistoryAdapter;
import com.taidii.diibot.module.school_main.contract.SignHistoryContract;
import com.taidii.diibot.module.school_main.presenter.SignHistoryPresenter;
import com.taidii.diibot.utils.DatePickerUtil;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.widget.popup.SignHistoryDeletePopup;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SignHistoryActivity extends BaseMvpActivity<SignHistoryPresenter> implements SignHistoryDeletePopup.HistoryDeleteListener
        ,DatePickerUtil.DatePickerListener, SignHistoryContract.View{

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.history_list_recycler)
    RecyclerView historyListRecycler;

    private int deletePosition;
    private SignHistoryDataModel deleteModel;
    protected SchoolMainEnum type;//跳转类别
    protected SingTypeEnum signType;//签到类型
    protected MainSchoolContentModel mainSchoolContentModel;//学生或老师信息
    private DatePickerUtil datePickerUtil;
    private SignHistoryAdapter signHistoryAdapter;
    private SignHistoryDeletePopup signHistoryDeletePopup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_history;
    }

    @Override
    protected SignHistoryPresenter createPresenter() {
        return new SignHistoryPresenter();
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        signHistoryAdapter = new SignHistoryAdapter();
        historyListRecycler.setLayoutManager(new LinearLayoutManager(this));
        historyListRecycler.setAdapter(signHistoryAdapter);
        tvStartTime.setText(DateUtil.getStringDateShort());
        tvEndTime.setText(DateUtil.getStringDateShort());
    }

    @Override
    protected void init() {
        /*获取跳转传值*/
        type = (SchoolMainEnum) getIntent().getSerializableExtra(Constant.TYPE);
        signType = (SingTypeEnum) getIntent().getSerializableExtra(Constant.SIGN_TYPE);
        mainSchoolContentModel = (MainSchoolContentModel) getIntent().getSerializableExtra(Constant.SCHOOL_CONTENT_MODEL);

        datePickerUtil = new DatePickerUtil(this);
        signHistoryDeletePopup = new SignHistoryDeletePopup(this,this);
        signHistoryDeletePopup.setTitle(this.getResources().getString(R.string.confirm_delete_sign_history));
        signHistoryDeletePopup.setOutSideDismiss(false);
        initListener();
    }

    @Override
    protected void initData() {
        getHistoryList(mainSchoolContentModel.getStudentsBean().getCenterStudentId(),DateUtil.getStringDateShort(),DateUtil.getStringDateShort());
    }

    /*获取列表数据*/
    private void getHistoryList(int studentId, String fromDate, String toDate){
        mPresenter.getStudentHistory(studentId,fromDate,toDate);
    }

    private void initListener(){
        signHistoryAdapter.setItemClickListener(new SignHistoryAdapter.ItemClickListener() {
            @Override
            public void clickItem(int position,SignHistoryDataModel signHistoryDataModel) {
                deletePosition = position;
                deleteModel = signHistoryDataModel;
                signHistoryDeletePopup.showPopupWindow();
            }
        });
    }

    @OnClick({R.id.im_back, R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_start_time:
                datePickerUtil.historyDatePick(this,DatePickerUtil.DatePickerEnum.START_DATE);
                break;
            case R.id.tv_end_time:
                datePickerUtil.historyDatePick(this,DatePickerUtil.DatePickerEnum.END_DATE);
                break;
        }
    }

    /*列表数据*/
    @Override
    public void signHistoryList(List<SignHistoryDataModel> signHistoryList) {
        signHistoryAdapter.setDataList(signHistoryList);
    }

    /*刷新列表*/
    @Override
    public void refreshHistoryList(List<SignHistoryDataModel> signHistoryList) {
        signHistoryAdapter.notifyDataChanged(signHistoryList);
    }

    /*删除记录*/
    @Override
    public void delete() {
        mPresenter.deleteSignHistory(deleteModel.getAttendances().getId(),deleteModel.getAttendances().getType());
    }

    /*删除记录成功回调*/
    @Override
    public void deleteSuccess() {
        mPresenter.refreshHistory(deletePosition,signHistoryAdapter.getDataList());
    }

    /*日期选择回调*/
    @Override
    public void selectDate(String date, DatePickerUtil.DatePickerEnum type) {
        switch (type){
            case START_DATE:
                tvStartTime.setText(date);
                break;
            case END_DATE:
                tvEndTime.setText(date);
                break;
        }
        /*重新拉取数据*/
        getHistoryList(mainSchoolContentModel.getStudentsBean().getCenterStudentId(),tvStartTime.getText().toString(),tvEndTime.getText().toString());
    }

}
