package com.link2me.android.recyclerview.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.common.APIRequest;
import com.link2me.android.common.Utils;
import com.link2me.android.recyclerview.HangulUtils;
import com.link2me.android.recyclerview.R;
import com.link2me.android.recyclerview.Value;
import com.link2me.android.recyclerview.adapter.FragmentRecyclerViewAdapter;
import com.link2me.android.recyclerview.model.AddressResult;
import com.link2me.android.recyclerview.model.Address_Item;
import com.link2me.android.recyclerview.model.RemoteService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewFragment extends Fragment implements FragmentRecyclerViewAdapter.OnItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private FragmentActivity mContext;

    View rootview;
    ImageButton btn_cancel;
    ImageButton btn_send;

    private ArrayList<Address_Item> addressItemList = new ArrayList<>(); // 서버 전체 데이터 리스트
    private ArrayList<Address_Item> searchItemList = new ArrayList<>(); // 검색한 데이터 리스트
    private RecyclerView mRecyclerView;
    private FragmentRecyclerViewAdapter mAdapter;
    private SearchView editsearch;

    RemoteService remoteService;

    public static ConstraintLayout fragconstraintLayout;
    public static boolean isCheckFlag = false;
    public CheckBox checkAll;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RecyclerViewFragment() {
    }

    public static RecyclerViewFragment newInstance(String param1, String param2) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) { // Fragment 가 Activity에 attach 될 때 호출된다.
        super.onAttach(activity);
        mContext =(FragmentActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the Recycler View adapter
        mRecyclerView = rootview.findViewById(R.id.address_listview_rcv);
        fragconstraintLayout = rootview.findViewById(R.id.listview_fragment2);
        editsearch = rootview.findViewById(R.id.search_rcv);
        checkAll = rootview.findViewById(R.id.lv_checkbox_all_rcv);
        btn_cancel = rootview.findViewById(R.id.btn_cancel_rcv);
        btn_send = rootview.findViewById(R.id.btn_send_rcv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    getActivity().runOnUiThread(() -> {
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

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAdapter = new FragmentRecyclerViewAdapter(mContext,searchItemList); // 객체 생성

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChoiceClickListener(this);
    }

    private void setButtons(){
        isCheckFlag = false;

        // Locate the EditText in listview_main.xml
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
            fragconstraintLayout.setVisibility(View.GONE);
        } else if (isCheckFlag == true) {
            fragconstraintLayout.setVisibility(View.VISIBLE);
        }

        // all checkbox
        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkAll.isChecked() == true) {
                mAdapter.selectAll();
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.unselectall();
                mAdapter.notifyDataSetChanged();
            }
        });

        btn_cancel.setOnClickListener(v -> {
            isCheckFlag = false;
            fragconstraintLayout.setVisibility(View.GONE);
            mAdapter.unselectall();
            checkAll.setChecked(false); // 전체 선택 체크박스 해제
            mAdapter.notifyDataSetChanged();
        });

        final String[] listview_items = {"그룹문자 보내기", "연락처 저장"};
        final AlertDialog.Builder items_builder = new AlertDialog.Builder(mContext);
        items_builder.setTitle("해당 작업을 선택하세요");
        items_builder.setItems(listview_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getActivity(), "그룹문자 보내기 구현하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "연락처 저장을 구현하세요.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        items_builder.create();
        btn_send.setOnClickListener(v -> items_builder.show());
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
    public void onItemClicked(View view, Address_Item item, int position) {
        Toast.makeText(mContext, "position : "+position + " clieck item name : "+item.getUserNM(), Toast.LENGTH_SHORT).show();
    }
}