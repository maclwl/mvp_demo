package com.taidii.diibot.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.enums.SingTypeEnum;
import com.taidii.diibot.utils.ImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SchoolContentAvatar extends RelativeLayout {

    @BindView(R.id.im_head)
    CircleImageView imHead;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.im_health_tip)
    ImageView imHealthTip;

    public SchoolContentAvatar(Context context) {
        super(context);
        initView(context, null);
    }

    public SchoolContentAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SchoolContentAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_school_content_avatar, this);
        ButterKnife.bind(this);
    }

    public void setData(Context mContext, String avatar, boolean isSign, boolean isHealthCheck,SingTypeEnum signType) {
        ImageViewUtils.loadItemImage(mContext, avatar, imHead, R.drawable.avatar_loading2);
        if (isHealthCheck){
            imHealthTip.setVisibility(View.VISIBLE);
        }else {
            imHealthTip.setVisibility(View.GONE);
        }
        if (isSign) {
            tvSign.setVisibility(View.VISIBLE);
            if (signType != null) {
                switch (signType) {
                    case SIGN_IN:
                        tvSign.setText(mContext.getResources().getString(R.string.sign_in));
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.tv_sign_rund5_green_bg));
                        llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_sign_in_bg));
                        break;
                    case SIGN_TEMP:
                        tvSign.setText(mContext.getResources().getString(R.string.sign_temp));
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.tv_sign_rund5_menu_bg));
                        llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_sign_temp_bg));
                        break;
                    case SIGN_OUT:
                        tvSign.setText(mContext.getResources().getString(R.string.sign_out));
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.tv_sign_rund5_blue_bg));
                        llBg.setBackground(mContext.getResources().getDrawable(R.drawable.school_content_sign_out_bg));
                        break;
                }
            }
        } else {
            tvSign.setVisibility(View.GONE);
        }
    }

}
