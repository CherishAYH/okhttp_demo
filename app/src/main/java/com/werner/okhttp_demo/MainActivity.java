package com.werner.okhttp_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String url = "http://192.168.1.254/?custom=1&cmd=3012";
    private static final String url2 = "http://192.168.1.254/?custom=1&cmd=3024";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        OkHttpUtils.initClient(client);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt1:

                send(url);

                break;
            case R.id.bt2:

                send(url2);
                break;
            case R.id.bt3:


                send(url);

                break;
        }
    }

    protected void send(final String url) {
        OkHttpUtils.get()
                .url(url)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (e != null) Log.e("Exception-----", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        response = response.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", "");
                        Log.e("response-----", response.toString());
                    }
                });



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response response = OkHttpUtils.get()
//                            .url(url)
//                            .tag(url)
//                            .build()
//                            .execute();
//
//                    if (response != null){
//                        Log.e("res", response.body().toString());
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("IOException", e.toString());
//                }
//            }
//        }).start();
    }
}
