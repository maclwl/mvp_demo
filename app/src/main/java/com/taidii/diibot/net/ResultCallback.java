package com.taidii.diibot.net;

public interface ResultCallback<T> {

    void onStart();

    void onSuccess(T t);

    void onFinish();

}
