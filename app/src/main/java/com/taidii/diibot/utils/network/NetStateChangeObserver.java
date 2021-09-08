package com.taidii.diibot.utils.network;

import com.taidii.diibot.entity.enums.NetworkType;

public interface NetStateChangeObserver {

    void onNetDisconnected();
    void onNetConnected(NetworkType networkType);
}
