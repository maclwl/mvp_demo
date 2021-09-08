package com.taidii.diibot.module.class_card.adapter;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.class_card.CameryPhoto;
import com.taidii.diibot.entity.class_card.ClassCardStudentBean;
import com.taidii.diibot.entity.class_card.Photo;
import com.taidii.diibot.module.class_card.ClassCardPhotoViewActivity;
import com.taidii.diibot.module.class_card.ClassCardVideoViewActivity;
import com.taidii.diibot.utils.FileUtil;
import com.taidii.diibot.utils.ImageViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassCardClassActivityAdapter extends BaseQuickAdapter<CameryPhoto.PhotosBean, BaseViewHolder> {
    public ClassCardClassActivityAdapter(@Nullable List<CameryPhoto.PhotosBean> data) {
        super(R.layout.class_card_class_activity_item,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CameryPhoto.PhotosBean item) {
       ImageViewUtils.loadItemImage(mContext, item.getAvatar(), (CircleImageView)helper.getView(R.id.im_head), R.drawable.avatar_loading);
        helper.setText(R.id.tv_sname , item.getOwner_name());

        helper.setText(R.id.tv_remark , parsePicTime(item.getPublish_at()).replace("T", " ").replace("Z", " ").substring(0,16));
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

        RecyclerView recyclerView = helper.getView(R.id.rv_photo_data);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setNestedScrollingEnabled(false);

        List<Photo> results = item.getUploaded_photos();
        if (results != null && results.size() > 0) {
            Photo photo = results.get(0);
            helper.setText(R.id.tv_mname ,photo.getComments());
        }

        ClassCardPhotoAdapter   classCardStudentAdapter = new ClassCardPhotoAdapter(results);

        classCardStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Photo photo = (Photo) adapter.getItem(position);
                if(FileUtil.isVideo(photo.getPhoto())){
                    Intent intent = new Intent(mContext, ClassCardVideoViewActivity.class);
                    intent.putExtra("videoUrl", photo.getPhoto());
                    intent.putExtra("imageUrl", photo.getThumb());
                    mContext.startActivity(intent);
                }else{
                    ArrayList<String> photoList = new ArrayList<>();
                    for(Photo p:results){
                        photoList.add(p.getPhoto());
                    }
                    Intent intent = new Intent(mContext, ClassCardPhotoViewActivity.class);
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("photos", photoList);
                    mContext.startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(classCardStudentAdapter);
    }

    private String parsePicTime(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(str);

            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            ca.add(Calendar.HOUR_OF_DAY, 8);
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(ca.getTime());

        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return null;
    }
}
