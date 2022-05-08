package com.link2me.android.retrofitsample.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.link2me.android.retrofitsample.MainActivity;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;
import com.link2me.android.retrofitsample.R;
import com.link2me.android.retrofitsample.model.LoginResult;
import com.link2me.android.retrofitsample.network.RetrofitAPI;
import com.link2me.android.retrofitsample.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    EditText etId;
    EditText etPw;
    String userID;
    String userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        etId = findViewById(R.id.et_id);
        etPw = findViewById(R.id.et_pw);
        Button btn_login = findViewById(R.id.btn_login);

        etId.setText(PrefsHelper.read("userid",""));
        if(!PrefsHelper.read("userid","").equals("")){
            etPw.requestFocus();
        }

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userID = etId.getText().toString().trim();
        userPW = etPw.getText().toString().trim();
        if(userID != null && !userID.isEmpty() && userPW != null && !userPW.isEmpty()){
            String uID = Utils.getDeviceId(mContext); // 스마트폰 고유장치번호
            String mfoneNO = Utils.getPhoneNumber(mContext); // 스마트폰 전화번호

            RetrofitAPI mloginService = RetrofitService.getClient().create(RetrofitAPI.class);
            mloginService.Login(userID,userPW,uID,mfoneNO)
                    .enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {
                            LoginResult result = response.body();
                            if(result.getStatus().contains("success")){
                                PrefsHelper.write("userid",userID);
                                PrefsHelper.write("userpw",userPW);
                                PrefsHelper.write("userNM",result.getUserinfo().getUserNM());
                                PrefsHelper.write("mobileNO",result.getUserinfo().getMobileNO());
                                PrefsHelper.write("profileImg",result.getUserinfo().getProfileImg());

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Utils.showAlert(mContext, result.getStatus(), result.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {

                        }
                    });
        }
    }
}