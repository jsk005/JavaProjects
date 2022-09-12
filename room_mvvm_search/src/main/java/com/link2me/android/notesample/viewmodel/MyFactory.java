package com.link2me.android.notesample.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyFactory implements ViewModelProvider.Factory {
    private Application mApplication;


    public MyFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new NoteViewModel(mApplication);
    }
}
