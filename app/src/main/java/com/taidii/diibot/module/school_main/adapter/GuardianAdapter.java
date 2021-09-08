package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.MateGuardianBean;
import com.taidii.diibot.utils.ImageViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuardianAdapter extends RecyclerView.Adapter<GuardianAdapter.ViewHolder> {

    private Context mContext = null;
    private List<MateGuardianBean> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public GuardianAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void itemClick(List<MateGuardianBean> guardianBeanList,int position);
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

    public void notifySetChanged(List<MateGuardianBean> mDataList){
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_guardian, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MateGuardianBean mateGuardianBean = mDataList.get(position);
        /*解决刷新ui闪烁*/
        ImageViewUtils.loadItemImage(mContext,mateGuardianBean.getAvatar(),holder.imHead,R.drawable.avatar_loading2);
        holder.tvName.setText(mateGuardianBean.getName());
        holder.tvRelation.setText(mateGuardianBean.getRelation());
        if (mateGuardianBean.isCheck()){
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_rund5_red_bg));
            holder.relBottom.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_bottom_layout_red_bg));
        }else {
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_rund5_gray_bg));
            holder.relBottom.setBackground(mContext.getResources().getDrawable(R.drawable.guardian_bottom_layout_gray_bg));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(mDataList,position);
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
        @BindView(R.id.tv_relation)
        TextView tvRelation;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.rel_bottom)
        RelativeLayout relBottom;
        @BindView(R.id.rel_item)
        RelativeLayout relItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
