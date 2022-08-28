package com.link2me.android.rview_ex3.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.common.HangulUtils;
import com.link2me.android.common.Utils;
import com.link2me.android.rview_ex3.databinding.ActivityAddressBinding;
import com.link2me.android.rview_ex3.model.AddressResult;
import com.link2me.android.rview_ex3.model.Address_Item;
import com.link2me.android.rview_ex3.view.adapter.AddressListAdapter;
import com.link2me.android.rview_ex3.viewmodel.AddressViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements AddressListAdapter.OnItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private ActivityAddressBinding binding;

    private AddressViewModel viewModel;

    private ArrayList<Address_Item> addressItemList = new ArrayList<>(); // 서버 전체 데이터 리스트
    private ArrayList<Address_Item> searchItemList = new ArrayList<>(); // 검색한 데이터 리스트
    private RecyclerView mRecyclerView;
    private AddressListAdapter mAdapter;

    public static ConstraintLayout constraintLayout;
    public static boolean isCheckFlag = false;
    public CheckBox checkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_address);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = AddressActivity.this;
        initView();
    }

    private void initView() {
        // 동기식 데이터 처리에서는 순서가 중요함.
        viewModelProvider();
        buildRecyclerView();
        getAddressList(); // 서버 데이터 가져오기
        setButtons();
    }

    private void buildRecyclerView() {
        mRecyclerView = binding.rvAddress;
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        //mAdapter = new AddressViewAdapter(mContext,searchItemList); // 객체 생성
        mAdapter = new AddressListAdapter(mContext, Address_Item.itemCallback); // DiffUtil을 넣은 어댑터를 생성
        //mAdapter.submitList(searchItemList); // 서버에서 데이터를 가져온 이 후 코드에 추가하면 된다.

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void viewModelProvider() {
        // viewModel init
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        // viewModel observe
        viewModel.get_liveData().observe(this, new Observer<AddressResult>() {
            @Override
            public void onChanged(AddressResult addressResult) {
                if(addressResult.getStatus().contains("success")){
                    addressItemList.clear(); // 서버에서 가져온 데이터 초기화 (전체 List)
                    searchItemList.clear(); // 서버에서 가져온 데이터 초기화 (검색 List)

                    for(Address_Item item : addressResult.getAddrinfo()){
                        addressItemList.add(item);
                        searchItemList.add(item);
                    }

                    Log.e(TAG, "viewModel observe");

                    mAdapter.submitList(searchItemList);

                } else {
                    Utils.showAlert(mContext,addressResult.getStatus(),addressResult.getMessage());
                }
            }
        });

    }

    private void getAddressList() {
        String search = "";
        viewModel.getAllAddressData(search);
    }

    private void setButtons(){
        isCheckFlag = false;
        constraintLayout = binding.listviewConstraint2;

        binding.search.setSubmitButtonEnabled(false);
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 문자열 입력을 완료했을 때 문자열 반환
                // 쿼리가 비어있을 때에는 호출되지 않는다.
                //viewModel.getAllAddressData(query); // 서버 재접속 후 데이터 가져오기
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 문자열이 변할 때마다 바로바로 문자열 반환
                filter(newText); // 전체 데이터 가져온 후 로컬에서 검색 처리
//                if(newText.trim().length() == 0) {
//                    viewModel.getAllAddressData("");
//                }
                return true;
            }
        });

//        binding.search.setOnCloseListener(() -> {
//            binding.search.clearFocus();
//            Utils.hideKeyboard(mContext,binding.getRoot()); // 키보드 자동 숨기기
//            return true;
//        });


        if (isCheckFlag == false) {
            constraintLayout.setVisibility(View.GONE);
        } else if (isCheckFlag == true) {
            constraintLayout.setVisibility(View.VISIBLE);
        }

        // all checkbox
        checkAll = binding.lvCheckboxAll;
        binding.lvCheckboxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkAll.isChecked() == true) {
                mAdapter.selectAll(searchItemList.size());
            } else {
                mAdapter.unselectall(searchItemList.size());
            }
        });

        binding.btnCancel.setOnClickListener(v -> {
            isCheckFlag = false;
            constraintLayout.setVisibility(View.GONE);
            mAdapter.unselectall(searchItemList.size());
            checkAll.setChecked(false); // 전체 선택 체크박스 해제
        });
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchItemList.clear();
        if (charText.length() == 0) {
            searchItemList.addAll(addressItemList);
        } else {
            for (Address_Item wp : addressItemList) {
                if(Utils.isNumber(charText)){ // 숫자여부 체크
                    if(wp.getMobileNO().contains(charText) || wp.getTelNO().contains(charText)){
                        // 휴대폰번호 또는 사무실번호에 숫자가 포함되어 있으면
                        searchItemList.add(wp);
                    }
                } else {
                    String iniName = HangulUtils.getHangulInitialSound(wp.getUserNM(), charText);
                    if (iniName.indexOf(charText) >= 0) { // 초성검색어가 있으면 해당 데이터 리스트에 추가
                        searchItemList.add(wp);
                    } else if (wp.getUserNM().toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchItemList.add(wp);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        //mAdapter.submitList(searchItemList);
    }

    @Override
    public void onItemClicked(Address_Item item, int position) {
        Toast.makeText(mContext, "position : "+position + " clieck item name : "+item.getUserNM(), Toast.LENGTH_SHORT).show();
    }
}