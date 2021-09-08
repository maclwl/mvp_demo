package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.taidii.diibot.R;
import razerdp.basepopup.BasePopupWindow;

public class FaceSynchronizationPopup extends BasePopupWindow implements OnProgressBarListener {

    private NumberProgressBar numSyn;

    public FaceSynchronizationPopup(Context context) {
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
        return createPopupById(R.layout.popup_face_synchronization);
    }

    private void initEvent(){
        numSyn = findViewById(R.id.num_syn);
        numSyn.setOnProgressBarListener(this);
    }

    public void initNumMax(int max){
        numSyn.setMax(max);
        Log.i("aaaaaaaa", "initNumMax: ---"+max);
    }

    public void incrementProgress(int progress){
        numSyn.incrementProgressBy(1);
        Log.i("aaaaaaaa", "incrementProgress: ---"+progress);
    }

    @Override
    public void onProgressChange(int current, int max) {
        Log.i("aaaaaaaa", "onProgressChange: ---"+current+"-----"+max);
        if(current == max-1) {
           dismiss();
           numSyn.setProgress(0);
        }
    }

}
