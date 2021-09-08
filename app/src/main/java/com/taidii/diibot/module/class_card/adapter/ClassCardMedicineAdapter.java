package com.taidii.diibot.module.class_card.adapter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.NewMedicineListRsp;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardMedicineAdapter extends BaseQuickAdapter<NewMedicineListRsp.KlassListBean, BaseViewHolder> {
    public ClassCardMedicineAdapter(@Nullable List<NewMedicineListRsp.KlassListBean> data) {
        super(R.layout.class_card_medicine_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewMedicineListRsp.KlassListBean item) {
        ImageViewUtils.loadItemImage(mContext, item.getAvatar(), (CircleImageView)helper.getView(R.id.im_head), R.drawable.avatar_loading);
        helper.setText(R.id.tv_sname , String.format(mContext.getResources().getString(R.string.class_medicine_sname),item.getFullname()));
        helper.setText(R.id.tv_remark , String.format(mContext.getResources().getString(R.string.class_medicine_remark),item.getParent_note()));
////        helper.setText(R.id.tv_size , PDFUtil.FormetFileSize(item.getFileSize()));
//        helper.setText(R.id.tv_time , item.getAdd_time().substring(0,10));
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
        RecyclerView recyclerView = helper.getView(R.id.rv_class_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        recyclerView.setNestedScrollingEnabled(false);

        List<NewMedicineListRsp.KlassListBean.RequestsBean> results =item.getRequests();

        ClassCardMedicineListAdapter   classCardStudentAdapter = new ClassCardMedicineListAdapter(results);

        classCardStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        recyclerView.setAdapter(classCardStudentAdapter);
    }
}
