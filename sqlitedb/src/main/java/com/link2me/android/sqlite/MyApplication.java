package com.link2me.android.sqlite;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public static OkHttpClient stethoInterceptingClient;
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        stethoInterceptingClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
