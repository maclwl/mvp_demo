package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.HealthContentModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthChildAdapter extends RecyclerView.Adapter<HealthChildAdapter.ViewHolder> {

    private String key;//病症列表属于的部位
    private Context mContext = null;
    private List<HealthContentModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public HealthChildAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void clickContentItem(String key);
    }

    public void setDataList(List<HealthContentModel> dataList,String key) {
        this.key = key;
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public List<HealthContentModel> getDataList(){
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_health_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HealthContentModel model = mDataList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvName.setSelected(model.isCheck());
        if (model.isCheck()){
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.themeWhite));
        }else {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_content_color));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isCheck()){
                    model.setCheck(false);
                }else {
                    model.setCheck(true);
                }
                notifyDataSetChanged();
                itemClickListener.clickContentItem(key);
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
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
