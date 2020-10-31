package com.link2me.android.json;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    String mInfo = "[{'name':'홍길동','age':22,'address':'강원'},"
            + "{'name':'이순신','age':34,'address':'율돌목'},"
            + "{'name':'양만춘','age':56,'address':'안시성'}]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view);
        mTextView.setText(mInfo);

        findViewById(R.id.btn).setOnClickListener(v -> JSONParse(mInfo));

    }

    private void JSONParse(String jsonStr) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                int age = jsonObject.getInt("age");
                String address = jsonObject.getString("address");
                builder.append("이름 : ").append(name)
                        .append(" 나이 : ").append(age)
                        .append(" 주소 : ").append(address)
                        .append("\n");
            }
            mTextView.setText(builder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}