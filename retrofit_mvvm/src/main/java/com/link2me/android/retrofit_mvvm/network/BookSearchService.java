package com.link2me.android.retrofit_mvvm.network;

import com.link2me.android.retrofit_mvvm.model.VolumesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookSearchService {
    // https://www.googleapis.com/books/v1/volumes?q=harry&inauthor=rowling

    @GET("/books/v1/volumes")
    Call<VolumesResponse> searchVolumes(
            @Query("q") String query,
            @Query("inauthor") String author
    );
}
