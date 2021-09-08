package com.taidii.diibot.entity.school;

import com.clj.fastble.data.BleDevice;

public class FastBluetoothModel {

    private BleDevice bleDevice;
    private BtDevice btDevice;

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BtDevice getBtDevice() {
        return btDevice;
    }

    public void setBtDevice(BtDevice btDevice) {
        this.btDevice = btDevice;
    }

}
