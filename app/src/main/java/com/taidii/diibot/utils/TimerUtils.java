package com.taidii.diibot.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {

    private static final int WHAT = 101;
    private static final long MAX_TIME = 60000;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private long curTime = 0;
    private TimerCountListener timerCountListener;

    public TimerUtils(TimerCountListener timerCountListener) {
        this.timerCountListener = timerCountListener;
    }

    public interface TimerCountListener{

        void timerFinish();
    }

    /* 初始化Timer */
    public void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (curTime == 0) {
                    curTime = MAX_TIME;
                } else {
                    curTime -= 1000;
                }
                Message message = new Message();
                message.what = WHAT;
                message.obj = curTime;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                        /*结束*/
                        timerCountListener.timerFinish();
                    }
                    break;
            }
        }
    };

    /* destroy上次使用的*/
    public void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    /*开始倒计时*/
    public void startTimer(){
        destroyTimer();
        initTimer();
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /*取消倒计时*/
    public void cancelTimer(){
        curTime = 0;
        mTimer.cancel();
    }

    /*释放，防止泄露*/
    public void release(){
        destroyTimer();
        if (mHandler != null) {
            mHandler.removeMessages(WHAT);
            mHandler = null;
        }
    }

}
