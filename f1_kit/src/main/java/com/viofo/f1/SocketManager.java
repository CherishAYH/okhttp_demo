package com.viofo.f1;


import android.support.annotation.NonNull;

/**
 * Created by lx on 2017/6/21.
 */

public class SocketManager{
    private static final String TAG = "SocketManager";
    private TcpClient tcpClient;
    private static SocketManager instance;
    private SocketListener mSocketListener;

    private SocketManager() {
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }
    
    public final void connect_F1(@NonNull SocketListener socketListener) {
        tcpClient = new TcpClient("192.168.1.1", 8888);
        tcpClient.setSocketListener(socketListener);
        tcpClient.connect();
        mSocketListener = socketListener;
    }
    
    public void sendMessage(String msg) {
        if (tcpClient != null) {
            tcpClient.sendMessage(msg);
        }
    }
    
    public void disconnect() {
        if (tcpClient != null) {
            tcpClient.closeSocket();
        }
        instance = null;
        mSocketListener = null;
    }
}
