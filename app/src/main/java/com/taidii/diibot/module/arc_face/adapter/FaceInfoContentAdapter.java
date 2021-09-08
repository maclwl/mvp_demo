package com.taidii.diibot.module.arc_face.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.entity.school.StaffsBean;
import com.taidii.diibot.entity.school.StudentsBean;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FaceInfoContentAdapter extends RecyclerView.Adapter<FaceInfoContentAdapter.ViewHolder> {

    private Context mContext = null;
    private SchoolMainEnum type;
    private List<MainSchoolContentModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public FaceInfoContentAdapter(SchoolMainEnum type) {
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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_face_info_content, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainSchoolContentModel mainSchoolContentModel = mDataList.get(position);
        switch (type) {
            case CLASS:
            case BUSES:
                final StudentsBean student = mainSchoolContentModel.getStudentsBean();
                holder.tvName.setText(student.getName());
                ImageViewUtils.loadItemImage(mContext, student.getFaceImage(), holder.imHead, R.drawable.avatar_loading2);
                if (student.isRegisterFace()){
                    holder.llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_sign_in_bg));
                }else {
                    holder.llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_red_bg));
                }
                break;
            case STAFF:
                final StaffsBean staff = mainSchoolContentModel.getStaffsBean();
                holder.tvName.setText(staff.getName());
                ImageViewUtils.loadItemImage(mContext, staff.getFaceImage(), holder.imHead, R.drawable.avatar_loading2);
                if (staff.isRegisterFace()){
                    holder.llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_sign_in_bg));
                }else {
                    holder.llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_red_bg));
                }
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

        @BindView(R.id.im_head)
        CircleImageView imHead;
        @BindView(R.id.ll_bg)
        LinearLayout llBg;
        @BindView(R.id.im_register_face)
        ImageView imRegisterFace;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

