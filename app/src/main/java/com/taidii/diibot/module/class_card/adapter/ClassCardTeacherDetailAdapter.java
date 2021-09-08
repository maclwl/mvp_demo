package com.taidii.diibot.module.class_card.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.module.class_card.ClassCardPhotoViewActivity;
import com.taidii.diibot.module.class_card.ClassCardVideoViewActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardTeacherDetailAdapter extends BaseQuickAdapter<ClassCardStudentListBean.DataBean.TeacherInfosBean, BaseViewHolder> {
    public ClassCardTeacherDetailAdapter(@Nullable List<ClassCardStudentListBean.DataBean.TeacherInfosBean> data) {
        super(R.layout.class_card_teacher_detail_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardStudentListBean.DataBean.TeacherInfosBean item) {

                RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.avatar_loading2)
                .bitmapTransform(new RoundedCorners(10))
                .error(R.drawable.avatar_loading2)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(item.getAvatar())
                .apply(options)
                .into((ImageView)helper.getView(R.id.im_head));
        helper.setText(R.id.tv_teacher_name , TextUtils.isEmpty(item.getGroup())?mContext.getResources().getString(R.string.class_card_nogroup):item.getGroup());
        helper.setText(R.id.tv_name , item.getName());
        helper.setText(R.id.tv_sname , String.format(mContext.getResources().getString(R.string.class_teacher_detail_phone),item.getPhone()));
        helper.setText(R.id.tv_mname , String.format(mContext.getResources().getString(R.string.class_teacher_detail_time),item.getEmployment_date()));


        ImageView imageView = helper.getView(R.id.im_sex);
        if (item.getGender()==1){
            imageView.setBackgroundResource(R.drawable.class_card_boy);
        }else {
            imageView.setBackgroundResource(R.drawable.class_card_girl);
        }
//        helper.addOnClickListener(R.id.img_select);


        RecyclerView recyclerView = helper.getView(R.id.rv_class_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        recyclerView.setNestedScrollingEnabled(false);

        List<String> results =item.getHonors();


        ClassCardHornorListAdapter   classCardStudentAdapter = new ClassCardHornorListAdapter(results);

        classCardStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        recyclerView.setAdapter(classCardStudentAdapter);
    }
}
