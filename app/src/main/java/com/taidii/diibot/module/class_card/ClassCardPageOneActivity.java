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
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.entity.class_card.NewMedicineListRsp;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.module.class_card.adapter.ClassCardMedicineAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardStudentAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardTeacherAdapter;
import com.taidii.diibot.module.class_card.viewJzd.SwitchClassCardPopup;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
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

public class ClassCardPageOneActivity extends BaseActivity implements StudentSignSelectAdapter.ItemClickListener, SwitchClassCardPopup.SettingPopupListener {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rv_student_data)
    RecyclerView rvStudentData;
    @BindView(R.id.refresh_student_layout)
    TwinklingRefreshLayout refreshStudentLayout;
    @BindView(R.id.rv_teacher_data)
    RecyclerView rvTeacherData;
    @BindView(R.id.refresh_teacher_layout)
    TwinklingRefreshLayout refreshTeacherLayout;
    @BindView(R.id.tv_people_num)
    TextView tvPeopleNum;
    @BindView(R.id.rv_medicine_data)
    RecyclerView rvMedicineData;
    @BindView(R.id.refresh_medicine_layout)
    TwinklingRefreshLayout refreshMedicineLayout;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.tv_circle_01)
    TextView tvCircle01;
    @BindView(R.id.tv_circle_02)
    TextView tvCircle02;
    @BindView(R.id.tv_circle_03)
    TextView tvCircle03;
    @BindView(R.id.tv_circle_04)
    TextView tvCircle04;
    @BindView(R.id.tv_circle_05)
    TextView tvCircle05;
    @BindView(R.id.tv_teacher_more)
    TextView tvTeacherMore;
    @BindView(R.id.top_name)
    ImageView topName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
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
    private ClassCardStudentAdapter classCardStudentAdapter;


    private ClassCardTeacherAdapter classCardTeacherAdapter;

    private ClassCardMedicineAdapter classCardMedicineAdapter;

    private SwitchClassCardPopup switchClassCardPopup;//设置弹窗

    private int classId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_card_pageone;
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

        if (GlobalParams.mClassList != null && !GlobalParams.mClassList.isEmpty()) {
            tvClassName.setText(GlobalParams.mClassList.get(0).getName());
            classId = GlobalParams.mClassList.get(0).getId();
        }
        ImageViewUtils.loadImage(act, GlobalParams.center_logo, topName, R.drawable.avatar_loading2);
        tvName.setText(GlobalParams.center_name);
        ivSetting.setVisibility(View.VISIBLE);
        ivRefresh.setVisibility(View.VISIBLE);
        getStudentDataList();
//        getTeacherDataList();
//        getMedicineDataList();

        refreshStudentLayout.setEnableLoadmore(false);
        refreshStudentLayout.setEnableRefresh(false);

        refreshTeacherLayout.setEnableLoadmore(false);
        refreshTeacherLayout.setEnableRefresh(false);

        refreshMedicineLayout.setEnableLoadmore(false);
        refreshMedicineLayout.setEnableRefresh(false);


        refreshStudentLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
//                studentPage = 1;
//                getDataList();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
//                studentPage = studentPage + 1;
//                getDataList();
            }
        });
    }

    @Override
    protected void init() {
        Calendar calendar = Calendar.getInstance();
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String week = getResources().getString(weeks[weekIndex - 1]);
        String dateTime = sdf.format(new Date());
        tvTime.setText(dateTime.substring(0, 10) + "   " + week + "   " + dateTime.substring(10, 16));

//        Spanned medicineNum = Html.fromHtml(String.format(getResources().getString(R.string.class_card_medicine_num), "<font color= #DB7200>" + 2 + "</font>"));
//        tvPeopleNum.setText(medicineNum);

        GradientDrawable background01 = (GradientDrawable) tvCircle01.getBackground();
        background01.setColor(Color.parseColor("#52C41A"));

        GradientDrawable background02 = (GradientDrawable) tvCircle02.getBackground();
        background02.setColor(Color.parseColor("#6B90FF"));

        GradientDrawable background03 = (GradientDrawable) tvCircle03.getBackground();
        background03.setColor(Color.parseColor("#A972FF"));

        GradientDrawable background04 = (GradientDrawable) tvCircle04.getBackground();
        background04.setColor(Color.parseColor("#FA8C16"));

        GradientDrawable background05 = (GradientDrawable) tvCircle05.getBackground();
        background05.setColor(Color.parseColor("#F9C837"));

    }

    @OnClick({R.id.iv_setting, R.id.btn_left, R.id.btn_right, R.id.tv_teacher_more,R.id.iv_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right: {
                Intent intent = new Intent(ClassCardPageOneActivity.this, ClassCardPageTwoActivity.class);
                intent.putExtra("classId", classId);
                startActivity(intent);
                break;
            }
            case R.id.tv_teacher_more: {
                Intent intent = new Intent(ClassCardPageOneActivity.this, TeacherDetailActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("className", tvClassName.getText().toString().trim());
                startActivity(intent);
                break;
            }
            case R.id.iv_refresh:
                getStudentDataList();
                break;
            case R.id.iv_setting:
                if (switchClassCardPopup == null) {
                    switchClassCardPopup = new SwitchClassCardPopup(this, this);
                    switchClassCardPopup.showPopupWindow();
                } else {
                    switchClassCardPopup.showPopupWindow();
                }
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
        params.put("klass_id", classId + "");
//        params.put("date",  sdf.format(new Date()).substring(0, 10));
        OKHttpUtils.get(url, params, this, new OKHttpUtils.OnResponse<ClassCardStudentListBean>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardStudentListBean result) {
                if (result != null) {
                    showStudentData(result);
                    showTeacherData(result);
                }
                getMedicineDataList();
            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                hideLoadProgressBar();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private List<ClassCardStudentListBean.DataBean.StudentInfosBean> results = new ArrayList<>();

    private void showStudentData(ClassCardStudentListBean result) {
        if (studentPage == 1) {
            results.clear();
        }

        if (result.getData() == null) {
            return;
        }

        tvCircle01.setText(String.valueOf(result.getData().getSign_in_num()));
        tvCircle02.setText(String.valueOf(result.getData().getSign_out_num()));
        tvCircle03.setText(String.valueOf(result.getData().getPersonal_num()));
        tvCircle04.setText(String.valueOf(result.getData().getSick_num()));
        List<ClassCardStudentListBean.DataBean.StudentInfosBean> beans = result.getData().getStudent_infos();
        if (beans != null) {

            results.addAll(beans);
        }

        if (classCardStudentAdapter == null) {
            classCardStudentAdapter = new ClassCardStudentAdapter(results);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(act, 8, GridLayoutManager.VERTICAL, false);

            rvStudentData.setLayoutManager(gridLayoutManager);

//            rvStudentData.setLayoutManager(new LinearLayoutManager(this) {
//                @Override
//                public boolean canScrollVertically() {
//                    return true;
//                }
//            });

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


            //解决数据加载不完的问题
            rvStudentData.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvStudentData.setFocusable(false);
            rvStudentData.setAdapter(classCardStudentAdapter);
        } else {
            classCardStudentAdapter.setNewData(results);
            classCardStudentAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if (studentPage == 1 && results != null && results.isEmpty()) {
            classCardStudentAdapter.setEmptyView(getEmptyView());
            classCardStudentAdapter.notifyDataSetChanged();
        }
    }

    private View getEmptyView() {
        View view = LayoutInflater.from(act).inflate(R.layout.class_card_empty_resource, null);
        TextView name = view.findViewById(R.id.name);
        name.setVisibility(View.VISIBLE);
        return view;
    }

    private void getTeacherDataList() {
        String url = "https://www.taidii.cn/api/resources/app/resource/";
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", studentPage + "");
        OKHttpUtils.get(url, params, this, new OKHttpUtils.OnResponse<ClassCardStudentBean>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardStudentBean result) {
                if (result != null) {
//                    showTeacherData(result);
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
                hideLoadProgressBar();
            }
        });
    }

    private List<ClassCardStudentListBean.DataBean.TeacherInfosBean> resultsTeacher = new ArrayList<>();

    private void showTeacherData(ClassCardStudentListBean result) {
        resultsTeacher.clear();
        if (result.getData() == null) {
            return;
        }
        List<ClassCardStudentListBean.DataBean.TeacherInfosBean> beans = result.getData().getTeacher_infos();
        if (beans != null && !beans.isEmpty()) {
            resultsTeacher.add(beans.get(0));
            if (beans.size() >= 2) {
                resultsTeacher.add(beans.get(1));
            }
            if (beans.size() >= 3) {
                resultsTeacher.add(beans.get(2));
            }
        }


        if (classCardTeacherAdapter == null) {
            classCardTeacherAdapter = new ClassCardTeacherAdapter(resultsTeacher);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false);

            rvTeacherData.setLayoutManager(gridLayoutManager);

//            rvStudentData.setLayoutManager(new LinearLayoutManager(this) {
//                @Override
//                public boolean canScrollVertically() {
//                    return true;
//                }
//            });

            classCardTeacherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
            rvTeacherData.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvTeacherData.setFocusable(false);
            rvTeacherData.setAdapter(classCardTeacherAdapter);
        } else {
            classCardTeacherAdapter.setNewData(resultsTeacher);
            classCardTeacherAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if (resultsTeacher != null && resultsTeacher.isEmpty()) {
            classCardTeacherAdapter.setEmptyView(getEmptyView());
            classCardTeacherAdapter.notifyDataSetChanged();
        }
//        hideLoadProgressBar();
    }


    private void getMedicineDataList() {
        String url = ApiContainer.GET_STUDENT_MEDICINE_LIST_NEW;
        ArrayMap<String, String> params = new ArrayMap<>();
//        params.put("page", studentPage + "");
        params.put("username", SharePrefUtils.getString(Constant.USERNAME));
        params.put("password", SharePrefUtils.getString(Constant.PASSWORD));
        params.put("klass_id", classId + "");
        OKHttpUtils.get(url, params, this, new OKHttpUtils.OnResponse<NewMedicineListRsp>() {

            @Override
            public void onStart() {
                super.onStart();
//                showLoadProgressBar();
            }

            @Override
            public void onSuccess(NewMedicineListRsp result) {
                if (result != null) {
                    showMedicineData(result);
                    Spanned medicineNum = Html.fromHtml(String.format(getResources().getString(R.string.class_card_medicine_num), "<font color= #DB7200>" + result.getNum() + "</font>"));
                    tvPeopleNum.setText(medicineNum);
                }

            }

            @Override
            public void onError(IOException e, String url) {
                super.onError(e, url);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                hideLoadProgressBar();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

            }
        });
    }

    private List<NewMedicineListRsp.KlassListBean> resultsMedicine = new ArrayList<>();

    private void showMedicineData(NewMedicineListRsp result) {
        if (medicinePage == 1) {
            resultsMedicine.clear();
        }
        List<NewMedicineListRsp.KlassListBean> beans = result.getKlass_list();
        if (beans != null) {

            resultsMedicine.addAll(beans);
        }


        if (classCardMedicineAdapter == null) {
            classCardMedicineAdapter = new ClassCardMedicineAdapter(resultsMedicine);

            rvMedicineData.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

            classCardMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
            rvMedicineData.setHasFixedSize(true);
            rvMedicineData.setItemViewCacheSize(600);
            RecyclerView.RecycledViewPool recycledViewPool = new
                    RecyclerView.RecycledViewPool();
            rvMedicineData.setRecycledViewPool(recycledViewPool);
            //解决数据加载不完的问题
            rvMedicineData.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvMedicineData.setFocusable(false);
            rvMedicineData.setAdapter(classCardMedicineAdapter);
        } else {
            classCardMedicineAdapter.setNewData(resultsMedicine);
            classCardMedicineAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if (medicinePage == 1 & resultsMedicine != null && resultsMedicine.isEmpty()) {
            classCardMedicineAdapter.setEmptyView(getEmptyView());
            classCardMedicineAdapter.notifyDataSetChanged();
        }
        hideLoadProgressBar();
    }


    @Override
    public void choiceClass(ClassCardClassListBean.DataBean ben) {
        tvClassName.setText(ben.getName());
        classId = ben.getId();
        getStudentDataList();
    }

    @Override
    public void switchAttendance() {

    }
}
