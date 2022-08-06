package com.link2me.android.mvvm_basic.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.link2me.android.mvvm_basic.R;
import com.link2me.android.mvvm_basic.databinding.ActivityMainBinding;
import com.link2me.android.mvvm_basic.viewmodel.CounterViewModel;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ActivityMainBinding binding;
    private CounterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = MainActivity.this;
        initView();
    }

    private void initView() {
        // Get the ViewModel
        viewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        // Create the observer which updates the UI.
        viewModel.counter().observe(this, integer -> {
            // UI Update
            binding.tvMain.setText(String.valueOf(integer));
        });

        binding.btnMinus.setOnClickListener(view -> {
            viewModel.decrement();
        });

        binding.btnPlus.setOnClickListener(view -> {
            viewModel.increment();
        });
    }


}