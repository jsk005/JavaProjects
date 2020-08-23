package com.link2me.android.activitychange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.link2me.android.common.BackPressHandler;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    String ClassName;

    TextView textView;
    Button btn2Target;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this);
        ClassName = this.getLocalClassName();

        textView = findViewById(R.id.maintextView);
        textView.setText("메인화면");

        btn2Target = findViewById(R.id.mainbutton);
        btn2Target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,TargetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("classname",ClassName);
                startActivity(intent);
            }
        });

        Button btn2Sub = findViewById(R.id.btntoSub);
        btn2Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}
