package com.taidii.diibot.module.school_main;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.base.mvp.BasePresenter;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.entity.school.BtDevice;
import com.taidii.diibot.entity.school.FastBluetoothModel;
import com.taidii.diibot.utils.EventBusUtil;
import com.taidii.diibot.utils.UuidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 老版本通过蓝牙名筛选扫描设备并自动连接（存在问题，添加新的蓝牙设备名扫描不到）
 *
 */
public abstract class BaseFastBluetoothActivity <T extends BasePresenter> extends BaseMvpActivity<T> {

    protected static final int REQUEST_CODE_OPEN_GPS = 1;
    protected static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    protected boolean isDisconnectAll = false;//是否手动断开所有
    protected boolean isConnecting = false;//是否正在扫描蓝牙设备
    protected boolean hasConnect = false;//是否已连接
    protected boolean isAutoDisconnect = true;//是否自动断开连接
    protected List<BtDevice> mBtList = new ArrayList<>();//蓝牙筛选列表
    protected List<FastBluetoothModel> bluetoothModelList = new ArrayList<>();//扫描蓝牙结果列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*蓝牙初始化*/
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
        mBtList = Constant.getBtDeviceServiceList();
    }

    /*检测蓝牙是否打开*/
    public boolean isBtEnable(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 扫描规则 : 修改老版本使用设备名筛选改为通过蓝牙服务名为筛选条件(service_uuid 篩選問題（多服务设备扫描不）)
     */
    public void setScanRule() {
        /*name筛选*/
        String[] names = Constant.SCREEN_BLUETOOTH.split(",");//老版本蓝牙名筛选存在问题（）
        UUID[] service_uuid = new UUID[]{UUID.fromString(UuidUtils.uuid16To128(Constant.SCREEN_SERVICE1))
                                        ,UUID.fromString(UuidUtils.uuid16To128(Constant.SCREEN_SERVICE2))
                                        ,UUID.fromString(UuidUtils.uuid16To128(Constant.SCREEN_SERVICE3))};

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                //.setServiceUuids(service_uuid)// 只扫描指定的服务的设备，可选
                //.setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(15000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /*扫描蓝牙（方案二（先扫描获取所有蓝牙。默认连接第一个达到自动连接效果））*/
    public void startScan() {
        bluetoothModelList.clear();
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice result) {
                /*扫描结果*/
                if (result!=null&&result.getDevice()!=null&&result.getDevice().getName()!=null){
                    /*判断当前的蓝牙*/
                    FastBluetoothModel model = new FastBluetoothModel();
                    model.setBleDevice(result);
                    bluetoothModelList.add(model);
                    if (result.getDevice().getName().contains(Constant.Device_Name_Mn)){
                        model.setBleDevice(result);
                        model.setBtDevice(mBtList.get(0));
                        bluetoothModelList.add(model);
                    }else if (result.getDevice().getName().contains(Constant.Device_Name_ly)){
                        model.setBleDevice(result);
                        model.setBtDevice(mBtList.get(1));
                        bluetoothModelList.add(model);
                    }else if (result.getDevice().getName().contains(Constant.Device_Name_Bf)){
                        model.setBleDevice(result);
                        model.setBtDevice(mBtList.get(2));
                        bluetoothModelList.add(model);
                    }else if (result.getDevice().getName().contains(Constant.Device_Name_88)){
                        model.setBleDevice(result);
                        model.setBtDevice(mBtList.get(3));
                        bluetoothModelList.add(model);
                    }
                }
                /*默认连接第一个*/
                if (bluetoothModelList.size()==1){
                    connect(bluetoothModelList.get(0).getBleDevice(),bluetoothModelList.get(0).getBtDevice());
                }
                /*扫描蓝牙列表结果*/
                bluetoothDeviceList(bluetoothModelList);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if (scanResultList.size()<=0){
                    bluetoothConnectFail();
                    hasConnect = false;
                    isConnecting = false;
                }
            }
        });
    }

    /*连接蓝牙*/
    public void connect(final BleDevice bleDevice,BtDevice btDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                bluetoothConnectFail();
                hasConnect = false;
                isConnecting = false;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bluetoothConnectSuccess();
                hasConnect = true;
                isConnecting = false;
                setBluetoothGatt(gatt, bleDevice,btDevice);
                /*关联人脸识别界面蓝牙连接*/
                EventBusUtil.sendEvent(new Event(3,"BluetoothConnect"));
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bluetoothDisConnect();
                hasConnect = false;
                isConnecting = false;
                isAutoDisconnect = true;
                /*发送给签到详情*/
                EventBusUtil.sendEvent(new Event(2,"BluetoothDisconnect"));
            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    /*权限检查/扫描蓝牙并连接*/
    public void checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /*权限管理*/
    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.notify_title)
                            .setMessage(R.string.gps_notify_msg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setBluetoothGatt(BluetoothGatt gatt, BleDevice bleDevice,BtDevice btDevice) {
        for (final BluetoothGattService service : gatt.getServices()) {
            if (service.getUuid().toString().toLowerCase().startsWith(btDevice.getService_Name())) {
                setBluetoothGattService(service, bleDevice,btDevice);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setBluetoothGattService(BluetoothGattService service, BleDevice bleDevice,BtDevice btDevice) {
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            if (characteristic.getUuid().toString().toLowerCase().startsWith(btDevice.getCharacteristic_Name())) {
                setCharacteristic(characteristic, bleDevice);
                break;
            }
        }
    }

    private List<Float> temList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setCharacteristic(final BluetoothGattCharacteristic characteristic, final BleDevice bleDevice) {
        runOnMainThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                BleManager.getInstance().notify(bleDevice,
                        characteristic.getService().getUuid().toString(),
                        characteristic.getUuid().toString(),
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {

                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {

                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String data = null;
                                        if (bleDevice.getName().equals(Constant.Device_Name_ly)) {
                                            data = new String(characteristic.getValue());
                                        } else {
                                            data = String.valueOf(HexUtil.encodeHex(characteristic.getValue()));
                                        }
                                        if (data.contains("T:")) {
                                            String tem = data.substring(data.indexOf("T:") + 2, data.indexOf("T:") + 6);
                                            if (temList.size() > 0) {

                                            }
                                            bluetoothTempValue(tem);
                                        }

                                        if (data.startsWith("ff") && data.length() == 8) {
                                            data = data.substring(2, 6);
                                            int temp = Integer.parseInt(data, 16);
                                            data = String.valueOf(temp * 0.1);
                                            if (data.length() > 4) {
                                                data = data.substring(0, 4);
                                            }
                                            bluetoothTempValue(data);
                                        } else if (data.startsWith("d5") && data.length() == 18) {
                                            data = data.substring(12, 16);
                                            int tempH = Integer.parseInt(data.substring(0, 2), 16);
                                            int tempL = Integer.parseInt(data.substring(2, 4), 16);
                                            data = String.valueOf((tempH * 256 + tempL) / 10.0);
                                            bluetoothTempValue(data);
                                        }
                                    }
                                });
                            }
                        });
            }
        });

    }

    private void runOnMainThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            threadHandler.post(runnable);
        }
    }

    private Handler threadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        }
    }

    /*-----------------相关状态处理--------------------*/

    /*停止扫描*/
    public void cancelScanBluetooth(){
        BleManager.getInstance().cancelScan();
    }

    /*关闭蓝牙*/
    public void closeFastBluetooth(){
        isAutoDisconnect = false;
        BleManager.getInstance().disableBluetooth();
    }

    /*断开所有连接蓝牙*/
    public void disConnectFastBluetooth(){
        isDisconnectAll = true;
        BleManager.getInstance().disconnectAllDevice();
    }

    /*蓝牙连接成功*/
    protected void bluetoothConnectSuccess(){

    }

    /*蓝牙断开连接*/
    protected void bluetoothDisConnect(){

    }

    /*连接失败*/
    protected void bluetoothConnectFail(){

    }

    protected void bluetoothTempValue(String tempValue){
        Log.i("BaseFastActivity", "bluetoothTempValue: ---"+tempValue);
        /*发送消息(发送温度)*/
        EventBusUtil.sendEvent(new Event(1,tempValue));
    }

    /*蓝牙设备列表*/
    protected void bluetoothDeviceList(List<FastBluetoothModel> resultModelList){

    }

    /*切换重新连接设备*/
    public void changeConnectBluetooth(FastBluetoothModel model){
        /*切换连接蓝牙*/
        connect(model.getBleDevice(),model.getBtDevice());
    }

    /**
     *单一蓝牙扫描并连接
     */
//    /*扫描蓝牙并连接(方案一（连接方式：先自动扫描并连接，蓝牙列表则重新调取扫描蓝牙）
//    问题：出现同种类型的多个蓝牙，自动扫描并连接会失败。连接成功状态出现扫描蓝牙列表不成功或不全问题)*/
//    public void startScanConnect() {
//        BleManager.getInstance().scanAndConnect(new BleScanAndConnectCallback() {
//            @Override
//            public void onScanFinished(BleDevice scanResult) {
//                if (!hasConnect){
//                    bluetoothConnectFail();
//                }
//            }
//
//            @Override
//            public void onStartConnect() {
//
//            }
//
//            @Override
//            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                bluetoothConnectFail();
//                hasConnect = false;
//                isConnecting = false;
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//            @Override
//            public void onConnectSuccess(BleDevice result, BluetoothGatt gatt, int status) {
//                bluetoothConnectSuccess();
//                hasConnect = true;
//                isConnecting = false;
//                if (result!=null&&result.getDevice()!=null&&result.getDevice().getName()!=null){
//                    /*判断当前的蓝牙*/
//                    if (result.getDevice().getName().contains(Constant.Device_Name_Mn)){
//                        setBluetoothGatt(gatt, result,mBtList.get(0));
//                    }else if (result.getDevice().getName().contains(Constant.Device_Name_ly)){
//                        setBluetoothGatt(gatt, result,mBtList.get(1));
//                    }else if (result.getDevice().getName().contains(Constant.Device_Name_Bf)){
//                        setBluetoothGatt(gatt, result,mBtList.get(2));
//                    }else if (result.getDevice().getName().contains(Constant.Device_Name_88)){
//                        setBluetoothGatt(gatt, result,mBtList.get(3));
//                    }
//                }
//            }
//
//            @Override
//            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
//                bluetoothDisConnect();
//                hasConnect = false;
//                isConnecting = false;
//            }
//
//            @Override
//            public void onScanStarted(boolean success) {
//
//            }
//
//            @Override
//            public void onScanning(BleDevice bleDevice) {
//
//            }
//
//        });
//    }

}
