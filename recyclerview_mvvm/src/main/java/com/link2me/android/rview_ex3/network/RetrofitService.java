package com.link2me.android.rview_ex3.network;

import com.link2me.android.rview_ex3.model.AddressResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    @FormUrlEncoded
    @POST(RetrofitURL.AddressData)
    Call<AddressResult> GetAddrData(
            @Field("keyword") String keyword,
            @Field("search") String search
    );

}
