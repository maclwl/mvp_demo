package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public abstract class CustomPopup extends BasePopupWindow implements View.OnClickListener{

    private TextView tvTitle;

    public CustomPopup(Context context) {
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
        return createPopupById(R.layout.popup_custom);
    }

    private void initEvent(){
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                clickConfirm();
                dismiss();
                break;
        }
    }

    public abstract void clickConfirm();
}


