
package com.taidii.diibot.utils;

import com.taidii.diibot.BuildConfig;
import com.google.gson.JsonObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class LogUtils {
    static {
        if (BuildConfig.DEBUG) {
            Logger.init("Diibot")
                    .hideThreadInfo()
                    .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Logger.d(tag, message);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg);
        }
    }

    public static void d(String msg, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg, args);
        }
    }

    public static void d(JsonObject json) {
        d(json.toString());
    }

    public static void d(boolean b) {
        d(b + "");
    }

    public static void d(int i) {
        d(i + "");
    }

    public static void out(String msg) {
        if (BuildConfig.DEBUG) {
            System.out.println(msg);
        }
    }

    public static void e(String tag, String message) {
    }

    public static void trace(String tag, String message) {
    }
}
