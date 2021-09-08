package com.taidii.diibot.module.organ_main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.entity.organ.ClassOrderBean;
import com.taidii.diibot.entity.organ.MainOrganCollectionModel;
import com.taidii.diibot.entity.organ.StudentsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainOrganCollectionAdapter extends RecyclerView.Adapter<MainOrganCollectionAdapter.ViewHolder> {

    private Context mContext = null;
    private List<MainOrganCollectionModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public MainOrganCollectionAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void clickChildContentItem(StudentsBean studentsBean,ClassOrderBean classOrder);
    }

    public void setDataList(List<MainOrganCollectionModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifySetChanged(List<MainOrganCollectionModel> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public List<MainOrganCollectionModel> getDataList() {
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_organ_collection, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainOrganCollectionModel mainOrganCollectionModel = mDataList.get(position);
        final ClassOrderBean classOrder = mainOrganCollectionModel.getClassOrder();
        holder.tvModelName.setText(classOrder.getClassname());
        holder.tvClassRoom.setText(String.format(Constant.CLASS_ROOM, classOrder.getRoomname()));
        /*总人数*/
        int totalCount = mainOrganCollectionModel.getStudentsList().size();
        /*签到人数和*/
        int signCount = 0;
        for (StudentsBean studentsBean : mainOrganCollectionModel.getStudentsList()){
            if (studentsBean.isSign()){
                signCount = signCount +1;
            }
        }
        holder.tvCount.setText(String.format(Constant.MAIN_SCHOOL_COUNT,String.valueOf(signCount),String.valueOf(totalCount)));
        if (classOrder.getTime_list().size()>0){
            holder.tvTime.setText(classOrder.getTime_list().get(0));
        }
        /*判断是否展开*/
        if (mainOrganCollectionModel.isSpread()) {
            holder.relModel.setBackgroundColor(mContext.getResources().getColor(R.color.main_collection_bg));
            holder.imDown.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_class_arrow_down));
            holder.relStudent.setVisibility(View.VISIBLE);
        } else {
            holder.relStudent.setVisibility(View.GONE);
            holder.imDown.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_class_arrow));
            holder.relModel.setBackgroundColor(mContext.getResources().getColor(R.color.themeWhite));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainOrganCollectionModel.isSpread()) {
                    mainOrganCollectionModel.setSpread(false);
                    notifyDataSetChanged();
                } else {
                    mainOrganCollectionModel.setSpread(true);
                    notifyDataSetChanged();
                }
            }
        });
        /*学生列表*/
        final MainOrganContentAdapter mainSchoolContentAdapter = new MainOrganContentAdapter();
        RecyclerView.LayoutManager layoutManagerStudent = new GridLayoutManager(mContext, 8, GridLayoutManager.VERTICAL, false);
        holder.studentList.setLayoutManager(layoutManagerStudent);
        holder.studentList.setAdapter(mainSchoolContentAdapter);
        mainSchoolContentAdapter.setDataList(mainOrganCollectionModel.getStudentsList());
        /*item监听*/
        mainSchoolContentAdapter.setItemClickListener(new MainOrganContentAdapter.ItemClickListener() {
            @Override
            public void clickItem(StudentsBean studentsBean) {
                itemClickListener.clickChildContentItem(studentsBean,classOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_down)
        ImageView imDown;
        @BindView(R.id.tv_model_name)
        TextView tvModelName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.student_list)
        RecyclerView studentList;
        @BindView(R.id.rel_student)
        RelativeLayout relStudent;
        @BindView(R.id.rel_model)
        RelativeLayout relModel;
        @BindView(R.id.tv_class_room)
        TextView tvClassRoom;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
