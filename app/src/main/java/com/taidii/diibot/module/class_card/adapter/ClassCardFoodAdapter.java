package com.taidii.diibot.module.class_card.adapter;

import android.text.Html;
import android.text.TextUtils;
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
import com.taidii.diibot.entity.class_card.Recipes;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardFoodAdapter extends BaseQuickAdapter<Recipes, BaseViewHolder> {
    public ClassCardFoodAdapter(@Nullable List<Recipes> data) {
        super(R.layout.class_card_food_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Recipes recipes) {
//        ImageViewUtils.loadItemImage(mContmext, avatar, imHead, R.drawable.avatar_loading2);
//        helper.setText(R.id.tv_sname , String.format(mContext.getResources().getString(R.string.class_medicine_sname),"大神"));
//        helper.setText(R.id.tv_mname , String.format(mContext.getResources().getString(R.string.class_medicine_mname),"消炎药"));
//        helper.setText(R.id.tv_remark , String.format(mContext.getResources().getString(R.string.class_medicine_remark),"小朋友需要饭后复用，一次服用一粒"));
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

        if (!TextUtils.isEmpty(recipes.getFood())) {
            helper.setText(R.id.tv_food , recipes.getFood());
        }

        int defaultImg = 0;

        if (recipes.getType() == 0) {
            defaultImg = R.drawable.ic_recipes_brelunch;
        } else if (recipes.getType() == 1) {
            defaultImg = R.drawable.ic_recipes_lunch;
        } else if (recipes.getType() == 2){
            defaultImg = R.drawable.ic_recipes_lunsnakes;
        }else if (recipes.getType() == 3){
            defaultImg = R.drawable.ic_recipes_breakfast;
        }else if (recipes.getType() == 4){
            defaultImg = R.drawable.ic_recipes_dinner;
        }

        if (TextUtils.isEmpty(recipes.getImage_thumbnail())){
            ((ImageView)helper.getView(R.id.im_head)).setImageResource(defaultImg);
        }else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(defaultImg)
//                    .bitmapTransform(new RoundedCorners(5))
                    .error(defaultImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .load(recipes.getImage_thumbnail())
                    .apply(options)
                    .into((ImageView)helper.getView(R.id.im_head));
        }
    }
}
