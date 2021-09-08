package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class RegionSelectPopup extends BasePopupWindow implements View.OnClickListener{

    public enum RegionEnum{
        CHINA,SINGAPORE
    }
    private RegionSelectListener regionSelectListener;

    public RegionSelectPopup(Context context,RegionSelectListener regionSelectListener) {
        super(context);
        this.regionSelectListener = regionSelectListener;
        setPopupGravity(Gravity.CENTER);
        initEvent();
    }

    public interface RegionSelectListener{

        void chooseRegion(RegionEnum region);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_region_selet);
    }

    private void initEvent(){
        findViewById(R.id.tv_china).setOnClickListener(this);
        findViewById(R.id.tv_singapore).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_china:
                regionSelectListener.chooseRegion(RegionEnum.CHINA);
                break;
            case R.id.tv_singapore:
                regionSelectListener.chooseRegion(RegionEnum.SINGAPORE);
                break;
        }
    }

}

