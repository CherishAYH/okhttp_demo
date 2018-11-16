package com.viofo.f1;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by VIOFO-lx on 2017/6/17.
 */

public class TcpClient {
    private static final int CONNECT_TIME_OUT = 5000;
    private String ip;
    private int port;
    private boolean isConnected = false;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SocketListener mSocketListener;

    public void setSocketListener(SocketListener socketListener){
        this.mSocketListener = socketListener;
    }
    
    TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @SuppressLint("CheckResult")
    public void connect() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) {
                startConnect(e);
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) {
                        getReceivedMessage();
                    }
                });
    }
    
    private void startConnect(FlowableEmitter<Object> e) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port), CONNECT_TIME_OUT);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            e.onNext("");
            
            isConnected = true;
            mSocketListener.onConnectSuccess();
        } catch (Exception ex) {
            mSocketListener.onConnectFailed();
            close();
        }
    
    }
    
    private void getReceivedMessage() {
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while (isConnected){
                if((length = inputStream.read(buffer)) !=-1){
                    mSocketListener.onReceive(buffer, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mSocketListener.onConnectBreak();
            close();
        }
    }

    @SuppressLint("CheckResult")
    public void sendMessage(String msg) {
        Observable.just(msg)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s){
                        send(s);
                    }
                });
    }
    
    @SuppressLint("CheckResult")
    public void closeSocket() {
        if (isConnected) {
            Observable.just("")
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o){
                            close();
                        }
                    });
        }
    }
    
    private void send(String msg) {
        if (outputStream != null) {
            try {
                outputStream.write(msg.getBytes());
                outputStream.flush();
                mSocketListener.onSendSuccess();
            } catch (Exception e) {
                mSocketListener.onConnectBreak();
                close();
                e.printStackTrace();
            }
        }
    }
    
    private void close() {
        isConnected = false;
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}