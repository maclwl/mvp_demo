package com.taidii.diibot.app;

import android.os.Environment;

import com.taidii.diibot.entity.school.BtDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constant {

    /*file*/
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/diibot/";
    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "diibot" + File.separator + "images" + File.separator;
    public final static String IMAGE_CACHE_DATA = IMAGE_PATH + "imageCache/";

    /*arc_face*/
    public static final String APP_ID = "6AYmavW4DsosPLM6xV8xzqYGsAsFNb8JakbHPVH2vMES";
    public static final String SDK_KEY = "CgzqQxk7FYQnw9hcobM6mnXa4f8nxAXxPheHMy7dnoox";

    /**
     * IR预览数据相对于RGB预览数据的横向偏移量，注意：是预览数据，一般的摄像头的预览数据都是 width > height
     */
    public static final int HORIZONTAL_OFFSET = 0;
    /**
     * IR预览数据相对于RGB预览数据的纵向偏移量，注意：是预览数据，一般的摄像头的预览数据都是 width > height
     */
    public static final int VERTICAL_OFFSET = 0;

    /*custom*/
    public static final String USERNAME= "USERNAME";
    public static final String PASSWORD= "PASSWORD";
    public static final String HEALTH_CHECK= "HEALTH_CHECK";
    public static final String IS_ABROAD= "IS_ABROAD";
    public static final String SAFE_ACCESS= "SAFE_ACCESS";
    public static final String DES_KEY= "DES_KEY";
    public static final String DES_IV= "DES_IV";
    public static final String TYPE= "TYPE";
    public static final String ID= "ID";
    public static final String NAME= "NAME";
    public static final String AVATAR= "AVATAR";
    public static final String DATA= "DATA";
    public static final String SIGN_TYPE = "SIGN_TYPE";
    public static final String GUARDIANS_LIST= "GUARDIANS_LIST";
    public static final String STUDENT_MODEL_LIST= "STUDENT_MODEL_LIST";
    public static final String SCHOOL_CONTENT_MODEL= "SCHOOL_CONTENT_MODEL";
    public static final String ORGAN_CONTENT_MODEL= "ORGAN_CONTENT_MODEL";
    public static final String ORGAN_CLASS_ORDER= "ORGAN_CLASS_ORDER";
    public static final String DATE_TIME_RULE= "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_DATE= "yyyy-MM-dd";
    public static final String LONG_TIME= "HH:mm:ss";
    public static final String SHORT_TIME= "HH:mm";
    public static final String VISIT_DATE= "dd/MM/yyyy";
    public static final String VISIT_HH= "HH";
    public static final String VISIT_MM= "mm";
    public static final String VISIT_SS= "ss";
    public static final String DELETE_HISTORY_VALUE= "%s_%s";
    public static final String MAIN_SCHOOL_COUNT= "%s/%s";
    public static final String CLASS_ROOM= "(%s)";
    public static final String GENERAL_TYPE= "GENERAL_TYPE";
    public static final String FACE_ACTIVE= "FACE_ACTIVE";
    public static final String HAS_CONNECT= "HAS_CONNECT";
    public static final String BLUETOOTH_DATA= "BLUETOOTH_DATA";

    /*人脸信息类别*/
    public static final String FACE_STUDENT= "STUDENT";
    public static final String FACE_STAFF= "STAFF";
    public static final String FACE_GUARDIAN= "GUARDIAN";

    /*agreement*/
    public static final String URL= "URL";
    public static final String PRIVACY_URL= "file:///android_asset/www/xieyi.html";
    public static final String AGREEMENT_URL= "file:///android_asset/www/yonghu.html";
    public static final String PRIVACY_EN_URL= "file:///android_asset/www/xieyi_en.html";
    public static final String AGREEMENT_EN_URL= "file:///android_asset/www/yonghu_en.html";

    /*main_setting*/
    public static final String IS_STAFF_CHECK= "IS_STAFF_CHECK";
    public static final String IS_STAFF_CHECK_CAMERA= "IS_STAFF_CHECK_CAMERA";
    public static final String IS_QUICK_CHECK= "IS_QUICK_CHECK";
    public static final String IS_STUDENT_CHECK_CAMERA= "IS_STUDENT_CHECK_CAMERA";
    public static final String IS_ORGAN_STUDENT_CHECK_CAMERA= "IS_ORGAN_STUDENT_CHECK_CAMERA";
    public static final String IS_SPREAD_ALL= "IS_SPREAD_ALL";
    public static final String IS_OUT_LINE= "IS_OUT_LINE";
    public static final String IS_TEMP_DEFAULT= "IS_TEMP_DEFAULT";
    public static final String IS_VISIBLE= "IS_VISIBLE";
    public static final String CONNECT_BLUETOOTH_NAME= "CONNECT_BLUETOOTH_NAME";
    public static final String IS_HIDE_SIGN_IN= "IS_HIDE_SIGN_IN";


    /*蓝牙刷选相关（废弃）*/
    public static final String Device_Name_Mn = "MN583_";
    public static final String Service_Name_Mn = "0000fff0";
    public static final String Characteristic_Name_Mn = "0000fff1";

    public static final String Device_Name_88 = "8806";
    public static final String Service_Name_88 = "0000fff0";
    public static final String Characteristic_Name_88 = "0000fff1";

    public static final String Device_Name_ly = "LYCWY-";
    public static final String Service_Name_ly = "0000ffe0";
    public static final String Characteristic_Name_ly = "0000ffe4";

    public static final String Device_Name_Bf = "BF4030";
    public static final String Service_Name_Bf = "000018f0";
    public static final String Characteristic_Name_Bf = "00002af0";

    public static final String SCREEN_BLUETOOTH = "MN583_,8806,LYCWY-,BF4030";

    public static final List<BtDevice> getBtDeviceList(){
        List<BtDevice> mBtList = new ArrayList<>();
        BtDevice btDevice = new BtDevice(Device_Name_Mn, Service_Name_Mn, Characteristic_Name_Mn);
        BtDevice btDevice2 = new BtDevice(Device_Name_ly, Service_Name_ly, Characteristic_Name_ly);
        BtDevice btDevice3 = new BtDevice(Device_Name_Bf, Service_Name_Bf, Characteristic_Name_Bf);
        BtDevice btDevice4 = new BtDevice(Device_Name_88, Service_Name_88, Characteristic_Name_88);
        mBtList.add(btDevice);
        mBtList.add(btDevice2);
        mBtList.add(btDevice3);
        mBtList.add(btDevice4);
        return mBtList;
    }

    /*蓝牙筛选新（通过蓝牙服务名做为筛选条件）*/
    public static final String Service_Name_str1 = "000018f0";
    public static final String Characteristic_Name_str1 = "00002af0";

    public static final String Service_Name_str2 = "0000fff0";
    public static final String Characteristic_Name_str2 = "0000fff1";

    public static final String Service_Name_str3 = "0000ffe0";
    public static final String Characteristic_Name_str3 = "0000ffe4";

    public static final String SCREEN_SERVICE1 = "18f0";
    public static final String SCREEN_SERVICE2 = "fff0";
    public static final String SCREEN_SERVICE3 = "ffe0";


    public static final List<BtDevice> getBtDeviceServiceList(){
        List<BtDevice> mBtList = new ArrayList<>();
        BtDevice btDevice = new BtDevice("", Service_Name_str1, Characteristic_Name_str1);
        BtDevice btDevice2 = new BtDevice("", Service_Name_str2, Characteristic_Name_str2);
        BtDevice btDevice3 = new BtDevice("", Service_Name_str3, Characteristic_Name_str3);
        mBtList.add(btDevice);
        mBtList.add(btDevice2);
        mBtList.add(btDevice3);
        return mBtList;
    }

    /*界面跳转标记位*/
    public static final int STUDENT_SIGN_ACTIVITY = 1001;
    public static final int STAFF_SIGN_ACTIVITY = 1002;

}
