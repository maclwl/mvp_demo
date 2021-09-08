package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.view.item.SchoolContentAvatar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainSchoolContentAdapter extends RecyclerView.Adapter<MainSchoolContentAdapter.ViewHolder> {

    private Context mContext = null;
    private SchoolMainEnum type;
    private List<MainSchoolContentModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public MainSchoolContentAdapter(SchoolMainEnum type) {
        this.type = type;
    }

    public interface ItemClickListener {

        void clickItem(int contentPosition, MainSchoolContentModel mainSchoolContentModel);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDataList(List<MainSchoolContentModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_school_content, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainSchoolContentModel mainSchoolContentModel = mDataList.get(position);
        switch (type) {
            case CLASS:
            case BUSES:
                holder.tvName.setText(mainSchoolContentModel.getStudentsBean().getName());
                holder.avatarLayout.setData(mContext,mainSchoolContentModel.getStudentsBean().getAvatar(),mainSchoolContentModel.getStudentsBean().isSign(),mainSchoolContentModel.getStudentsBean().isHealthCheck(),mainSchoolContentModel.getStudentsBean().getSingType());
                break;
            case STAFF:
                holder.tvName.setText(mainSchoolContentModel.getStaffsBean().getName());
                holder.avatarLayout.setData(mContext,mainSchoolContentModel.getStaffsBean().getAvatar(),mainSchoolContentModel.getStaffsBean().isSign(),false,mainSchoolContentModel.getStaffsBean().getSignType());
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.clickItem(position, mainSchoolContentModel);
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
        @BindView(R.id.avatar_layout)
        SchoolContentAvatar avatarLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
