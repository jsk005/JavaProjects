package com.link2me.android.retrofit_mvvm_login.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.link2me.android.retrofit_mvvm_login.model.LoginResult;
import com.link2me.android.retrofit_mvvm_login.network.RetrofitAdapter;
import com.link2me.android.retrofit_mvvm_login.network.RetrofitService;
import com.link2me.android.retrofit_mvvm_login.network.RetrofitURL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private RetrofitService loginService;
    private MutableLiveData<LoginResult> loginResultLiveData;

    public AuthRepository() {
        loginResultLiveData = new MutableLiveData<>();
        loginService = RetrofitAdapter.getClient(RetrofitURL.BaseUrl).create(RetrofitService.class);
    }

    public void loginRepo(String userID, String password, String uID, String mfoneNO){
        loginService.Login(userID,password,uID,mfoneNO)
                .enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        loginResultLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        loginResultLiveData.postValue(null);
                    }
                });
    }

    public LiveData<LoginResult> getLoginResultLiveData() {
        return loginResultLiveData;
    }
}
