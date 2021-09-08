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
import com.taidii.diibot.entity.class_card.ClassCardWeekPlanBean;

import java.util.List;


public class ClassCardWeekPlanDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ClassCardWeekPlanDetailAdapter(@Nullable List<String> data) {
        super(R.layout.class_card_weekly_image_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv_weekly_update_img = helper.getView(R.id.iv_weekly_update_img);
            RequestOptions options = new RequestOptions()
                .fitCenter().dontAnimate()
//                .bitmapTransform(new RoundedCorners(8))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(item)
                .apply(options)
                .into(iv_weekly_update_img);
    }
}
