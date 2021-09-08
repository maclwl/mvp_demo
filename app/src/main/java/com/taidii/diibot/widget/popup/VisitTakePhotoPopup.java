package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class VisitTakePhotoPopup extends BasePopupWindow implements View.OnClickListener{

    private VisitTakePhotoListener visitTakePhotoListener;

    public VisitTakePhotoPopup(Context context,VisitTakePhotoListener visitTakePhotoListener) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        this.visitTakePhotoListener = visitTakePhotoListener;
        initEvent();
    }

    public interface VisitTakePhotoListener{

        void visitTakePhoto();

        void visitSelectPhoto();
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
        return createPopupById(R.layout.popup_visit_take_photo);
    }

    private void initEvent(){
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.im_take_photo).setOnClickListener(this);
        findViewById(R.id.im_select_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.im_take_photo:
                dismiss();
                visitTakePhotoListener.visitTakePhoto();
                break;
            case R.id.im_select_photo:
                dismiss();
                visitTakePhotoListener.visitSelectPhoto();
                break;
        }
    }
}
