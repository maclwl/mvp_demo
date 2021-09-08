package com.taidii.diibot.module.class_card.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentListBean;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardStudentAdapter extends BaseQuickAdapter<ClassCardStudentListBean.DataBean.StudentInfosBean, BaseViewHolder> {
    public ClassCardStudentAdapter(@Nullable List<ClassCardStudentListBean.DataBean.StudentInfosBean> data) {
        super(R.layout.class_card_student_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardStudentListBean.DataBean.StudentInfosBean item) {
        ImageViewUtils.loadItemImage(mContext, item.getAvatar(), (CircleImageView)helper.getView(R.id.im_head), R.drawable.avatar_loading2);
        helper.setText(R.id.tv_name , item.getName());

        TextView tv_sign = (TextView) helper.getView(R.id.tv_sign);
        LinearLayout ll_bg = (LinearLayout) helper.getView(R.id.ll_bg);
//# 已签到
//            SIGN_IN_STATUS = 1
//# 已签退
//            SIGN_OUT_STATUS = 2
//# 病假
//            SICK_LEAVE_STATUS = 3
//# 事假
//            PERSONAL_LEAVE_STATUS = 4
        if(item.getStatus()==0){
            GradientDrawable background01 = (GradientDrawable) tv_sign.getBackground();
            background01.setColor(Color.parseColor("#DFF0D5"));
            ((GradientDrawable) ll_bg.getBackground()).setStroke((int) mContext.getResources().getDimension(R.dimen.dp_1_5),Color.parseColor("#DFF0D5"));
            tv_sign.setText(mContext.getResources().getString(R.string.class_student_head06));
        }else if(item.getStatus()==1){
            GradientDrawable background01 = (GradientDrawable) tv_sign.getBackground();
            background01.setColor(Color.parseColor("#52C41A"));
            ((GradientDrawable) ll_bg.getBackground()).setStroke((int) mContext.getResources().getDimension(R.dimen.dp_1_5),Color.parseColor("#52C41A"));
            tv_sign.setText(mContext.getResources().getString(R.string.class_student_head01));
        }else if(item.getStatus()==2){
            GradientDrawable background01 = (GradientDrawable) tv_sign.getBackground();
            background01.setColor(Color.parseColor("#6B90FF"));
            ((GradientDrawable) ll_bg.getBackground()).setStroke((int) mContext.getResources().getDimension(R.dimen.dp_1_5),Color.parseColor("#6B90FF"));
            tv_sign.setText(mContext.getResources().getString(R.string.class_student_head02));
        }else if(item.getStatus()==3){
            GradientDrawable background01 = (GradientDrawable) tv_sign.getBackground();
            background01.setColor(Color.parseColor("#FA8C16"));
            ((GradientDrawable) ll_bg.getBackground()).setStroke((int) mContext.getResources().getDimension(R.dimen.dp_1_5),Color.parseColor("#FA8C16"));
            tv_sign.setText(mContext.getResources().getString(R.string.class_student_head04));
        }else if(item.getStatus()==4){
            GradientDrawable background01 = (GradientDrawable) tv_sign.getBackground();
            background01.setColor(Color.parseColor("#A972FF"));
            ((GradientDrawable) ll_bg.getBackground()).setStroke((int) mContext.getResources().getDimension(R.dimen.dp_1_5),Color.parseColor("#A972FF"));
            tv_sign.setText(mContext.getResources().getString(R.string.class_student_head03));
        }

    }
}
