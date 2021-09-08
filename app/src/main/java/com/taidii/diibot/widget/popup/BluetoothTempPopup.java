package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class BluetoothTempPopup extends BasePopupWindow implements View.OnClickListener{

    private BluetoothTempListener bluetoothTempListener;

    public BluetoothTempPopup(Context context,BluetoothTempListener bluetoothTempListener) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        this.bluetoothTempListener = bluetoothTempListener;
        initEvent();
    }

    public interface BluetoothTempListener{

        void connectBluetooth();
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
        return createPopupById(R.layout.popup_bluebooth_temp);
    }

    private void initEvent(){
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                bluetoothTempListener.connectBluetooth();
                dismiss();
                break;
        }
    }
}


