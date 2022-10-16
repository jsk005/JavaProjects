package com.link2me.android.room_mvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.link2me.android.room_mvvm.model.AddressResult;
import com.link2me.android.room_mvvm.repository.AddressRepository;


public class AddressViewModel extends ViewModel {
    private AddressRepository repository;
    private LiveData<AddressResult> _liveData;

    public AddressViewModel() {
    }

    public void getAllAddressData(String search){
        repository.addressDataRepo(search);
    }

    public LiveData<AddressResult> get_liveData(){
        if(_liveData == null){
            repository = new AddressRepository();
            _liveData = repository.getAddressResultLiveData();
        }
        return _liveData;
    }
}
