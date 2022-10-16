package com.link2me.android.room_mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.link2me.android.room_mvvm.model.Contact_Item;
import com.link2me.android.room_mvvm.repository.SQLiteRepository;

import java.util.List;

public class SQLiteViewModel extends AndroidViewModel {
    private SQLiteRepository repository;
    private LiveData<List<Contact_Item>> allContactInfo;

    public SQLiteViewModel(@NonNull Application application) {
        super(application);
        repository = new SQLiteRepository(application);
        allContactInfo = repository.getAllContactInfo();
    }

    public void insertAll(List<Contact_Item> items){
        repository.insertAll(items);
    }

    public void update(Contact_Item item){
        repository.update(item);
    }

    public void delete(Contact_Item item){
        repository.delete(item);
    }

    public void deleteByIdx(int idx){
        repository.deleteByIdx(idx);
    }

    public void deleteAllContactInfo(){
        repository.deleteAll();
    }

    public LiveData<List<Contact_Item>> getAllContactInfo() {
        return allContactInfo;
    }


}
