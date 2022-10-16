package com.link2me.android.room_mvvm.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.link2me.android.common.CipherUtils;
import com.link2me.android.room_mvvm.model.AddressResult;
import com.link2me.android.room_mvvm.network.RetrofitAdapter;
import com.link2me.android.room_mvvm.network.RetrofitService;
import com.link2me.android.room_mvvm.network.RetrofitURL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    private RetrofitService service;
    private MutableLiveData<AddressResult> mutableLiveData;

    public AddressRepository() {
        mutableLiveData = new MutableLiveData<>();
        service = RetrofitAdapter.getClient(RetrofitURL.BaseUrl).create(RetrofitService.class);
    }

    public void addressDataRepo(String search) {
        String keyword = CipherUtils.encryptAES(CipherUtils.URLkey());
        service.GetAddrData(keyword, search).enqueue(new Callback<AddressResult>() {
            @Override
            public void onResponse(Call<AddressResult> call, Response<AddressResult> response) {
                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<AddressResult> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<AddressResult> getAddressResultLiveData(){
        return mutableLiveData;
    }
}
