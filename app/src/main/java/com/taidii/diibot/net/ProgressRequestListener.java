package com.taidii.diibot.net;

public interface ProgressRequestListener {

    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
