package com.link2me.android.retrofit_mvvm_login.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.Utils;
import com.link2me.android.retrofit_mvvm_login.databinding.ActivityLoginBinding;
import com.link2me.android.retrofit_mvvm_login.model.LoginResult;
import com.link2me.android.retrofit_mvvm_login.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private ActivityLoginBinding binding;

    private BackPressHandler backPressHandler;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        backPressHandler = new BackPressHandler(this);
        mContext = LoginActivity.this;

        Utils.viewShowHide(binding.btnLogin, false);
        initView();
    }

    private void initView() {
        // viewModel init
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // viewModel observe
        viewModel.getLoginResultLiveData().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if(loginResult.getStatus().contains("success")){
                    Utils.goNextActivity(mContext,MainActivity.class);
                    finish();
                } else {
                    Utils.showAlert(mContext, loginResult.getStatus(), loginResult.getMessage());
                }
            }
        });

        // addTextChangedListener
        binding.etPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userID = binding.etId.getText().toString().trim();
                String password = binding.etPw.getText().toString().trim();
                if (!userID.isEmpty() && !password.isEmpty())
                    Utils.viewShowHide(binding.btnLogin, true);
                // userID 와 패스워드 모두 입력하면 로그인 버튼이 활성화된다.
            }
        });

        // button setOnClickListener
        binding.btnLogin.setOnClickListener(view -> {
            login();
        });

    }

    private void login() {
        String userID = binding.etId.getText().toString().trim();
        String password = binding.etPw.getText().toString().trim();
        String uID = Utils.getDeviceId(mContext); // 스마트폰 고유장치번호
        String mfoneNO = Utils.getPhoneNumber(mContext); // 스마트폰 전화번호
        viewModel.login(userID, password,uID,mfoneNO);
        /*
        로그인 전달 변수 수정 시
        AutoViewModel.java 와 AutoRepository.java 파일 모두 수정
        */
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}