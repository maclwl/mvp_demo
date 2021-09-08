package com.taidii.diibot.module.class_card.viewJzd;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.suke.widget.SwitchButton;
import com.taidii.diibot.R;
import com.taidii.diibot.app.ApiContainer;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.app.GlobalParams;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.module.class_card.adapter.ClassCardChoiceClassAdapter;
import com.taidii.diibot.module.class_card.adapter.ClassCardMedicineAdapter;
import com.taidii.diibot.module.school_main.SchoolMainActivity;
import com.taidii.diibot.net.OKHttpUtils;
import com.taidii.diibot.utils.SharePrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class SwitchClassCardPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView class_name;

    private RecyclerView recyclerView;

    private SettingPopupListener settingPopupListener;

    private Context context;

    private ClassCardChoiceClassAdapter classCardMedicineAdapter;
    private List<ClassCardStudentBean.ResultsBean> resultsMedicine = new ArrayList<>();
    public SwitchClassCardPopup(Context context, SettingPopupListener settingPopupListener) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.CENTER);
        this.settingPopupListener = settingPopupListener;
        initEvent();
    }

    public interface SettingPopupListener{

        void choiceClass(ClassCardClassListBean.DataBean ben);
        void switchAttendance();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_switch_class_card);
    }

    private void initEvent(){

        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.class_name).setOnClickListener(this);
        findViewById(R.id.rel_switch_attenance).setOnClickListener(this);
        class_name = findViewById(R.id.class_name);
        if(GlobalParams.mClassList!=null&&!GlobalParams.mClassList.isEmpty()){
            class_name.setText(GlobalParams.mClassList.get(0).getName());
        }

        recyclerView = findViewById(R.id.rv_class_data);

        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        resultsMedicine.add(new ClassCardStudentBean.ResultsBean());
        if (classCardMedicineAdapter == null) {
            classCardMedicineAdapter = new ClassCardChoiceClassAdapter(GlobalParams.mClassList);

            recyclerView.setLayoutManager(new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });

            classCardMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ClassCardClassListBean.DataBean dataBean = (ClassCardClassListBean.DataBean) adapter.getItem(position);
                    if(dataBean!=null&&settingPopupListener!=null){
                        settingPopupListener.choiceClass(dataBean);
                        class_name.setText(dataBean.getName());
                        dismiss();
                    }
                }
            });


            //解决数据加载不完的问题
            recyclerView.setNestedScrollingEnabled(false);
            //解决数据加载完成后, 没有停留在顶部的问题
            recyclerView.setFocusable(false);
            recyclerView.setAdapter(classCardMedicineAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.class_name:
                if(recyclerView.getVisibility()==View.GONE){
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            case R.id.rel_switch_attenance:
                Intent intent = new Intent(context, SchoolMainActivity.class);
                context.startActivity(intent);
//                settingPopupListener.switchAttendance();
                break;
        }
    }

}
