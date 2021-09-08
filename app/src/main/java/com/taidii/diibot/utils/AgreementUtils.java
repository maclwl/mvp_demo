package com.taidii.diibot.utils;

import android.content.Context;

import com.taidii.diibot.app.Constant;

public class AgreementUtils {

    /**
     * 获取隐私政策
     * @param context
     * @return
     */
    public static String getPrivacyUrl(Context context){
        if (Utils.isZh(context)){
            return Constant.PRIVACY_URL;
        }else {
            return Constant.PRIVACY_EN_URL;
        }
    }

    /**
     * 获取用户协议
     * @param context
     * @return
     */
    public static String getAgreementUrl(Context context){
        if (Utils.isZh(context)){
            return Constant.AGREEMENT_URL;
        }else {
            return Constant.AGREEMENT_EN_URL;
        }
    }

}
