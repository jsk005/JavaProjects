package com.link2me.android.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.common.APIRequest;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.Utils;
import com.link2me.android.recyclerview.adapter.RecyclerViewAdapter;
import com.link2me.android.recyclerview.model.AddressResult;
import com.link2me.android.recyclerview.model.Address_Item;
import com.link2me.android.recyclerview.model.RemoteService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ArrayList<Address_Item> addressItemList = new ArrayList<>(); // 서버 전체 데이터 리스트
    private ArrayList<Address_Item> searchItemList = new ArrayList<>(); // 검색한 데이터 리스트
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private SearchView editsearch;

    RemoteService remoteService;

    public static ConstraintLayout constraintLayout;
    public static boolean isCheckFlag = false;
    public CheckBox checkAll;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this);

        initView();
    }

    private void initView() {
        createAddressList(); // 서버 데이터 가져오기
        buildRecyclerView();
        setButtons();
    }

    private void createAddressList() {
        String keyword = Value.encrypt(Value.URLkey());
        remoteService = APIRequest.getClient().create(RemoteService.class);
        Call<AddressResult> call = remoteService.GetAddrData(keyword,"");
        call.enqueue(new Callback<AddressResult>() {
            @Override
            public void onResponse(Call<AddressResult> call, Response<AddressResult> response) {
                if(response.body().getStatus().contains("success")){
                    addressItemList.clear(); // 서버에서 가져온 데이터 초기화
                    searchItemList.clear(); // 서버에서 가져온 데이터 초기화

                    for(Address_Item item : response.body().getAddrinfo()){
                        addressItemList.add(item);
                        searchItemList.add(item);
                    }
                    // runOnUiThread()를 호출하여 실시간 갱신한다.
                    runOnUiThread(() -> {
                        // 갱신된 데이터 내역을 어댑터에 알려줌
                        mAdapter.notifyDataSetChanged();
                    });
                } else {
                    Utils.showAlert(mContext,response.body().getStatus(),response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddressResult> call, Throwable t) {

            }
        });

    }

    private void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.address_listview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAdapter = new RecyclerViewAdapter(mContext,searchItemList); // 객체 생성

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemSelectClickListener(this);
    }

    private void setButtons(){
        isCheckFlag = false;
        constraintLayout = findViewById(R.id.listview_constraint2);

        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 문자열 입력을 완료했을 때 문자열 반환
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 문자열이 변할 때마다 바로바로 문자열 반환
                String text = newText;
                filter(text);
                return true;
            }
        });

        if (isCheckFlag == false) {
            constraintLayout.setVisibility(View.GONE);
        } else if (isCheckFlag == true) {
            constraintLayout.setVisibility(View.VISIBLE);
        }

        // all checkbox
        checkAll = findViewById(R.id.lv_checkbox_all);
        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkAll.isChecked() == true) {
                mAdapter.selectAll();
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.unselectall();
                mAdapter.notifyDataSetChanged();
            }
        });

        Button cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> {
            isCheckFlag = false;
            constraintLayout.setVisibility(View.GONE);
            mAdapter.unselectall();
            checkAll.setChecked(false); // 전체 선택 체크박스 해제
            mAdapter.notifyDataSetChanged();
        });

        Button send = findViewById(R.id.btn_send);
        final String[] listview_items = {"그룹문자 보내기", "연락처 저장"};
        final AlertDialog.Builder items_builder = new AlertDialog.Builder(MainActivity.this);
        items_builder.setTitle("해당 작업을 선택하세요");
        items_builder.setItems(listview_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "그룹문자 보내기 구현하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "연락처 저장을 구현하세요.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        items_builder.create();

        send.setOnClickListener(v ->

                items_builder.show());

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
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed();
    }

    @Override
    public void onItemClicked(View view, Address_Item item, int position) {
        Toast.makeText(mContext, "position : "+position + " clieck item name : "+item.getUserNM(), Toast.LENGTH_SHORT).show();
    }
}