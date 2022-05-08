package com.link2me.android.retrofitsample.network;

import com.link2me.android.retrofitsample.model.LoginResult;
import com.link2me.android.retrofitsample.model.PasswdResult;
import com.link2me.android.retrofitsample.model.VersionResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPI {
    // POST 방식으로 데이터를 주고 받을 때 넘기는 변수는 Field 라고 해야 한다.
    @FormUrlEncoded
    @POST(RetrofitURL.URL_LOGIN)
    Call<LoginResult> Login(
            @Field("userID") String userID,
            @Field("password") String userPW,
            @Field("uID") String uID,
            @Field("mfoneNO") String mfoneNO
    );

    @FormUrlEncoded
    @POST(RetrofitURL.URL_Verson)
    Call<VersionResult> lastVersion(
            @Field("ostype") String ostype
    );

    @FormUrlEncoded
    @POST(RetrofitURL.URL_Password)
    Call<PasswdResult> PasswdUpdate(
            @Field("userID") String userID,
            @Field("curpw") String curPW,
            @Field("newpw") String newPW,
            @Field("keyword") String keyword
            // 서버와 Android 간에 key 를 사용하여 일치할 경우에만 업데이트 처리
    );


}
