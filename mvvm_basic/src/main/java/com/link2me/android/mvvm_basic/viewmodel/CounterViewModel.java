package com.link2me.android.mvvm_basic.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {
    private final String TAG = this.getClass().getSimpleName();

    // LiveData는 관찰 가능한 데이터 홀더 클래스
    private MutableLiveData<Integer> _liveData;

    public CounterViewModel() {
    }

    public LiveData<Integer> counter() {
        if(_liveData == null){
            _liveData = new MutableLiveData<>();
            _liveData.setValue(0);
        }
        return _liveData;
    }

    public void increment(){
        _liveData.setValue(_liveData.getValue() + 1);
    }

    public void decrement() {
        _liveData.setValue(_liveData.getValue() -1);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG,"CounterViewModel destroyed");
    }
}
