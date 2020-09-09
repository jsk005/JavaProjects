package com.link2me.android.activitychange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        TextView textView = findViewById(R.id.textView);
        textView.setText("이곳은 Activity Changer를 통해 이동한 곳입니다.");
    }
}