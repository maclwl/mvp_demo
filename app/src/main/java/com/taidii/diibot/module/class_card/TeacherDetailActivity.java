package com.taidii.diibot.module.class_card;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.module.class_card.adapter.ClassCardMedicineAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardStudentAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardTeacherAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardTeacherDetailAdapter;
import com.taidii.diibot.module.class_card.viewJzd.SwitchClassCardPopup;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.ImageViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;
import xiao.free.horizontalrefreshlayout.refreshhead.NiceRefreshHeader;

public class TeacherDetailActivity extends BaseActivity implements StudentSignSelectAdapter.ItemClickListener ,SwitchClassCardPopup.SettingPopupListener, RefreshCallBack {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rv_student_data)
    RecyclerView rvStudentData;
    @BindView(R.id.refresh)
    HorizontalRefreshLayout refreshStudentLayout;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.top_name)
    ImageView topName;
    @BindView(R.id.tv_name)
    TextView tvName;
    private int[] weeks = new int[]{R.string.txt_course_sunday
            , R.string.txt_course_monday
            , R.string.txt_course_tuesday
            , R.string.txt_course_wednesday
            , R.string.txt_course_thursday
            , R.string.txt_course_friday
            , R.string.txt_course_saturday};
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private int studentPage = 1;
    private ClassCardTeacherDetailAdapter classCardStudentAdapter;

    private SwitchClassCardPopup switchClassCardPopup;//设置弹窗

    private ArrayList<ClassCardStudentListBean.DataBean.TeacherInfosBean> teacherInfosBeans = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_detail;
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
        ImageViewUtils.loadImage(act, GlobalParams.center_logo, topName, R.drawable.avatar_loading2);
        tvName.setText(GlobalParams.center_name);
        classId = getIntent().getIntExtra("classId",-1);
//        tvClassName.setText(getIntent().getStringExtra("className"));
        getStudentDataList();

//        refreshStudentLayout.setRefreshCallback(this);
//        refreshStudentLayout.setRefreshHeader(new NiceRefreshHeader(this), HorizontalRefreshLayout.LEFT);
//        refreshStudentLayout.setRefreshHeader(new NiceRefreshHeader(this), HorizontalRefreshLayout.RIGHT);


    }

    @Override
    public void onLeftRefreshing() {
        refreshStudentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshStudentLayout.onRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onRightRefreshing() {
        refreshStudentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mLayoutAdapter.getMore();
                refreshStudentLayout.onRefreshComplete();
            }
        }, 2000);
    }


    @Override
    protected void init() {
        Calendar calendar = Calendar.getInstance();
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String week = getResources().getString(weeks[weekIndex - 1]);
        String dateTime = sdf.format(new Date());
        tvTime.setText(dateTime.substring(0, 10) + "   " + week + "   " + dateTime.substring(10, 16));

    }

    @OnClick({R.id.iv_setting,R.id.tv_class_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                if(switchClassCardPopup ==null){
                    switchClassCardPopup = new SwitchClassCardPopup(this, this);
                    switchClassCardPopup.showPopupWindow();
                }else{
                    switchClassCardPopup.showPopupWindow();
                }
                break;
            case R.id.tv_class_name:
                finish();
                break;
        }
    }

    /*跳转*/
    @Override
    public void clickItem(int position, QrCodeStudentCollection signHistoryDataModel) {
    }

    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }



    private void getStudentDataList() {
        String url = ApiContainer.CLASS_CLASS_STUDENT_LIST;
//        String url = String.format(ApiContainer.CLASS_CLASS_STUDENT_LIST, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("klass_id", classId+"");
//        params.put("date",  sdf.format(new Date()).substring(0, 10));
        OKHttpUtils.get(url, params,this,new OKHttpUtils.OnResponse<ClassCardStudentListBean>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardStudentListBean result) {
                if(result!=null&&result.getData()!=null){
                    List<ClassCardStudentListBean.DataBean.TeacherInfosBean> beans = result.getData().getTeacher_infos();
                    if(beans!=null&&!beans.isEmpty()){
                        teacherInfosBeans.addAll(beans);
                    }
                    showStudentData();
                }

            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }
    private void showStudentData() {


        if (classCardStudentAdapter == null) {
            classCardStudentAdapter = new ClassCardTeacherDetailAdapter(teacherInfosBeans);


            rvStudentData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            classCardStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    currentPosition = position;
//                    ResourceBean.ResultsBean dataBean = (ResourceBean.ResultsBean) adapter.getItem(position);
//                    if(dataBean.getImages()!=null){
//                        Intent intent = new Intent(ResourcesPdfActivity.this, ResourcePdfDetailActivity.class);
//                        intent.putStringArrayListExtra("images", (ArrayList<String>) dataBean.getImages());
//                        intent.putExtra("title",dataBean.getTitle());
//                        intent.putExtra("read_ornot",dataBean.isRead_ornot());
//                        intent.putExtra("id",dataBean.getId());
//                        intent.putExtra("file_url",dataBean.getFile_url());
//                        ResourcesPdfActivity.this.startActivity(intent);
//                    }
                }
            });

            /*优化嵌套卡顿*/
            rvStudentData.setHasFixedSize(true);
            rvStudentData.setItemViewCacheSize(600);
            RecyclerView.RecycledViewPool recycledViewPool = new
                    RecyclerView.RecycledViewPool();
            rvStudentData.setRecycledViewPool(recycledViewPool);
            //解决数据加载不完的问题
            rvStudentData.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvStudentData.setFocusable(false);
            rvStudentData.setAdapter(classCardStudentAdapter);
        } else {
            classCardStudentAdapter.setNewData(teacherInfosBeans);
            classCardStudentAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if (studentPage == 1 && teacherInfosBeans != null && teacherInfosBeans.isEmpty()) {
            classCardStudentAdapter.setEmptyView(getEmptyView());
            classCardStudentAdapter.notifyDataSetChanged();
        }
        refreshStudentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadProgressBar();
            }
        }, 800);

    }

    private View getEmptyView() {
        View view = LayoutInflater.from(act).inflate(R.layout.class_card_empty_resource, null);
        TextView name = view.findViewById(R.id.name);
        name.setVisibility(View.VISIBLE);
        return view;
    }


    private int classId =-1;
    @Override
    public void choiceClass(ClassCardClassListBean.DataBean ben) {
        tvClassName.setText(ben.getName());
        classId = ben.getId();
    }
    @Override
    public void switchAttendance() {

    }
}
