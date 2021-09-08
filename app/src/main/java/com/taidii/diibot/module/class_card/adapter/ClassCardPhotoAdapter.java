package com.taidii.diibot.module.class_card.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.Photo;
import com.taidii.diibot.utils.FileUtil;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;


public class ClassCardPhotoAdapter extends BaseQuickAdapter<Photo, BaseViewHolder> {
    public ClassCardPhotoAdapter(@Nullable List<Photo> data) {
        super(R.layout.class_card_photo_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Photo item) {
        ImageView videoPlay = helper.getView(R.id.img_video_play_one);
        ImageViewUtils.loadImage(mContext, item.getThumb(), (ImageView) helper.getView(R.id.photo_one), R.drawable.avatar_loading2);
        if (FileUtil.isVideo(item.getPhoto())) {
            videoPlay.setVisibility(View.VISIBLE);
        }else{
            videoPlay.setVisibility(View.GONE);
        }
    }
}
