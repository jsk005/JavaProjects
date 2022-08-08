package com.link2me.android.retrofit_mvvm_login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.link2me.android.retrofit_mvvm_login.model.LoginResult;
import com.link2me.android.retrofit_mvvm_login.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private AuthRepository repository;
    private LiveData<LoginResult> _liveData;

    public AuthViewModel() {
    }

    public void login(String userID, String password, String uID, String mfoneNO) {
        repository.loginRepo(userID, password, uID, mfoneNO);
    }

    public LiveData<LoginResult> getLoginResultLiveData() {
        if (_liveData == null) {
            repository = new AuthRepository();
            _liveData = repository.getLoginResultLiveData();
        }
        return _liveData;
    }

}
