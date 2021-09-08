package com.taidii.diibot.module.class_card.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardMessagetBean;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.utils.DateUtil;

import java.util.List;


public class ClassCardNoticeAdapter extends BaseQuickAdapter<ClassCardMessagetBean.ResultsBean, BaseViewHolder> {
    public ClassCardNoticeAdapter(@Nullable List<ClassCardMessagetBean.ResultsBean> data) {
        super(R.layout.class_card_notice_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardMessagetBean.ResultsBean item) {
        helper.setText(R.id.tv_sname , item.getTitle());
//        helper.setText(R.id.tv_mname , String.format(mContext.getResources().getString(R.string.class_medicine_mname),"消炎药"));
        helper.setText(R.id.tv_remark , item.getText());
////        helper.setText(R.id.tv_size , PDFUtil.FormetFileSize(item.getFileSize()));
        String date = DateUtil.transform(DateUtil.formatDate(DateUtil.getGMTDate(item
                .getLast_update()), "yyyy-MM-dd"));
        helper.setText(R.id.tv_time , date.substring(0,10));

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
