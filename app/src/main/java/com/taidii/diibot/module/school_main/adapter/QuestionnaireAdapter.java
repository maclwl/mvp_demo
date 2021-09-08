package com.taidii.diibot.module.school_main.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.QuestionnaireModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {

    private Context mContext = null;
    private List<QuestionnaireModel> mDataList = new ArrayList();

    public QuestionnaireAdapter() {

    }

    public void setDataList(List<QuestionnaireModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<QuestionnaireModel> dataList) {
        int lastIndex = mDataList.size();
        if (mDataList.addAll(dataList)) {
            notifyItemRangeInserted(lastIndex, dataList.size());
        }
    }

    public void notifySetChanged(List<QuestionnaireModel> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quesyionnaire, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final QuestionnaireModel model = mDataList.get(position);
        holder.tvName.setText(model.getName());
        if (model.isCheck()) {
            holder.imYes.setSelected(true);
            holder.imNo.setSelected(false);
            /*判断如果是unwell展开输入*/
            if (model.getKey().equals("unwell")){
                holder.editContent.setVisibility(View.VISIBLE);
                holder.editContent.setText(model.getContent());
            }else {
                holder.editContent.setVisibility(View.GONE);
            }
        } else {
            holder.imYes.setSelected(false);
            holder.imNo.setSelected(true);
            holder.editContent.setVisibility(View.GONE);
        }
        holder.imYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCheck(true);
                notifyDataSetChanged();
            }
        });
        holder.imNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCheck(false);
                notifyDataSetChanged();
            }
        });
        holder.editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    model.setContent(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        @BindView(R.id.im_yes)
        ImageView imYes;
        @BindView(R.id.im_no)
        ImageView imNo;
        @BindView(R.id.edit_content)
        EditText editContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}