package com.viofo.f1;

/**
 * Created by luoxin on 2017/7/5.
 */

public interface SocketListener {
    
    void onConnectSuccess();
    void onConnectFailed();
    void onConnectBreak();
    void onSendSuccess();
    void onReceive(byte[] buffer, int length);
}
