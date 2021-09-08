package com.taidii.diibot.module.class_card.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.ClassCardWeekPlanBean;

import java.util.List;


public class ClassCardWeekPlanAdapter extends BaseQuickAdapter<ClassCardWeekPlanBean.DataBean, BaseViewHolder> {
    public ClassCardWeekPlanAdapter(@Nullable List<ClassCardWeekPlanBean.DataBean> data) {
        super(R.layout.class_card_week_plan_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClassCardWeekPlanBean.DataBean item) {





//        ImageViewUtils.loadItemImage(mContmext, avatar, imHead, R.drawable.avatar_loading2);
        helper.setText(R.id.tv_sname , String.format(mContext.getResources().getString(R.string.class_card_week),item.getWeek()));
        helper.setText(R.id.tv_time , "("+item.getDate_from().replace("-",".")+"-"+item.getDate_to().replace("-",".")+")");
//

        ImageView imageView = helper.getView(R.id.im_head);
        ImageView iv_empty = helper.getView(R.id.iv_empty);
        List<String> image_list = item.getImage_list();
        if(image_list.isEmpty()){
            iv_empty.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }else{
            iv_empty.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                .centerCrop()
                .bitmapTransform(new RoundedCorners(8))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(image_list.get(0))
                .apply(options)
                .into(imageView);
        }
    }
}
