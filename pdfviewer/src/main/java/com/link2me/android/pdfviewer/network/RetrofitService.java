package com.link2me.android.pdfviewer.network;

import com.link2me.android.pdfviewer.model.PdfResult;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {
    // POST 방식으로 데이터를 주고 받을 때 넘기는 변수는 Field 라고 해야 한다.

    @FormUrlEncoded
    @POST(RetrofitUrl.PdfData)
    Call<PdfResult> getPdfData(
            @Field("keyword") String keyword,
            @Field("search") String search
    );

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFile(@Url String fileUrl);

}
