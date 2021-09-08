package com.taidii.diibot.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

public class MathUtil {

    public static final String PLACE_ONE = "#0.0";

    public static final String formatNumber(Number number) {
        return formatNumber(number, "#0.00");
    }

    public static final String formatNumber(Number number, String format) {
        if (TextUtils.isEmpty(format)) {
            return String.valueOf(number);
        }
        return new DecimalFormat(format).format(number);
    }

}
