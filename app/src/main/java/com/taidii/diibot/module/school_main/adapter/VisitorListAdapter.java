package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.school.VisitorsBean;
import com.taidii.diibot.utils.DateUtil;
import com.taidii.diibot.utils.DateUtils;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitorListAdapter extends RecyclerView.Adapter<VisitorListAdapter.ViewHolder> {

    private Context mContext = null;
    private List<VisitorsBean> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public VisitorListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void clickItem(VisitorsBean visitorsBean);
    }

    public void setDataList(List<VisitorsBean> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<VisitorsBean> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<VisitorsBean> getDataList() {
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_visitor_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VisitorsBean visitorsBean = mDataList.get(position);
        ImageViewUtils.loadItemImage(mContext, visitorsBean.getAvatar(), holder.imHead, R.drawable.noicon);
        holder.tvName.setText(visitorsBean.getName());
        if (!TextUtils.isEmpty(visitorsBean.getSign_in())&&!TextUtils.isEmpty(visitorsBean.getSign_out())){
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.visitor_item_gray_bg));
            String inDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorsBean.getSign_in()), Constant.DATE_TIME_RULE));
            String outDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorsBean.getSign_out()), Constant.DATE_TIME_RULE));
            holder.tvSinInTime.setText(DateUtil.formatNewStr(inDatetime, Constant.SHORT_TIME));
            holder.tvSinOutTime.setText(DateUtil.formatNewStr(outDatetime, Constant.SHORT_TIME));
        }else {
            holder.relItem.setBackground(mContext.getResources().getDrawable(R.drawable.visitor_item_green_bg));
            if (!TextUtils.isEmpty(visitorsBean.getSign_in())){
                String inDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorsBean.getSign_in()), Constant.DATE_TIME_RULE));
                holder.tvSinInTime.setText(DateUtil.formatNewStr(inDatetime, Constant.SHORT_TIME));
                holder.tvSinOutTime.setText(mContext.getResources().getString(R.string.visit_time_default));
            }else if (!TextUtils.isEmpty(visitorsBean.getSign_out())){
                String outDatetime = DateUtils.transform(DateUtils.formatDate(DateUtils.getGMTDate(visitorsBean.getSign_out()), Constant.DATE_TIME_RULE));
                holder.tvSinInTime.setText(mContext.getResources().getString(R.string.visit_time_default));
                holder.tvSinOutTime.setText(DateUtil.formatNewStr(outDatetime, Constant.SHORT_TIME));
            }else {
                holder.tvSinInTime.setText(mContext.getResources().getString(R.string.visit_time_default));
                holder.tvSinOutTime.setText(mContext.getResources().getString(R.string.visit_time_default));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.clickItem(visitorsBean);
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_sin_in_time)
        TextView tvSinInTime;
        @BindView(R.id.tv_sin_out_time)
        TextView tvSinOutTime;
        @BindView(R.id.rel_item)
        RelativeLayout relItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
