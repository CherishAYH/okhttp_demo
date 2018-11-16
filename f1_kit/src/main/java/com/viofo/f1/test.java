package com.viofo.f1;

import android.util.Log;

import java.util.Arrays;

public class test {
    public static void test(){
        final String startSessionCommand = "{ \"token\": 0, \"msg_id\": 257 }";
        SocketManager.getInstance().connect_F1(new SocketListener() {
            @Override
            public void onConnectSuccess() {
                //connect success
                SocketManager.getInstance().sendMessage(startSessionCommand);

            }

            @Override
            public void onConnectFailed() {
                //connect failed
            }

            @Override
            public void onConnectBreak() {
                //connect break
            }

            @Override
            public void onSendSuccess() {
                //send message success
            }

            @Override
            public void onReceive(byte[] buffer, int length) {
                //handle Command return message
                String message = new String(Arrays.copyOf(buffer, length)).trim();
                Log.e("device returned", message);
            }
        });
    }
}
