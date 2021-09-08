package com.taidii.diibot.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.taidii.diibot.R;

public class ImageViewUtils {

    //ui渲染图片
    public static void loadImage(Context context, String imageUrl, ImageView imageView, int errorRes){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(errorRes)
                .placeholder(R.color.color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    //加载详情图
    public static void loadGoodDetailImage(Context context, String imageUrl, int width, int height, ImageView imageView, int errorRes){
        RequestOptions options = new RequestOptions()
                .override(width,height)
                .error(errorRes)
                .placeholder(R.color.color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    //加载列表item图片，防止刷新闪烁
    public static void loadItemImage(Context context, String imageUrl, ImageView imageView, int errorRes){
        Glide.with(context)
                .load(imageUrl)
                .placeholder(errorRes)
                .error(errorRes)
                .centerCrop()
                .addListener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setTag(imageUrl);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.NORMAL)
                .into(imageView);
    }

}
