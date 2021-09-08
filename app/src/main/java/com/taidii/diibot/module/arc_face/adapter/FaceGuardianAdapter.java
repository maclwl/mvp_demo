package com.taidii.diibot.module.arc_face.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceGuardianAdapter extends RecyclerView.Adapter<FaceGuardianAdapter.ViewHolder> {

    private Context mContext = null;
    private List<MateGuardianBean> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public FaceGuardianAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void itemClick(int position,MateGuardianBean mateGuardianBean);
    }

    public void setDataList(List<MateGuardianBean> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<MateGuardianBean> dataList) {
        int lastIndex = mDataList.size();
        if (mDataList.addAll(dataList)) {
            notifyItemRangeInserted(lastIndex, dataList.size());
        }
    }

    public void notifySetChanged(List<MateGuardianBean> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_face_guardian, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MateGuardianBean mateGuardianBean = mDataList.get(position);
        holder.tvRelation.setText(mateGuardianBean.getRelation());
        ImageViewUtils.loadImage(mContext,mateGuardianBean.getFaceImage(),holder.imHead,R.drawable.avatar_loading);
        if (mateGuardianBean.isRegisterFace()){
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_rund5_green_bg));
            holder.relBottom.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_bottom_layout_green_bg));
            holder.tvRegisterTitle.setText(mContext.getResources().getString(R.string.click_reentry));
        }else {
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_rund5_red_bg));
            holder.relBottom.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_bottom_layout_red_bg));
            holder.tvRegisterTitle.setText(mContext.getResources().getString(R.string.click_entry));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(position,mateGuardianBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_head)
        RoundedImageView imHead;
        @BindView(R.id.tv_register_title)
        TextView tvRegisterTitle;
        @BindView(R.id.tv_relation)
        TextView tvRelation;
        @BindView(R.id.rel_bottom)
        RelativeLayout relBottom;
        @BindView(R.id.rel_item)
        LinearLayout relItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
