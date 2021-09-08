package com.taidii.diibot.net;

public interface ProgressResponseListener {

    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}