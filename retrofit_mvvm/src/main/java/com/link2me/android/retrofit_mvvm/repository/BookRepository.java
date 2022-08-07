package com.link2me.android.retrofit_mvvm.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.link2me.android.retrofit_mvvm.model.VolumesResponse;
import com.link2me.android.retrofit_mvvm.network.BookSearchService;
import com.link2me.android.retrofit_mvvm.network.RetrofitAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String BOOK_SEARCH_SERVICE_BASE_URL = "https://www.googleapis.com/";

    private BookSearchService bookSearchService;
    private MutableLiveData<VolumesResponse> volumesResponseLiveData;

    public BookRepository() {
        volumesResponseLiveData = new MutableLiveData<>();

        bookSearchService = RetrofitAdapter.getClient(BOOK_SEARCH_SERVICE_BASE_URL)
                .create(BookSearchService.class);
    }

    public void searchVolumes(String keyword, String author) {
        bookSearchService.searchVolumes(keyword, author)
                .enqueue(new Callback<VolumesResponse>() {
                    @Override
                    public void onResponse(Call<VolumesResponse> call, Response<VolumesResponse> response) {
                        if(response.body() != null){
                            volumesResponseLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<VolumesResponse> call, Throwable t) {
                        volumesResponseLiveData.postValue(null);
                    }
                });
    }

    public LiveData<VolumesResponse> getVolumesResponseLiveData() {
        return volumesResponseLiveData;
    }


}
