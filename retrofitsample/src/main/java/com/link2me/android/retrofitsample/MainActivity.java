package com.link2me.android.retrofitsample;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.link2me.android.common.BackPressHandler;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this);
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed();
    }

}