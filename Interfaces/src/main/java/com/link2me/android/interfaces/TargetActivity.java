package com.link2me.android.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.link2me.android.interfaces.interfaces.ActivityChangListener;

public class TargetActivity extends AppCompatActivity implements ActivityChangListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private static Class classToGoAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mContext = TargetActivity.this;

        Log.e(TAG,"onCreate classToGoAfter :" + classToGoAfter);

        // 화면이 보인다.
        // 작업을 한 후 클릭을 하면 특정 Activity 로 이동한다.
        // 또는 작업을 한 후 특정 Activity 로 이동된다.

        // 시작 시간
        long start = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        Log.e(TAG,"elapsed time :" + (end - start)/1000.0);

        gotoActivity();

//        Button btn_click = findViewById(R.id.btn_move_activity);
//        btn_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG,"gotoActivity :" + classToGoAfter);
//                gotoActivity();
//            }
//        });

    }

    private void gotoActivity() {
        Intent intent = new Intent(mContext, classToGoAfter);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityClass(Class activityClassToGo) {
        Log.e(TAG,"onActivityClass :" + activityClassToGo);
        classToGoAfter = activityClassToGo;
    }
}