package com.taidii.diibot.module.class_card;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.CameryPhoto;
import com.taidii.diibot.entity.class_card.ClassCardMessagetBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardWeekPlanBean;
import com.taidii.diibot.entity.class_card.FoodRsp;
import com.taidii.diibot.entity.class_card.Recipes;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.module.class_card.adapter.ClassCardClassActivityAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardFoodAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardNoticeAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardWeekPlanAdapter;
import com.taidii.diibot.module.school_main.adapter.StudentSignSelectAdapter;
import com.taidii.diibot.net.HttpManager;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.ImageViewUtils;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassCardPageTwoActivity extends BaseActivity implements StudentSignSelectAdapter.ItemClickListener {


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
    @BindView(R.id.tv_look_week)
    TextView tvLookWeek;
    @BindView(R.id.rv_medicine_data)
    RecyclerView rvMedicineData;
    @BindView(R.id.refresh_medicine_layout)
    TwinklingRefreshLayout refreshMedicineLayout;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.tv_notice_more)
    TextView tvTeacherMore;
    @BindView(R.id.tv_look_food)
    TextView tvLookFood;
    @BindView(R.id.tv_class_activity)
    TextView tvClassActivity;
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
    private ClassCardFoodAdapter classCardStudentAdapter;


    private ClassCardNoticeAdapter classCardTeacherAdapter;

    private ClassCardWeekPlanAdapter classCardMedicineAdapter;

    private ClassCardClassActivityAdapter classCardClassActivityAdapter;
    private int classId = -1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_card_pagetwo;
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
        classId = getIntent().getIntExtra("classId",-1);
        tvName.setText(GlobalParams.center_name);
        getStudentDataList();
        getTeacherDataList();
        getMedicineDataList();
        getClassActivityDataList();

        refreshStudentLayout.setEnableLoadmore(false);
        refreshStudentLayout.setEnableRefresh(false);

        refreshTeacherLayout.setEnableLoadmore(false);
        refreshTeacherLayout.setEnableRefresh(false);

        refreshMedicineLayout.setEnableLoadmore(false);
        refreshMedicineLayout.setEnableRefresh(false);

        refreshClassActivity.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                pageCount = 1;
                lastCameraId = -1;
                getClassActivityDataList();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageCount = pageCount + 1;
                getClassActivityDataList();
            }
        });
    }

    @OnClick({R.id.iv_setting,R.id.btn_left,R.id.btn_right,R.id.tv_notice_more,R.id.tv_look_food,R.id.tv_look_week})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:

                break;
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_notice_more: {
                Intent intent = new Intent(ClassCardPageTwoActivity.this, NoticeDetailActivity.class);
                intent.putExtra("classId",classId);
                startActivity(intent);
                break;
            }
            case R.id.tv_look_food: {
//                Intent intent = new Intent(ClassCardPageTwoActivity.this, FoodDetailActivity.class);
//                startActivity(intent);
                break;
            }
            case R.id.tv_look_week: {
                Intent intent = new Intent(ClassCardPageTwoActivity.this, WeekPlanDetailActivity.class);
                startActivity(intent);
                break;
            }
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


    private void getStudentDataList() {
        String url = ApiContainer.CLASS_FOOD_LIST;
        ArrayMap<String, String> params = new ArrayMap<>();
//        params.put("page", studentPage + "");
        params.put("date",sdf.format(new Date()).substring(0, 10));
        params.put("username",SharePrefUtils.getString(Constant.USERNAME));
        params.put("password",SharePrefUtils.getString(Constant.PASSWORD));
        params.put("is_app",1+"");
        ArrayMap<String, String> headers = new ArrayMap<>();
        try {
            headers.put("Cookie", HttpManager.cookieHeader(act, ApiContainer.API_HOST + url));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OKHttpUtils.get(url,headers, params, this, new OKHttpUtils.OnResponse<FoodRsp>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(FoodRsp result) {
                if (result != null) {
                    showStudentData(result);
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

    private List<ClassCardStudentBean.ResultsBean> results = new ArrayList<>();

    private void showStudentData(FoodRsp rsp) {
        if (null == rsp)
            return;

         ArrayList<Recipes> mRecipesList = new ArrayList<>();
        if (mRecipesList.size() > 0) {
            mRecipesList.clear();
        }
        if (null != rsp.getContent()) {
            if (rsp.getContent().getMorning_snack() != null) {
                if (rsp.getContent().getMorning_snack().size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (String str : rsp.getContent().getMorning_snack()) {
                        buffer.append(str).append("\n");
                    }
                    Recipes recipes = new Recipes();
                    recipes.setType(3);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getMorning_snack_img());
                    recipes.setImage_thumbnail(rsp.getContent().getMorning_snack_img_thumbnail());
                    mRecipesList.add(recipes);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("无");
                    Recipes recipes = new Recipes();
                    recipes.setType(3);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getMorning_snack_img());
                    recipes.setImage_thumbnail(rsp.getContent().getMorning_snack_img_thumbnail());
                    mRecipesList.add(recipes);
                }
            }

            if (rsp.getContent().getBreakfast() != null) {
                if (rsp.getContent().getBreakfast().size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (String str : rsp.getContent().getBreakfast()) {
                        buffer.append(str).append("\n");
                    }
                    Recipes recipes = new Recipes();
                    recipes.setType(0);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getBreakfast_img());
                    recipes.setImage_thumbnail(rsp.getContent().getBreakfast_img_thumbnail());
                    mRecipesList.add(recipes);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("无");
                    Recipes recipes = new Recipes();
                    recipes.setType(0);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getBreakfast_img());
                    recipes.setImage_thumbnail(rsp.getContent().getBreakfast_img_thumbnail());
                    mRecipesList.add(recipes);
                }
            }

            if (rsp.getContent().getLunch() != null) {
                if (rsp.getContent().getLunch().size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (String str : rsp.getContent().getLunch()) {
                        buffer.append(str).append("\n");
                    }
                    Recipes recipes = new Recipes();
                    recipes.setType(1);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getLunch_img());
                    recipes.setImage_thumbnail(rsp.getContent().getLunch_img_thumbnail());
                    mRecipesList.add(recipes);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("无");
                    Recipes recipes = new Recipes();
                    recipes.setType(1);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getLunch_img());
                    recipes.setImage_thumbnail(rsp.getContent().getLunch_img_thumbnail());
                    mRecipesList.add(recipes);
                }
            }

            if (rsp.getContent().getNoon() != null) {
                if (rsp.getContent().getNoon().size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (String str : rsp.getContent().getNoon()) {
                        buffer.append(str).append("\n");
                    }
                    Recipes recipes = new Recipes();
                    recipes.setType(2);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getNoon_img());
                    recipes.setImage_thumbnail(rsp.getContent().getNoon_img_thumbnail());
                    mRecipesList.add(recipes);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("无");
                    Recipes recipes = new Recipes();
                    recipes.setType(2);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getNoon_img());
                    recipes.setImage_thumbnail(rsp.getContent().getNoon_img_thumbnail());
                    mRecipesList.add(recipes);
                }
            }

            if (rsp.getContent().getDinner() != null) {
                if (rsp.getContent().getDinner().size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (String str : rsp.getContent().getDinner()) {
                        buffer.append(str).append("\n");
                    }
                    Recipes recipes = new Recipes();
                    recipes.setType(4);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getDinner_img());
                    recipes.setImage_thumbnail(rsp.getContent().getDinner_img_thumbnail());
                    mRecipesList.add(recipes);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("无");
                    Recipes recipes = new Recipes();
                    recipes.setType(4);
                    recipes.setFood(buffer.toString().trim());
                    recipes.setImage(rsp.getContent().getDinner_img());
                    recipes.setImage_thumbnail(rsp.getContent().getDinner_img_thumbnail());
                    mRecipesList.add(recipes);
                }
            }

        }
        if (classCardStudentAdapter == null) {
            classCardStudentAdapter = new ClassCardFoodAdapter(mRecipesList);

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(act,8, GridLayoutManager.VERTICAL, false);
//
//            rvStudentData.setLayoutManager(gridLayoutManager);

            rvStudentData.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

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
            classCardStudentAdapter.setNewData(mRecipesList);
            classCardStudentAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if ( mRecipesList != null && mRecipesList.isEmpty()) {
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
        String url = ApiContainer.CLASS_MESSAGE_LIST;
        ArrayMap<String, String> params = new ArrayMap<>();
//        params.put("page", studentPage + "");
        params.put("klass_id",String.valueOf(classId));
        params.put("username",SharePrefUtils.getString(Constant.USERNAME));
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
//                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardMessagetBean result) {
                if (result != null) {
                    showTeacherData(result);
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

    private List<ClassCardMessagetBean.ResultsBean> resultsTeacher = new ArrayList<>();

    private void showTeacherData(ClassCardMessagetBean result) {

        List<ClassCardMessagetBean.ResultsBean> beans = result.getResults();
        if (beans != null && !beans.isEmpty()) {
            resultsTeacher.add(beans.get(0));
            if (beans.size() >= 2) {
                resultsTeacher.add(beans.get(1));
            }
        }

        if (classCardTeacherAdapter == null) {
            classCardTeacherAdapter = new ClassCardNoticeAdapter(resultsTeacher);

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(act,3, GridLayoutManager.VERTICAL, false);
//
//            rvTeacherData.setLayoutManager(gridLayoutManager);

            rvTeacherData.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

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
    }


    private void getMedicineDataList() {
        String url = ApiContainer.CLASS_WEEKPLAN_LIST;
        ArrayMap<String, String> params = new ArrayMap<>();
//        params.put("page", studentPage + "");
        params.put("klass_id",String.valueOf(classId));
        params.put("username",SharePrefUtils.getString(Constant.USERNAME));
        params.put("password",SharePrefUtils.getString(Constant.PASSWORD));
        params.put("kind",String.valueOf(0));
        ArrayMap<String, String> headers = new ArrayMap<>();
        try {
            headers.put("Cookie", HttpManager.cookieHeader(act, ApiContainer.API_HOST + url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OKHttpUtils.get(url, params, this, new OKHttpUtils.OnResponse<ClassCardWeekPlanBean>() {

            @Override
            public void onStart() {
                super.onStart();
//                showLoadProgressBar();
            }

            @Override
            public void onSuccess(ClassCardWeekPlanBean result) {
                if (result != null) {
                    showMedicineData(result);
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
//                hideLoadProgressBar();
            }
        });
    }

    private List<ClassCardWeekPlanBean.DataBean> resultsMedicine = new ArrayList<>();

    private void showMedicineData(ClassCardWeekPlanBean result) {
        if (medicinePage == 1) {
            resultsMedicine.clear();
        }
        List<ClassCardWeekPlanBean.DataBean> beans = result.getData();
        if (beans != null) {

            resultsMedicine.addAll(beans);
        }



        if (classCardMedicineAdapter == null) {
            classCardMedicineAdapter = new ClassCardWeekPlanAdapter(resultsMedicine);

            rvMedicineData.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

            classCardMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ClassCardWeekPlanBean.DataBean dataBean = (ClassCardWeekPlanBean.DataBean) adapter.getItem(position);
                        Intent intent = new Intent(ClassCardPageTwoActivity.this, WeekPlanDetailActivity.class);
                        intent.putExtra("dataBean", dataBean);
                    ClassCardPageTwoActivity.this.startActivity(intent);
                }
            });


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
    }

    private int lastCameraId = -1;
    private int pageCount = 1;
    private final int pageSize = 10;
    private void getClassActivityDataList() {
        String url = String.format(ApiContainer.GET_MOUNT_PIC, SharePrefUtils.getString(Constant.USERNAME),SharePrefUtils.getString(Constant.PASSWORD));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("page", String.valueOf(pageCount));
        jsonObject.addProperty("number", String.valueOf(pageSize));
//        praise_details=1
//        show_comments=1
        jsonObject.addProperty("praise_details", 1);
        jsonObject.addProperty("show_related_class", 1);

        if (lastCameraId > 0) {
            jsonObject.addProperty("currentid", String.valueOf(lastCameraId));
            jsonObject.addProperty("order", String.valueOf(-1));
        }
        if (classId!=-1) {
            jsonObject.addProperty("klass_id", classId);
            jsonObject.addProperty("version", "2");

        }

        OKHttpUtils.post(url, jsonObject, this, new OKHttpUtils.OnResponse<CameryPhoto>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadProgressBar();
            }

            @Override
            public void onSuccess(CameryPhoto result) {
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
                hideLoadProgressBar();
                refreshClassActivity.finishRefreshing();
                refreshClassActivity.finishLoadmore();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                refreshClassActivity.finishRefreshing();
                refreshClassActivity.finishLoadmore();
            }
        });
    }

    private List< CameryPhoto.PhotosBean> resultsClassActivity = new ArrayList<>();
    private void showClassActivityData(CameryPhoto result) {
        if (pageCount == 1) {
            resultsClassActivity.clear();
        }
        List< CameryPhoto.PhotosBean> beans = result.getPhotos();
        if (beans != null) {

            resultsClassActivity.addAll(beans);
        }

        if (resultsClassActivity.size() > 0) {
            lastCameraId = resultsClassActivity.get(resultsClassActivity.size() - 1).getBatch_id();
        }

        Collections.sort(resultsClassActivity, new Comparator<CameryPhoto.PhotosBean>() {
            @Override
            public int compare(CameryPhoto.PhotosBean o1, CameryPhoto.PhotosBean o2) {
                return DateUtil.isDateOneBigger(parsePicTime(o1.getPublish_at()).replace("T", " ").replace("Z", " "),
                        parsePicTime(o2.getPublish_at()).replace("T", " ").replace("Z", " "));
            }
        });



        if (classCardClassActivityAdapter == null) {
            classCardClassActivityAdapter = new ClassCardClassActivityAdapter(resultsClassActivity);

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

            /*优化嵌套卡顿*/
            rvClassActivity.setHasFixedSize(true);
            rvClassActivity.setItemViewCacheSize(600);
            RecyclerView.RecycledViewPool recycledViewPool = new
                    RecyclerView.RecycledViewPool();
            rvClassActivity.setRecycledViewPool(recycledViewPool);
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
        if (pageCount == 1 & resultsClassActivity != null && resultsClassActivity.isEmpty()) {
            classCardClassActivityAdapter.setEmptyView(getEmptyView());
            classCardClassActivityAdapter.notifyDataSetChanged();
        }
        hideLoadProgressBar();
    }

    private String parsePicTime(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(str);

            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            ca.add(Calendar.HOUR_OF_DAY, 8);
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(ca.getTime());

        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return null;
    }

}
