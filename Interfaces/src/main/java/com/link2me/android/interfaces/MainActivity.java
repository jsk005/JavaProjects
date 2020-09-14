package com.link2me.android.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.link2me.android.interfaces.interfaces.RemoteControl;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        Button btn_download = findViewById(R.id.btn_downloader);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Browser browser = new Browser(); // 객체 생성
                browser.imgClick(); // 메소드 실행 : 이미지 다운로드 해라.
            }
        });

        Button btn_rc_local = findViewById(R.id.btn_rc_local);
        btn_rc_local.setText("인터페이스 로컬변수");
        btn_rc_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 개발 코드
                RemoteControl rc; // 인터페이스 변수(참조 타입)

                rc = new Television(); // 구현 객체 생성
                rc.turnOn();
                rc.turnOff();

                rc = new Audio(); // 구현 객체 생성
                rc.turnOn();
                rc.turnOff();

            }
        });

        Button btn_rc_parameter = findViewById(R.id.btn_rc_parameter);
        btn_rc_parameter.setText("인터페이스 매개변수");
        btn_rc_parameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = new Device();

                Television tv = new Television();
                Audio audio = new Audio();

                device.turnon(tv);
                device.turnon(audio);

                device.turnoff(tv);
            }
        });

        findViewById(R.id.btn_activity_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityChanger changer = new ActivityChanger();
                changer.saveData(mContext,OtherActivity.class);
            }
        });

        findViewById(R.id.btn_customdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,DialogActivity.class));
                finish();
            }
        });
    }
}
