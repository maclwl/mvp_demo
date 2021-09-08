package com.taidii.diibot.view.health;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.HealthRegionModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthRegionLayout extends RelativeLayout {

    @BindView(R.id.tv_eye)
    TextView tvEye;
    @BindView(R.id.tv_mouth)
    TextView tvMouth;
    @BindView(R.id.tv_hand)
    TextView tvHand;
    @BindView(R.id.tv_buttock)
    TextView tvButtock;
    @BindView(R.id.tv_face)
    TextView tvFace;
    @BindView(R.id.tv_neck)
    TextView tvNeck;
    @BindView(R.id.tv_chest)
    TextView tvChest;
    @BindView(R.id.tv_foot)
    TextView tvFoot;
    @BindView(R.id.tv_general)
    TextView tvGeneral;
    @BindView(R.id.im_person)
    ImageView imPerson;

    private HealthRegionListener healthRegionListener;

    public interface HealthRegionListener {

        void clickRegionButton(String key);
    }

    public void setHealthRegionListener(HealthRegionListener healthRegionListener) {
        this.healthRegionListener = healthRegionListener;
    }

    public HealthRegionLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public HealthRegionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HealthRegionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.health_parts_layout, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_eye, R.id.tv_mouth, R.id.tv_hand, R.id.tv_buttock, R.id.tv_face, R.id.tv_neck, R.id.tv_chest, R.id.tv_foot, R.id.tv_general})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_eye:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.eye));
                break;
            case R.id.tv_mouth:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.mouth));
                break;
            case R.id.tv_hand:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.hand));
                break;
            case R.id.tv_buttock:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.buttocks));
                break;
            case R.id.tv_face:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.face));
                break;
            case R.id.tv_neck:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.neck));
                break;
            case R.id.tv_chest:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.chest));
                break;
            case R.id.tv_foot:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.foot));
                break;
            case R.id.tv_general:
                healthRegionListener.clickRegionButton(getResources().getString(R.string.disease));
                break;
        }
    }

    public void setGender(int gender) {
        switch (gender) {
            case 0:
                imPerson.setImageDrawable(getResources().getDrawable(R.drawable.icon_health_check_boy));
                break;
            case 1:
                imPerson.setImageDrawable(getResources().getDrawable(R.drawable.icon_health_check_girl));
                break;
        }
    }

    /*刷新UI*/
    public void regionSetChange(HealthRegionModel eyeRegion, HealthRegionModel mouthRegion, HealthRegionModel handRegion, HealthRegionModel buttockRegion, HealthRegionModel generalRegion, HealthRegionModel faceRegion, HealthRegionModel neckRegion, HealthRegionModel chestRegion, HealthRegionModel footRegion) {
        /*眼button*/
        if (eyeRegion.isClick()) {
            tvEye.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (eyeRegion.isDisease()) {
                tvEye.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvEye.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*口button*/
        if (mouthRegion.isClick()) {
            tvMouth.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (mouthRegion.isDisease()) {
                tvMouth.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvMouth.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*手button*/
        if (handRegion.isClick()) {
            tvHand.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (handRegion.isDisease()) {
                tvHand.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvHand.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*臀button*/
        if (buttockRegion.isClick()) {
            tvButtock.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (buttockRegion.isDisease()) {
                tvButtock.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvButtock.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*面button*/
        if (faceRegion.isClick()) {
            tvFace.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (faceRegion.isDisease()) {
                tvFace.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvFace.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*颈button*/
        if (neckRegion.isClick()) {
            tvNeck.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (neckRegion.isDisease()) {
                tvNeck.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvNeck.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*胸button*/
        if (chestRegion.isClick()) {
            tvChest.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (chestRegion.isDisease()) {
                tvChest.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvChest.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*脚button*/
        if (footRegion.isClick()) {
            tvFoot.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (footRegion.isDisease()) {
                tvFoot.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvFoot.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
        /*疾病button*/
        if (generalRegion.isClick()) {
            tvGeneral.setBackground(getResources().getDrawable(R.drawable.health_yellow_full_sp));
        } else {
            if (generalRegion.isDisease()) {
                tvGeneral.setBackground(getResources().getDrawable(R.drawable.health_red_full_sp));
            } else {
                tvGeneral.setBackground(getResources().getDrawable(R.drawable.health_green_full_sp));
            }
        }
    }

}
