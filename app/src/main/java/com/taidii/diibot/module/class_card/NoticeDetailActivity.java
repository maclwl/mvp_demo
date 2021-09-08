package com.taidii.diibot.module.class_card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.ClassCardMessagetBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.module.class_card.adapter.ClassCardClassActivityAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardFoodAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardNoticeAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardNoticeDetailAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardWeekPlanAdapter;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
import com.taidii.diibot.net.HttpManager;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeDetailActivity extends BaseActivity implements StudentSignSelectAdapter.ItemClickListener {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rv_class_activity)
    RecyclerView rvClassActivity;
    @BindView(R.id.refresh_class_activity)
    TwinklingRefreshLayout refreshClassActivity;
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
    private int medicinePage = 1;

    private ClassCardNoticeDetailAdapter classCardClassActivityAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_notice_detail;
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
        getClassActivityDataList();


        refreshClassActivity.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                studentPage = 1;
                getClassActivityDataList();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                studentPage = studentPage + 1;
                getClassActivityDataList();
            }
        });
    }

    @OnClick({R.id.tv_class_activity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_class_activity:
                finish();
                break;
        }
    }

    @Override
    protected void init() {
        Calendar calendar = Calendar.getInstance();
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String week = getResources().getString(weeks[weekIndex - 1]);
        String dateTime = sdf.format(new Date());
        tvTime.setText(dateTime.substring(0, 10) + "   " + week + "   " + dateTime.substring(10, 16));

    }

//    @OnClick(R.id.im_back)
//    public void onViewClicked() {
//        finish();
//    }

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





    private void getClassActivityDataList() {
        String url = ApiContainer.CLASS_MESSAGE_LIST;
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", studentPage + "");
        params.put("klass_id",String.valueOf(getIntent().getIntExtra("classId",-1)));
        params.put("username", SharePrefUtils.getString(Constant.USERNAME));
        params.put("password",SharePrefUtils.getString(Constant.PASSWORD));
        ArrayMap<String, String> headers = new ArrayMap<>();
        try {
            headers.put("Cookie", HttpManager.cookieHeader(act, ApiContainer.API_HOST + url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OKHttpUtils.get(url, params, this, new OKHttpUtils.OnResponse<ClassCardMessagetBean>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardMessagetBean result) {
                if (result != null) {
                    showClassActivityData(result);
                }

            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                refreshClassActivity.finishRefreshing();
                refreshClassActivity.finishLoadmore();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideLoadProgressBar();
                refreshClassActivity.finishRefreshing();
                refreshClassActivity.finishLoadmore();
            }
        });
    }

    private List<ClassCardMessagetBean.ResultsBean> resultsClassActivity = new ArrayList<>();

    private void showClassActivityData(ClassCardMessagetBean result) {
        if (studentPage == 1) {
            resultsClassActivity.clear();
        }
        List<ClassCardMessagetBean.ResultsBean> beans = result.getResults();
        if (beans != null) {

            resultsClassActivity.addAll(beans);
        }



        if (classCardClassActivityAdapter == null) {
            classCardClassActivityAdapter = new ClassCardNoticeDetailAdapter(resultsClassActivity);

            rvClassActivity.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

            classCardClassActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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


            //解决数据加载不完的问题
            rvClassActivity.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvClassActivity.setFocusable(false);
            rvClassActivity.setAdapter(classCardClassActivityAdapter);
        } else {
            classCardClassActivityAdapter.setNewData(resultsClassActivity);
            classCardClassActivityAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if (studentPage == 1 & resultsClassActivity != null && resultsClassActivity.isEmpty()) {
            classCardClassActivityAdapter.setEmptyView(getEmptyView());
            classCardClassActivityAdapter.notifyDataSetChanged();
        }
    }

    private View getEmptyView() {
        View view = LayoutInflater.from(act).inflate(R.layout.class_card_empty_resource, null);
        TextView name = view.findViewById(R.id.name);
        name.setVisibility(View.VISIBLE);
        return view;
    }

}
