package com.taidii.diibot.widget.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taidii.diibot.R;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.module.school_main.adapter.BluetoothDeviceAdapter;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class CloseBluetoothPopup extends BasePopupWindow implements View.OnClickListener{

    private Context mContext;
    private RecyclerView recyclerView;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;
    private CloseBluetoothListener closeBluetoothListener;

    public CloseBluetoothPopup(Context context,CloseBluetoothListener closeBluetoothListener) {
        super(context);
        mContext = context;
        setPopupGravity(Gravity.CENTER);
        initEvent();
        this.closeBluetoothListener = closeBluetoothListener;
    }

    public interface CloseBluetoothListener{

        void closeBluetooth();

        void changeConnectDevice(FastBluetoothModel model);
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
        return createPopupById(R.layout.popup_close_bluetooth);
    }

    private void initEvent(){
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_device);
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(bluetoothDeviceAdapter);
        initListener();
    }

    private void initListener(){
        bluetoothDeviceAdapter.setItemClickListener(new BluetoothDeviceAdapter.ItemClickListener() {
            @Override
            public void itemClick(FastBluetoothModel model) {
                if (closeBluetoothListener != null){
                    closeBluetoothListener.changeConnectDevice(model);
                }
            }
        });
    }

    public void setDateList(List<FastBluetoothModel> resultModelList){
        bluetoothDeviceAdapter.setDataList(resultModelList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.btn_close:
                closeBluetoothListener.closeBluetooth();
                dismiss();
                break;
        }
    }
}
