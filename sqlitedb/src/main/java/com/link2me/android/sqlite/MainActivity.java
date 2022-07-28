package com.link2me.android.sqlite;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.HangulUtils;
import com.link2me.android.common.Utils;
import com.link2me.android.sqlite.localdb.DBContract;
import com.link2me.android.sqlite.localdb.DbFacade;
import com.link2me.android.sqlite.model.Address_Item;
import com.link2me.android.sqlite.model.SQLite_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    SearchView editsearch;
    private ListView listView; // 리스트뷰
    private ListViewAdapter listViewAdapter = null; // 리스트뷰에 사용되는 ListViewAdapter

    private ArrayList<Address_Item> addressItemList = new ArrayList<>(); // 서버에서 가져온 원본 데이터 리스트
    private ArrayList<Address_Item> searchItemList = new ArrayList<>(); // 검색한 데이터 리스트

    private BackPressHandler backPressHandler;

    private DbFacade mFacade;
    HashMap<String, SQLite_Item> sqliteDBMap = new HashMap<String, SQLite_Item>();
    ArrayList<String> aryIdx = new ArrayList<String>();
    ArrayList<String> aryName = new ArrayList<String>();
    ArrayList<Integer> aryCheck = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this); // 뒤로 가기 버튼 이벤트

        mFacade = new DbFacade(mContext);

        SQLiteDB2ArrayList();
        initView();
    }

    private void initView() {
        // Adapter에 추가 데이터를 저장하기 위한 ArrayList
        getServerData(); // 서버 데이터 가져오기

        listView = (ListView) findViewById(R.id.my_listView);
        listViewAdapter = new ListViewAdapter(mContext, searchItemList); // Adapter 생성
        listView.setAdapter(listViewAdapter); // 어댑터를 리스트뷰에 세팅
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 문자열 입력을 완료했을 때 문자열 반환
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 문자열이 변할 때마다 바로바로 문자열 반환
                String text = newText;
                listViewAdapter.filter(text);
                return false;
            }
        });
    }

    private void getServerData() {

        Response.Listener<String> responseListener = response -> {
            int newcnt = 0;
            int upcnt = 0;
            int no_cnt = 0;
            JSONArray peoples = null;

            try {
                JSONObject jsonObj = new JSONObject(response);
                peoples = jsonObj.getJSONArray(DBContract._RESULTS);

                addressItemList.clear(); // 서버에서 가져온 데이터 초기화
                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    final String idx = c.getString(DBContract.Entry._IDX);
                    final String name = c.getString(DBContract.Entry._NAME);
                    final String mobileNO = c.getString(DBContract.Entry._MobileNO);
                    final String officeNO = c.getString(DBContract.Entry._telNO);
                    final String Team = "";
                    final String Mission = "";
                    final String Position = "";
                    String PhotoImage = c.getString(DBContract.Entry._Photo);
                    final String Status = "1";

                    Log.e(TAG,"name : " + name);

                    // 서버에서 가져온 데이터 저장
                    getServerDataList(idx, name, mobileNO, officeNO, PhotoImage, false);
                    selectDataList(idx, name, mobileNO, officeNO, PhotoImage, false);

                    if (sqliteDBMap.containsKey(idx)) {
                        SQLite_Item item = sqliteDBMap.get(idx);
                        String sqLiteIdx = item.getIdx();
                        String sqLiteName = item.getUserNM();
                        String sqLiteMobileNO = item.getMobileNO();
                        String sqLiteOfficeNO = item.getTelNO();
                        String sqLiteTeam = item.getTeam();
                        String sqLiteMission = item.getMission();
                        String sqLitePosition = item.getPosition();

                        aryCheck.set(aryIdx.indexOf(idx), 1); // 서버에 있는 자료이면 1로 세팅

                        if (sqLiteIdx.equals(idx) && sqLiteName.equals(name) && sqLiteMobileNO.equals(mobileNO)
                                && sqLiteOfficeNO.equalsIgnoreCase(officeNO) && sqLiteTeam.equalsIgnoreCase(Team)
                                && sqLiteMission.equalsIgnoreCase(Mission) && sqLitePosition.equalsIgnoreCase(Position)) {
                            no_cnt++;
                        } else {
                            // 데이터가 틀린 부분이 있으면 수정하라.
                            upcnt++;
                            mFacade.updateDB(idx, name, mobileNO, officeNO, Team, Mission, Position, PhotoImage, Status);
                        }

                    } else { // 데이터가 없다면
                        newcnt++;
                        mFacade.InsertData(idx, name, mobileNO, officeNO, Team, Mission, Position, PhotoImage, Status);
                    }
                }

                Log.e(TAG,"No Change : "+no_cnt + ", Update : " + upcnt + ", New Insert : " + newcnt);

                // 화면에 반영하기 위하여 runOnUiThread()를 호출하여 실시간 갱신한다.
                runOnUiThread(() -> {
                    listViewAdapter.notifyDataSetChanged();// 갱신된 데이터 내역을 어댑터에 알려줌
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
        ContactRequest request = new ContactRequest(mContext, "1", responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext); // RequestQueue 생성 및 초기화
        requestQueue.add(request); // 생성한 StringRequest를 RequestQueue에 추가
    }

    public void SQLiteDB2ArrayList() {
        sqliteDBMap.clear(); // 메모리 초기화
        aryIdx.clear();
        aryCheck.clear();

        Cursor cursor = mFacade.LoadSQLiteDBCursor();
        try {
            cursor.moveToFirst();
            System.out.println("SQLiteDB 개수 = " + cursor.getCount());
            while (!cursor.isAfterLast()) {
                SQLite_Item item = new SQLite_Item();
                item.setIdx(cursor.getString(0));
                item.setUserNM(cursor.getString(1));
                item.setMobileNO(cursor.getString(2));
                item.setTelNO(cursor.getString(3));
                item.setTeam(cursor.getString(4));
                item.setMission(cursor.getString(5));
                item.setPosition(cursor.getString(6));
                item.setPhoto(cursor.getString(7));
                item.setStatus(cursor.getString(8));
                // HashMap 에 추가
                sqliteDBMap.put(cursor.getString(0), item);
                aryIdx.add(cursor.getString(0));
                aryName.add(cursor.getString(1));
                aryCheck.add(cursor.getInt(8)); // 로컬에 있고 서버에 없는 자료 제거 목적
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // 아이템 데이터 추가를 위한 메소드
    public void getServerDataList(String uid, String name, String mobileNO, String officeNO, String photo_image, boolean checkItem_flag) {
        Address_Item item = new Address_Item();
        item.setPhoto(photo_image);
        item.setIdx(uid);
        item.setUserNM(name);
        item.setMobileNO(mobileNO);
        item.setOfficeNO(officeNO);
        item.setCheckBoxState(checkItem_flag);
        addressItemList.add(item);
    }

    // 선택한 데이터 추가를 위한 메소드
    public void selectDataList(String uid, String name, String mobileNO, String officeNO, String photo_image, boolean checkItem_flag) {
        Address_Item item = new Address_Item();
        item.setPhoto(photo_image);
        item.setIdx(uid);
        item.setUserNM(name);
        item.setMobileNO(mobileNO);
        item.setOfficeNO(officeNO);
        item.setCheckBoxState(checkItem_flag);
        searchItemList.add(item);
    }

    private class PhotoURLExists extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        Context context;
        private List<Address_Item> lvItemList = null;

        public ListViewAdapter(Context context, List<Address_Item> items) {
            this.context = context;
            lvItemList = items;
        }

        class ViewHolder {
            LinearLayout child_layout;
            ImageView photo_Image;
            TextView tv_name;
            TextView tv_mobileNO;
            ImageView child_btn;
        }

        @Override
        public int getCount() {
            return lvItemList.size(); // 데이터 개수 리턴
        }

        @Override
        public Object getItem(int position) {
            return lvItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // 지정한 위치(position)에 있는 데이터 리턴
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
            final ViewHolder viewHolder;
            final Context context = parent.getContext();
            final Integer index = Integer.valueOf(position);

            // 화면에 표시될 View
            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.address_item, parent, false);

                convertView.setBackgroundColor(0x00FFFFFF);
                convertView.invalidate();

                // 화면에 표시될 View 로부터 위젯에 대한 참조 획득
                viewHolder.photo_Image = convertView.findViewById(R.id.profile_Image);
                viewHolder.tv_name = convertView.findViewById(R.id.child_name);
                viewHolder.tv_mobileNO = convertView.findViewById(R.id.child_mobileNO);
                viewHolder.child_btn = convertView.findViewById(R.id.child_Btn);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // PersonData 에서 position 에 위치한 데이터 참조 획득
            final Address_Item addressItem = lvItemList.get(position);

            if(addressItem.getPhoto().contains("jpg")){
                String photoURL = Value.Photo_URL + addressItem.getPhoto();
                Glide.with(mContext).load(photoURL).into(viewHolder.photo_Image);
            }

            viewHolder.tv_name.setText(addressItem.getUserNM());
            viewHolder.tv_mobileNO.setText(PhoneNumberUtils.formatNumber(addressItem.getMobileNO()));

            final String[] items ={"휴대폰 전화걸기", "연락처 저장"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("해당작업을 선택하세요");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, items[which] + "선택했습니다.", Toast.LENGTH_SHORT).show();
                    switch (which){
                        case 0:
                            if(addressItem.getMobileNO().length() ==0){
                                Toast.makeText(context, "전화걸 휴대폰 번호가 없습니다.",Toast.LENGTH_SHORT).show();
                                break;
                            }

                            AlertDialog dialog1 = new AlertDialog.Builder(context)
                                    .setTitle(addressItem.getUserNM())
                                    .setMessage(PhoneNumberUtils.formatNumber(addressItem.getMobileNO()) + " 통화하시겠습니까?")
                                    .setPositiveButton("예",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PhoneNumberUtils.formatNumber(addressItem.getMobileNO())));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                                                        }
                                                        return;
                                                    }
                                                    startActivity(intent);
                                                }
                                            })
                                    .setNegativeButton(
                                            "아니오",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();
                            dialog1.show();
                            break;

                        case 1:
                            Toast.makeText(context, "전화번호 저장하는 로직은 직접 구현하시기 바랍니다.",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            builder.create();

            viewHolder.child_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.show();
                }
            });

            return convertView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            lvItemList.clear();
            if (charText.length() == 0) {
                lvItemList.addAll(addressItemList);
            } else {
                for (Address_Item wp : addressItemList) {
                    if (Utils.isNumber(charText)) { // 숫자여부 체크
                        if (wp.getMobileNO().contains(charText) || wp.getOfficeNO().contains(charText)) {
                            // 휴대폰번호 또는 사무실번호에 숫자가 포함되어 있으면
                            lvItemList.add(wp);
                        }
                    } else {
                        String iniName = HangulUtils.getHangulInitialSound(wp.getUserNM(), charText);
                        if (iniName.indexOf(charText) >= 0) { // 초성검색어가 있으면 해당 데이터 리스트에 추가
                            lvItemList.add(wp);
                        } else if (wp.getUserNM().toLowerCase(Locale.getDefault()).contains(charText)) {
                            lvItemList.add(wp);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed();
    }
}