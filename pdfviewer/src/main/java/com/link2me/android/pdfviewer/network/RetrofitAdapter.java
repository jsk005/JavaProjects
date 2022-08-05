package com.link2me.android.pdfviewer.network;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {
    static Retrofit retrofit = null;
    static Retrofit retrofit_service = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitUrl.BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient createOkHttpClient() {
        // 네트워크 통신 로그(서버로 보내는 파라미터 및 받는 파라미터) 보기
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.cookieJar(new RetrofitCookieJar()); // Session 통신을 위한 기능
        // 네트워크 통신 로그(서버로 보내는 파라미터 및 받는 파라미터) 보기
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public static <T> T createService(Class<T> serviceClass) {
        // implementation "com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0"
        if (retrofit_service == null) {
            retrofit_service = new Retrofit.Builder()
                    .baseUrl(RetrofitUrl.BASE_URL)
                    .client(createOkHttpClient())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        }
        return retrofit_service.create(serviceClass);
    }

}
