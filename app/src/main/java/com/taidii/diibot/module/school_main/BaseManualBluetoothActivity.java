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
import com.taidii.diibot.utils.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 方案：先扫描出所有打开的蓝牙设备，用户手动连接
 */
public abstract class BaseManualBluetoothActivity<T extends BasePresenter> extends BaseMvpActivity<T> {

    protected static final int REQUEST_CODE_OPEN_GPS = 1;
    protected static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    protected boolean isScanning = false;//是否正在扫描
    protected boolean hasConnect = false;//是否已连接
    protected boolean isConnecting = false;//是否正在扫描蓝牙设备
    protected List<BtDevice> mBtList = new ArrayList<>();//蓝牙筛选列表
    protected List<FastBluetoothModel> bluetoothModelList = new ArrayList<>();//扫描蓝牙结果列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*蓝牙初始化*/
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(0, 5000)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
        mBtList = Constant.getBtDeviceServiceList();
    }

    /**
     * 检测蓝牙是否打开
     */
    public boolean isBtEnable(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 扫描规则 : 扫描出所有已打开的蓝牙设备
     */
    public void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * 开始扫描蓝牙设备
     */
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
                isScanning = true;
                /*扫描结果*/
                if (result!=null&&result.getDevice()!=null&&result.getDevice().getName()!=null) {
                    /*判断当前的蓝牙*/
                    FastBluetoothModel model = new FastBluetoothModel();
                    model.setBleDevice(result);
                    bluetoothModelList.add(model);
                }
                /*扫描蓝牙列表结果*/
                bluetoothDeviceList(bluetoothModelList);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                isScanning = false;
                //扫描完成 1.关闭旋转动画，2.检测是否有扫描到蓝牙设备：有则弹窗展示设备手动连接，无则提示
                if (bluetoothModelList.size()>0){
                    bluetoothScanFinish();
                }else {
                    showLongToast(R.string.device_not_scanned);
                }
            }
        });
    }

    /**
     * 连接蓝牙设备
     * @param bleDevice
     */
    public void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                isConnecting = true;
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                bluetoothConnectFail();
                hasConnect = false;
                isConnecting = false;
                SharePrefUtils.removeKey(Constant.CONNECT_BLUETOOTH_NAME);
                showMessage(R.string.bluetooth_connect_fail);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bluetoothConnectSuccess();
                hasConnect = true;
                isConnecting = false;
                //连接成功，存入连接的蓝牙名，以作下一次自动连接
                SharePrefUtils.saveString(Constant.CONNECT_BLUETOOTH_NAME,bleDevice.getName());
                setBluetoothGatt(gatt, bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                hasConnect = false;
                isConnecting = false;
                if (isActiveDisConnected){
                    bluetoothActiveDisConnected();
                }else {
                    bluetoothDisConnect();
                }
                /*发送给签到详情*/
                EventBusUtil.sendEvent(new Event(2,"BluetoothDisconnect"));
            }

        });
    }

    /**
     * 权限检测回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * 检测权限
     */
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

    /**
     * 权限异常处理
     * @param permission
     */
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

    /**
     * 检测定位权限
     * @return
     */
    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setBluetoothGatt(BluetoothGatt gatt, BleDevice bleDevice) {
        for (final BluetoothGattService service : gatt.getServices()) {
            for (BtDevice btDevice : mBtList){
                if (service.getUuid().toString().toLowerCase().startsWith(btDevice.getService_Name())) {
                    setBluetoothGattService(service, bleDevice,btDevice);
                    break;
                }
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
                                        /*区分vams蓝牙主板和体温枪数据解析*/
                                        if (characteristic.getUuid().toString().toLowerCase().startsWith(Constant.Characteristic_Name_str3)) {
                                            //vams解析方式
                                            data = new String(characteristic.getValue());
                                        } else {
                                            //体温枪解析方式
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

    /**
     * 停止扫描
     */
    public void cancelScanBluetooth(){
        BleManager.getInstance().cancelScan();
    }

    /**
     * 关闭蓝牙
     */
    public void closeFastBluetooth(){
        BleManager.getInstance().disableBluetooth();
    }

    /**
     * 断开所有连接蓝牙
     */
    public void disConnectFastBluetooth(){
        BleManager.getInstance().disconnectAllDevice();
    }

    /**
     * 扫描蓝牙设备结束
     */
    protected void bluetoothScanFinish(){

    }

    /**
     * 蓝牙连接成功
     */
    protected void bluetoothConnectSuccess(){

    }

    /**
     * 已连上的蓝牙主动断开连接
     */
    protected void bluetoothDisConnect(){

    }

    /**
     * 连接上的蓝牙手动断开连接
     */
    protected void bluetoothActiveDisConnected(){

    }

    /**
     * 连接失败
     */
    protected void bluetoothConnectFail(){

    }

    /**
     * 读取温度数据
     * @param tempValue
     */
    protected void bluetoothTempValue(String tempValue){
        Log.i("BaseManualBluetooth", "bluetoothTempValue: ---"+tempValue);
        /*发送消息(发送温度)*/
        EventBusUtil.sendEvent(new Event(1,tempValue));
    }

    /**
     * 蓝牙设备列表
     * @param resultModelList
     */
    protected void bluetoothDeviceList(List<FastBluetoothModel> resultModelList){

    }

    /**
     * 切换重新连接设备
     * @param model
     */
    public void changeConnectBluetooth(FastBluetoothModel model){
        connect(model.getBleDevice());
    }

}
