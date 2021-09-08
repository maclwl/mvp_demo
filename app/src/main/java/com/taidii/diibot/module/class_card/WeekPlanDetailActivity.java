package com.taidii.diibot.module.class_card;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardWeekPlanBean;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.module.class_card.adapter.ClassCardWeekPlanAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardWeekPlanDetailAdapter;
import com.taidii.diibot.module.class_card.viewJzd.SwitchClassCardPopup;
import com.taidii.diibot.utils.ImageViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeekPlanDetailActivity extends BaseActivity implements SwitchClassCardPopup.SettingPopupListener {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.top_name)
    ImageView topName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.rv_student_data)
    RecyclerView rvStudentData;
    private int[] weeks = new int[]{R.string.txt_course_sunday
            , R.string.txt_course_monday
            , R.string.txt_course_tuesday
            , R.string.txt_course_wednesday
            , R.string.txt_course_thursday
            , R.string.txt_course_friday
            , R.string.txt_course_saturday};
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private SwitchClassCardPopup switchClassCardPopup;//设置弹窗

    private  ClassCardWeekPlanBean.DataBean dataBean;
    private ClassCardWeekPlanDetailAdapter classCardWeekPlanDetailAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_weekplan_detail;
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

        dataBean = getIntent().getParcelableExtra("dataBean");
        if(dataBean!=null){
            tvClassName.setText(String.format(getResources().getString(R.string.class_card_week),dataBean.getWeek())+
                    "("+dataBean.getDate_from().replace("-",".")+"-"+dataBean.getDate_to().replace("-",".")+")");
            tvTeacherName.setText(dataBean.getCreated_by());
            List<String> image_list = dataBean.getImage_list();
            showMedicineData(image_list);
        }
    }

    private void showMedicineData( List<String> image_list) {
        if (classCardWeekPlanDetailAdapter == null) {
            classCardWeekPlanDetailAdapter = new ClassCardWeekPlanDetailAdapter(image_list);

            rvStudentData.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

            classCardWeekPlanDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    ClassCardWeekPlanBean.DataBean dataBean = (ClassCardWeekPlanBean.DataBean) adapter.getItem(position);
//                    Intent intent = new Intent(ClassCardPageTwoActivity.this, WeekPlanDetailActivity.class);
//                    intent.putExtra("dataBean", dataBean);
//                    ClassCardPageTwoActivity.this.startActivity(intent);
                }
            });


            //解决数据加载不完的问题
            rvStudentData.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            rvStudentData.setFocusable(false);
            rvStudentData.setAdapter(classCardWeekPlanDetailAdapter);
        } else {
            classCardWeekPlanDetailAdapter.setNewData(image_list);
            classCardWeekPlanDetailAdapter.notifyDataSetChanged();
        }
        //显示无数据视图
        if ( image_list != null && image_list.isEmpty()) {
            classCardWeekPlanDetailAdapter.setEmptyView(getEmptyView());
            classCardWeekPlanDetailAdapter.notifyDataSetChanged();
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


    @OnClick({R.id.iv_setting, R.id.tv_class_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                if (switchClassCardPopup == null) {
                    switchClassCardPopup = new SwitchClassCardPopup(this, this);
                    switchClassCardPopup.showPopupWindow();
                } else {
                    switchClassCardPopup.showPopupWindow();
                }
                break;
            case R.id.tv_class_name:
                finish();
                break;
        }
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


    private View getEmptyView() {
        View view = LayoutInflater.from(act).inflate(R.layout.class_card_empty_resource, null);
        TextView name = view.findViewById(R.id.name);
        name.setVisibility(View.VISIBLE);
        return view;
    }

    private int classId = -1;

    @Override
    public void choiceClass(ClassCardClassListBean.DataBean ben) {
        tvClassName.setText(ben.getName());
        classId = ben.getId();
    }

    @Override
    public void switchAttendance() {

    }
}
