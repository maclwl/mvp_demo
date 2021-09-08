package com.taidii.diibot.module.class_card.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardClassListBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;

import java.util.List;


public class ClassCardChoiceClassAdapter extends BaseQuickAdapter<ClassCardClassListBean.DataBean, BaseViewHolder> {
    public ClassCardChoiceClassAdapter(@Nullable List<ClassCardClassListBean.DataBean> data) {
        super(R.layout.class_card_choice_class_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardClassListBean.DataBean item) {
//        ImageViewUtils.loadItemImage(mContmext, avatar, imHead, R.drawable.avatar_loading2);
//        helper.setText(R.id.tv_sname , String.format(mContext.getResources().getString(R.string.class_medicine_sname),"大神"));
//        helper.setText(R.id.tv_mname , String.format(mContext.getResources().getString(R.string.class_medicine_mname),"消炎药"));
//        helper.setText(R.id.tv_remark , String.format(mContext.getResources().getString(R.string.class_medicine_remark),"小朋友需要饭后复用，一次服用一粒"));
////        helper.setText(R.id.tv_size , PDFUtil.FormetFileSize(item.getFileSize()));
        helper.setText(R.id.class_name , item.getName());
//
//        if (item.isRead_ornot()){
//            helper.getView(R.id.tv_new).setVisibility(View.GONE);
//        }else {
//            helper.getView(R.id.tv_new).setVisibility(View.VISIBLE);
//        }

//        ImageView imageView = helper.getView(R.id.img_select);
//        if (item.isSelect()){
//            imageView.setBackgroundResource(R.drawable.reward_selection_ok);
//        }else {
//            imageView.setBackgroundResource(R.drawable.reward_selection_no);
//        }
//        helper.addOnClickListener(R.id.img_select);
    }
}
