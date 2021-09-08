package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.QrCodeStudentCollection;
import com.taidii.diibot.utils.ImageViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentSignSelectAdapter extends RecyclerView.Adapter<StudentSignSelectAdapter.ViewHolder> {

    private Context mContext = null;
    private List<QrCodeStudentCollection> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public StudentSignSelectAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void clickItem(int position, QrCodeStudentCollection signHistoryDataModel);
    }

    public void setDataList(List<QrCodeStudentCollection> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<QrCodeStudentCollection> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<QrCodeStudentCollection> getDataList() {
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_student_sign_select, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainSchoolContentModel contentModel = mDataList.get(position).getContentModel();
        holder.tvName.setText(contentModel.getStudentsBean().getName());
        ImageViewUtils.loadItemImage(mContext, contentModel.getStudentsBean().getAvatar(), holder.imHead, R.drawable.avatar_loading2);
        /*是否已签到*/
        if (contentModel.getStudentsBean().isSign()){
            holder.tvSign.setVisibility(View.VISIBLE);
            /*判断签到*/
            switch (contentModel.getStudentsBean().getSingType()){
                case SIGN_IN:
                    holder.tvSign.setText(mContext.getResources().getString(R.string.sign_in));
                    holder.tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.student_sign_in_tv_bg));
                    holder.relHead.setBackground(mContext.getResources().getDrawable(R.drawable.student_select_head_sign_in));
                    break;
                case SIGN_TEMP:
                    holder.tvSign.setText(mContext.getResources().getString(R.string.sign_out));
                    holder.tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.student_sign_out_tv_bg));
                    holder.relHead.setBackground(mContext.getResources().getDrawable(R.drawable.student_select_head_sign_temp));
                    break;
                case SIGN_OUT:
                    holder.tvSign.setText(mContext.getResources().getString(R.string.sign_out));
                    holder.tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.student_sign_out_tv_bg));
                    holder.relHead.setBackground(mContext.getResources().getDrawable(R.drawable.student_select_head_sign_out));
                    break;
            }
        }else {
            holder.tvSign.setVisibility(View.GONE);
            holder.relHead.setBackground(mContext.getResources().getDrawable(R.drawable.student_select_head_normal));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.clickItem(position,mDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.im_head)
        RoundedImageView imHead;
        @BindView(R.id.tv_sign)
        TextView tvSign;
        @BindView(R.id.rel_head)
        RelativeLayout relHead;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
