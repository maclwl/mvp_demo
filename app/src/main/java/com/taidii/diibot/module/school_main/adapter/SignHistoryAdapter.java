package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.school.SignHistoryDataModel;
import com.taidii.diibot.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignHistoryAdapter extends RecyclerView.Adapter<SignHistoryAdapter.ViewHolder> {

    private Context mContext = null;
    private List<SignHistoryDataModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public SignHistoryAdapter() {
    }

    public interface ItemClickListener {

        void clickItem(int position, SignHistoryDataModel signHistoryDataModel);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDataList(List<SignHistoryDataModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<SignHistoryDataModel> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<SignHistoryDataModel> getDataList() {
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sign_history, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SignHistoryDataModel historyDataModel = mDataList.get(position);
        /*背景色*/
        if (position % 2 != 0) {
            holder.llItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen15));
        } else {
            holder.llItem.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }
        switch (historyDataModel.getAttendances().getType()) {
            case 0:
                holder.tvType.setText(mContext.getResources().getString(R.string.sign_in));
                break;
            case 1:
                holder.tvType.setText(mContext.getResources().getString(R.string.sign_out));
                break;
            case 4:
                holder.tvType.setText(mContext.getResources().getString(R.string.sign_temp));
                break;
            case 2:
            case 3:
                holder.tvType.setText(mContext.getResources().getString(R.string.health_check));
                break;
        }
        holder.tvData.setText(DateUtil.formatNewStr(historyDataModel.getAttendances().getDateTime(), Constant.LONG_DATE));
        holder.tvTime.setText(DateUtil.formatNewStr(historyDataModel.getAttendances().getDateTime(), Constant.SHORT_TIME));
        holder.tvTemp.setText(String.valueOf(historyDataModel.getAttendances().getTemperature()));
        if (historyDataModel.getAttendances().getAuthorizedPickUpId() != 0 && historyDataModel.getGuardians() != null) {
            holder.tvGuardian.setText(historyDataModel.getGuardians().getName());
        } else {
            holder.tvGuardian.setText("");
        }
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.clickItem(position, historyDataModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_temp)
        TextView tvTemp;
        @BindView(R.id.tv_guardian)
        TextView tvGuardian;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}