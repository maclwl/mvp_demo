package com.taidii.diibot.module.organ_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.organ.StudentsBean;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainOrganContentAdapter extends RecyclerView.Adapter<MainOrganContentAdapter.ViewHolder> {

    private Context mContext = null;
    private List<StudentsBean> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public interface ItemClickListener {

        void clickItem(StudentsBean studentsBean);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDataList(List<StudentsBean> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_organ_content, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final StudentsBean studentsBean = mDataList.get(position);
        holder.tvName.setText(studentsBean.getName());
        ImageViewUtils.loadItemImage(mContext, studentsBean.getAvatar(), holder.imHead, R.drawable.avatar_loading);
        if (studentsBean.isSign()){
            holder.imSign.setVisibility(View.VISIBLE);
        }else {
            holder.imSign.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.clickItem(studentsBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_head)
        CircleImageView imHead;
        @BindView(R.id.im_sign)
        ImageView imSign;
        @BindView(R.id.tv_name)
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

