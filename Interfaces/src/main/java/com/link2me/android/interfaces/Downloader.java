package com.link2me.android.interfaces;

import com.link2me.android.interfaces.interfaces.OnDownloadListener;

public class Downloader {

    private OnDownloadListener mListener;

    public Downloader(OnDownloadListener listener) {
        // 인터페이스 연결
        mListener = listener;
    }

    public void start() {

        System.out.println("Download Start.");
        try {
            Thread.sleep(5000);
            // 실제 작업 처리(이미지 다운로드)하는 거라고 생각해라.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mListener.onDownFinish();

    }
}
