package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.taidii.diibot.R;

import razerdp.basepopup.BasePopupWindow;

public class BluetoothConnectFailPopup extends BasePopupWindow implements View.OnClickListener{

    private BluetoothConnectListener bluetoothConnectListener;

    public BluetoothConnectFailPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        initEvent();
    }

    public interface BluetoothConnectListener{

        void stopConnectBluetooth();
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
        return createPopupById(R.layout.popup_bluetooth_connect_fail);
    }

    private void initEvent(){
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
            case R.id.tv_confirm:
                dismiss();
                break;
        }
    }
}


