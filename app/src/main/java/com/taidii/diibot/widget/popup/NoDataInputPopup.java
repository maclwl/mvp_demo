package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class NoDataInputPopup extends BasePopupWindow implements View.OnClickListener{

    public NoDataInputPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        initEvent();
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
        return createPopupById(R.layout.popup_no_data_input);
    }

    private void initEvent(){
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.im_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close:
            case R.id.im_confirm:
                dismiss();
                break;
        }
    }
}
