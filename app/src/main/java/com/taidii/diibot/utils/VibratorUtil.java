package com.taidii.diibot.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

public class VibratorUtil {

    private static VibratorUtil instance;
    private Context mAppContext;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    private Ringtone ringtone;
    private static final int MIN_TIME_OUT = 4000; //时间间隔
    long lastNotificationTime;

    private VibratorUtil(Context appContext) {
        mAppContext = appContext;
        init();
    }

    public static VibratorUtil getInstance(Context appContext) {
        if (instance == null) {
            synchronized(VibratorUtil.class) {
                if (instance == null) {
                    instance = new VibratorUtil(appContext.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private void init(){
        audioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE); //此方法是由Context调用的
        vibrator = (Vibrator) mAppContext.getSystemService(Context.VIBRATOR_SERVICE); //同上
    }

    /**
     * 开启手机震动和播放系统提示铃声
     */
    public void vibrateAndPlayTone() {
        if (System.currentTimeMillis() - lastNotificationTime < MIN_TIME_OUT) {
            return;
        }
        try {
            lastNotificationTime = System.currentTimeMillis();
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            }
            long[] pattern = new long[]{0, 180, 80, 120};
            vibrator.vibrate(pattern, -1); //震动
            if (ringtone == null) {
                Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                ringtone = RingtoneManager.getRingtone(mAppContext, notificationUri);
                if (ringtone == null) {
                    return;
                }
            }
            if (!ringtone.isPlaying()) {
                ringtone.play();
                //判断手机品牌
                String vendor = Build.MANUFACTURER;
                if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                    Thread ctlThread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if (ringtone.isPlaying()) {
                                    ringtone.stop();
                                }
                            } catch (Exception e) {
                            }
                        }
                    };
                    ctlThread.run();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
