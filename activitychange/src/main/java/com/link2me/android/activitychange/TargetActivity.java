package com.link2me.android.activitychange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TargetActivity extends AppCompatActivity implements ActivityChangListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private static Class classToGoAfter;

    String ClassName;
    String getData;

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        mContext = TargetActivity.this;

        Log.e(TAG,"onCreate classToGoAfter :" + classToGoAfter);

        initView();
    }

    private void initView() {
        if(classToGoAfter == null) {
            ClassName = getIntent().getExtras().getString("classname");
        }

        Log.e(TAG,"get ClassName :" + ClassName);
        Log.e(TAG,"classToGoAfter :" + classToGoAfter);

        textView = findViewById(R.id.targettextView);
        textView.setText("TargetActivity 입니다.");

        button = findViewById(R.id.targetbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClassName != null) {
                    // Activity 이동의 상태를 기억했다가 다시 원래의 activity로 되돌아가기 위한 경우
                    try {
                        Intent intent = new Intent(TargetActivity.this, Class.forName(ClassName));
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    finish(); // 현재 Activity 를 없애줌
                } else if(classToGoAfter != null) {
                    gotoActivity();
                }
            }
        });

        findViewById(R.id.btn_changer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivity();
                Toast.makeText(mContext, "Other Activity 로 이동합니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void activityClass(Class activityClassToGo) {
        Log.e(TAG,"activityClass :" + activityClassToGo);
        // 인터페이스 구현
        classToGoAfter = activityClassToGo;
    }

    private void gotoActivity() {
        Log.e(TAG,"gotoActivity :" + classToGoAfter);
        Intent intent = new Intent(mContext, classToGoAfter);
        startActivity(intent);
        finish();
    }
}