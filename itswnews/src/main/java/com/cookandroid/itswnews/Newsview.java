package com.cookandroid.itswnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class Newsview extends AppCompatActivity {

    Context mContext;
    private WebView mWebView; // 웹뷰
    private WebSettings mWebSettings; // 웹뷰 세팅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsview);
        mContext = Newsview.this;

        initView(getIntent().getExtras().getString("NEWS_URL"));
    }

    private void initView(String web_url) {
        try {
            String url = URLDecoder.decode(web_url,"UTF-8"); // target

            mWebView = (WebView) findViewById(R.id.webview); // Layout 와의 연결
            // Javascript를 WebView에 삽입하려는 개발자는 페이지 로드가 시작된 후에 스크립트를 실행해야 한다.
            mWebSettings = mWebView.getSettings(); // 세부 세팅 등록
            mWebSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 허용

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);

            //mWebSettings.setSaveFormData(true); // 폼 입력값 저장 여부
            mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
            mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
            mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
            mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
            mWebSettings.setDisplayZoomControls(false);
            mWebSettings.setSupportZoom(true); // 줌 사용 여부
            mWebSettings.setUserAgentString("Android");
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
            mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
            if(Build.VERSION.SDK_INT >= 16){
                mWebSettings.setAllowFileAccessFromFileURLs(true);
                mWebSettings.setAllowUniversalAccessFromFileURLs(true);
            }
            if(Build.VERSION.SDK_INT >= 21){
                mWebSettings.setMixedContentMode(mWebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    //activity.setProgress(progress * 100); // display at android:label
                }

                @Override
                public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                    new AlertDialog.Builder(view.getContext()).setTitle("메시지")	.setMessage(message)
                            .setPositiveButton(android.R.string.ok,	new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            }).setCancelable(true).create().show();
                    return true;
                }

                @Override
                public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                    new AlertDialog.Builder(view.getContext()).setTitle("메시지")	.setMessage(message)
                            .setPositiveButton(android.R.string.ok,	new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,	int which) {
                                    result.confirm();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel,	new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,	int which) {
                                    result.cancel();
                                }
                            }).create().show();
                    return true;
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {
                // 컨텐츠를 랜더링할 때 호출된다. 로딩중인 url을 가로챌 수 있다.
                public void onReceivedError(WebView view, int errorCode, String description, String fallingUrl) {
                    Toast.makeText(mContext,
                            "WebViewClient Error(" + String.valueOf(errorCode)
                                    + "):" + description, Toast.LENGTH_SHORT).show();
                }

                @SuppressLint("MissingPermission")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("tel:")) {
                        Intent call_phone = new Intent(Intent.ACTION_CALL);
                        call_phone.setData(Uri.parse(url));
                        startActivity(call_phone); // 권한 설정은 Loing.java에서 처리했음
                    } else if (url.startsWith("sms:")) {
                        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                        startActivity(i);
                    } else if (url.startsWith("intent:")) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                            if (existPackage != null) {
                                startActivity(intent);
                            } else {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                                startActivity(marketIntent);
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        view.loadUrl(url);
                    }
                    return true;
                }

            });

            mWebView.loadUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}