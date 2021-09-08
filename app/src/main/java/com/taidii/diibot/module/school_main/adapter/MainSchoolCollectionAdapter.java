package com.taidii.diibot.module.school_main.adapter;

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
import com.taidii.diibot.entity.school.MainSchoolCollectionModel;
import com.taidii.diibot.entity.school.MainSchoolContentModel;
import com.taidii.diibot.entity.school.SchoolMainEnum;
import com.taidii.diibot.utils.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainSchoolCollectionAdapter extends RecyclerView.Adapter<MainSchoolCollectionAdapter.ViewHolder> {

    private Context mContext = null;
    private List<MainSchoolCollectionModel> mDataList = new ArrayList();
    private ItemClickListener itemClickListener;

    public MainSchoolCollectionAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void clickChildContentItem(int collectionPosition, int contentPosition,SchoolMainEnum type,MainSchoolContentModel mainSchoolContentModel);
    }

    public void setDataList(List<MainSchoolCollectionModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifySetChanged(List<MainSchoolCollectionModel> mDataList){
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public List<MainSchoolCollectionModel> getDataList(){
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_school_collection, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainSchoolCollectionModel mainSchoolCollectionModel = mDataList.get(position);
        holder.tvModelName.setText(mainSchoolCollectionModel.getModelName());

        /*总人数*/
        int totalCount = mainSchoolCollectionModel.getMainSchoolContentModelList().size();
        /*签到人数和*/
        int signCount = 0;
        switch (mainSchoolCollectionModel.getType()){
            case CLASS:
            case BUSES:
                for (MainSchoolContentModel mainSchoolContentModel : mainSchoolCollectionModel.getMainSchoolContentModelList()){
                    if (mainSchoolContentModel.getStudentsBean().isSign()){
                        signCount = signCount +1;
                    }
                }
                break;
            case STAFF:
                for (MainSchoolContentModel mainSchoolContentModel : mainSchoolCollectionModel.getMainSchoolContentModelList()){
                    if (mainSchoolContentModel.getStaffsBean().isSign()){
                        signCount = signCount +1;
                    }
                }
                break;
        }
        holder.tvCount.setText(String.format(Constant.MAIN_SCHOOL_COUNT,String.valueOf(signCount),String.valueOf(totalCount)));
        /*是否展开学生列表(首先判断设置中是否设置全部展开)*/
        if (mainSchoolCollectionModel.isSpread()) {
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
                if (mainSchoolCollectionModel.isSpread()) {
                    mainSchoolCollectionModel.setSpread(false);
                    notifyDataSetChanged();
                } else {
                    mainSchoolCollectionModel.setSpread(true);
                    notifyDataSetChanged();
                }
            }
        });

        /*学生列表*/
        final MainSchoolContentAdapter mainSchoolContentAdapter = new MainSchoolContentAdapter(mainSchoolCollectionModel.getType());
        RecyclerView.LayoutManager layoutManagerStudent = new GridLayoutManager(mContext, 8, GridLayoutManager.VERTICAL, false);
        holder.studentList.setLayoutManager(layoutManagerStudent);
        holder.studentList.setAdapter(mainSchoolContentAdapter);
        if(SharePrefUtils.getBoolean(Constant.IS_HIDE_SIGN_IN)){
            mainSchoolContentAdapter.setDataList(mainSchoolCollectionModel.getMainSchoolContentModelListHide());
        }else {
            mainSchoolContentAdapter.setDataList(mainSchoolCollectionModel.getMainSchoolContentModelList());
        }
        /*学生点击监听*/
        mainSchoolContentAdapter.setItemClickListener(new MainSchoolContentAdapter.ItemClickListener() {
            @Override
            public void clickItem(int contentPosition,MainSchoolContentModel mainSchoolContentModel) {
                itemClickListener.clickChildContentItem(position,contentPosition,mainSchoolCollectionModel.getType(),mainSchoolContentModel);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
