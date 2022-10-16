package com.link2me.android.room_mvvm.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.HangulUtils;
import com.link2me.android.common.Utils;
import com.link2me.android.room_mvvm.databinding.ActivityMainBinding;
import com.link2me.android.room_mvvm.model.AddressResult;
import com.link2me.android.room_mvvm.model.Address_Item;
import com.link2me.android.room_mvvm.model.Contact_Item;
import com.link2me.android.room_mvvm.view.adapter.AddressListAdapter;
import com.link2me.android.room_mvvm.viewmodel.AddressViewModel;
import com.link2me.android.room_mvvm.viewmodel.SQLiteViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddressListAdapter.OnItemClickListener  {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private ActivityMainBinding binding;
    private AddressViewModel viewModel;
    private SQLiteViewModel sqLiteViewModel;

    private BackPressHandler backPressHandler;

    private ArrayList<Address_Item> addressItemList = new ArrayList<>(); // 서버 전체 데이터 리스트
    private ArrayList<Address_Item> searchItemList = new ArrayList<>(); // 검색한 데이터 리스트
    private RecyclerView mRecyclerView;
    private AddressListAdapter mAdapter;

    public static ConstraintLayout constraintLayout;
    public static boolean isCheckFlag = false;
    public CheckBox checkAll;

    private Contact_Item contactItem;
    private List<Contact_Item> contactItemList = new ArrayList<>();
    HashMap<Integer, Integer> idxCheck = new HashMap<>();
    HashMap<Integer, Integer> localIdxChk = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this);

        initView();
    }

    private void initView() {
        viewModelProvider();
        buildRecyclerView();
        setButtons();
    }

    private void buildRecyclerView() {
        mRecyclerView = binding.rvAddress;
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        //mAdapter = new AddressViewAdapter(mContext,searchItemList); // 객체 생성
        mAdapter = new AddressListAdapter(mContext); // DiffUtil을 넣은 어댑터를 생성
        //mAdapter.submitList(searchItemList); // 서버에서 데이터를 가져온 이 후 코드에 추가하면 된다.

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void viewModelProvider() {
        // viewModel init
        sqLiteViewModel = new ViewModelProvider(this).get(SQLiteViewModel.class);
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        sqLiteViewModel.getAllContactInfo().observe(this, new Observer<List<Contact_Item>>() {
            @Override
            public void onChanged(List<Contact_Item> address_items) {
                Log.e(TAG, "Contact_Item size : " + address_items.size());
                for(Contact_Item item : address_items){
                    // 로컬에 있고 서버에 없는 자료 제거 목적. 로컬에서 가져올 때는 전부 0으로 간주한다.
                    idxCheck.put(item.getIdx(),0);
                    String items = item.getIdx()+" : "+item.getUserNM()+","+item.getMobileNO()+","+item.getTelNO()+","+item.getTeam()+","+ item.getMission()+","+ item.getPosition()+","+ item.getPhoto();
                    Log.e(TAG,items);
                }
                Log.e(TAG, "sqLiteViewModel observe");
            }
        });

        viewModel.get_liveData().observe(this, new Observer<AddressResult>() {
            @Override
            public void onChanged(AddressResult addressResult) {
                if(addressResult.getStatus().contains("success")){
                    addressItemList.clear(); // 서버에서 가져온 데이터 초기화 (전체 List)
                    searchItemList.clear(); // 서버에서 가져온 데이터 초기화 (검색 List)

                    addressItemList.addAll(addressResult.getAddrinfo());
                    searchItemList.addAll(addressResult.getAddrinfo());

                    mAdapter.submitList(searchItemList);

                    for(Address_Item item : addressResult.getAddrinfo()){
                        String flag = item.getCheckBoxState() == true ? "1":"0";
                        contactItem= new Contact_Item(item.getIdx(),item.getUserNM(),item.getMobileNO(), item.getTelNO(), item.getTeam(), item.getMission(), item.getPosition(), item.getPhoto(),flag);
                        contactItemList.add(contactItem);

                        // 서버에서 가져온 데이터의 idx 체크
                        idxCheck.put(item.getIdx(),1);
                    }

                    // 서버에서 가져온 데이터를 먼저 SQLite 에 저장하고 나서
                    sqLiteViewModel.insertAll(contactItemList);
                    // SQLite DB 서버와 동기화 처리
                    setSQLiteDB(); // sqLiteViewModel.getAllContactInfo().observe 에서 이 코드를 실행하면 에러번 불필요한 쿼리가 발생함을 확인할 수 있다.
                    Log.e(TAG, "viewModel observe");
                } else {
                    Utils.showAlert(mContext,addressResult.getStatus(),addressResult.getMessage());
                }
            }
        });

        getAddressList(); // 서버 데이터 가져오기
    }

    private void getAddressList() {
        String search = "";
        viewModel.getAllAddressData(search);
    }

    private void setSQLiteDB(){
        // 서버에서 가져온 신규 데이터를 포함해서 무조건 저장하므로 DB 테이블의 row 수는 더 많다. 적을 수는 없다.
        for(Map.Entry<Integer, Integer> entry : idxCheck.entrySet()){
            if(entry.getValue().equals(0)){
                System.out.println("Key : " + entry.getKey() + ", Value : " + entry.getValue());
                sqLiteViewModel.deleteByIdx(entry.getKey()); // 서버에 없는 데이터는 삭제 처리
            }
        }
    }

    private void setSQLiteDB_Test(){
        for(Integer key : idxCheck.keySet()){
            localIdxChk.remove(key); // 서버에 있는 키는 모두 삭제 처리
        }

        for(Integer key : localIdxChk.keySet()){
            System.out.println(key);
            sqLiteViewModel.deleteByIdx(key);
        }
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
                return true;
            }
        });

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

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed();
    }
}