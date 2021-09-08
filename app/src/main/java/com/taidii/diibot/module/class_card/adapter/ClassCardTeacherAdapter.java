package com.taidii.diibot.module.class_card.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardTeacherAdapter extends BaseQuickAdapter<ClassCardStudentListBean.DataBean.TeacherInfosBean, BaseViewHolder> {
    public ClassCardTeacherAdapter(@Nullable List<ClassCardStudentListBean.DataBean.TeacherInfosBean> data) {
        super(R.layout.class_card_teacher_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardStudentListBean.DataBean.TeacherInfosBean item) {
        ImageViewUtils.loadItemImage(mContext, item.getAvatar(), (CircleImageView)helper.getView(R.id.im_head), R.drawable.avatar_loading);
        helper.setText(R.id.tv_name , item.getName());
    }
}
