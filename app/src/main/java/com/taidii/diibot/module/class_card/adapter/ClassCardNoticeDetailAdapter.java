package com.taidii.diibot.module.class_card.adapter;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardMessagetBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.module.class_card.ClassCardPhotoViewActivity;
import com.taidii.diibot.module.class_card.ClassCardVideoViewActivity;
import com.taidii.diibot.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class ClassCardNoticeDetailAdapter extends BaseQuickAdapter<ClassCardMessagetBean.ResultsBean, BaseViewHolder> {
    public ClassCardNoticeDetailAdapter(@Nullable List<ClassCardMessagetBean.ResultsBean> data) {
        super(R.layout.class_card_class_notice_detail_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardMessagetBean.ResultsBean item) {
        helper.setText(R.id.tv_sname , item.getTitle());
//        helper.setText(R.id.tv_mname , String.format(mContext.getResources().getString(R.string.class_medicine_mname),"消炎药"));
        helper.setText(R.id.tv_mname , item.getText());
////        helper.setText(R.id.tv_size , PDFUtil.FormetFileSize(item.getFileSize()));
//        String date = DateUtil.transform(DateUtil.formatDate(DateUtil.getGMTDate(item
//                .getLast_update()), "yyyy-MM-dd"));
//        helper.setText(R.id.tv_time , date);


    }
}
