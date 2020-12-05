package com.link2me.android.webview;

import android.content.Context;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private WebView webView;

    private static final String URL_DAUM_MAP = "http://m.map.daum.net/";
    private static final String URL_NAVER_MAP = "http://m.map.naver.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        webView = findViewById(R.id.webview);

        initWebView();
    }

    private void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용을 허용한다.
        webView.setWebViewClient(new WebViewClient());  // 새로운 창을 띄우지 않고 내부에서 웹뷰를 실행시킨다.
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });
        webView.loadUrl(URL_NAVER_MAP);
    }
}