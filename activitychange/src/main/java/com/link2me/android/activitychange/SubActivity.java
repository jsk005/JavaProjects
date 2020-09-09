package com.link2me.android.activitychange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    String ClassName;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        mContext = SubActivity.this;
        ClassName = this.getLocalClassName();

        initView();
    }

    private void initView() {
        textView = findViewById(R.id.subtextView);
        textView.setText("SubActivity UI");

        Button btn2Main = findViewById(R.id.btntoMain);
        Button btn2Target = findViewById(R.id.btntoTarget);

        btn2Main.setOnClickListener(this);
        btn2Target.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btntoMain:
                // 일반적인 Activity 간 이동
                Intent mainintent = new Intent(SubActivity.this,MainActivity.class);
                startActivity(mainintent);
                finish();
                break;

            case R.id.btntoTarget:
                // 일반적인 Activity 간 이동
                Intent intentTarget = new Intent(mContext, TargetActivity.class);
                intentTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentTarget.putExtra("classname",ClassName);
                startActivity(intentTarget);
                break;
        }
    }
}