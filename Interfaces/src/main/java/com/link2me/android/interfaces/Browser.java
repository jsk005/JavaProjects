package com.link2me.android.interfaces;

import com.link2me.android.interfaces.interfaces.OnDownloadListener;

public class Browser implements OnDownloadListener {
    // 인터페이스에 선언된 추상 메서드의 실체 메서드 선언

    void imgClick(){
        Downloader loader = new Downloader(this);
        loader.start(); // 시작 : 이미지 다운로드 시작
    }

    @Override
    public void onDownFinish() {
        System.out.println("Browser : onDownFinish()");
    }

    @Override
    public void onDownFailed() {

    }
}
