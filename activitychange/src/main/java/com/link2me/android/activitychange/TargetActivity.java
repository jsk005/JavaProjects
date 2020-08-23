package com.link2me.android.activitychange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.link2me.android.common.BackPressHandler;

public class TargetActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    String CLASSName;

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        mContext = TargetActivity.this;

        initView();
    }

    private void initView() {
        CLASSName = getIntent().getExtras().getString("classname");

        textView = findViewById(R.id.targettextView);
        textView.setText("TargetActivity 입니다.");

        button = findViewById(R.id.targetbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Activity 이동의 상태를 기억했다가 다시 원래의 activity로 되돌아가기 위한 경우
                String classname = CLASSName + ".class";
                try {
                    Intent intent = new Intent(TargetActivity.this, Class.forName(classname));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                finish(); // 현재 Activity 를 없애줌
            }
        });
    }
}