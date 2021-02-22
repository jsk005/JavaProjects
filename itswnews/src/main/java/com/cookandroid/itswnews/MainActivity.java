package com.cookandroid.itswnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<NewsData> newsList = new ArrayList<>(); // 데이터 리스트
    private String[] mDataset = {"1","2","3","4","5","6","7","8"};

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        buildRecyclerView();
    }

    private void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.myrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAdapter = new RecyclerViewAdapter(mContext, newsList); // 객체 생성

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        sendAndRequestResponse();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, Newsview.class);
                intent.putExtra("NEWS_URL", newsList.get(position).getUrl());
                startActivity(intent);
            }
        });

    }

    private void sendAndRequestResponse() {
        //String url ="http://newsapi.org/v2/top-headlines?country=kr&category=science&apiKey=675a48efa82b4e418bbe95f58fa9f02d";
        String url ="https://www.abc.com/_sample/getURLJSON.php";

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"onResponse : "+response.toString());
                showJSONData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void showJSONData(String response){
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray arrayArticles =  jsonObj.getJSONArray("articles");

            newsList.clear();
            for(int i = 0; i < arrayArticles.length(); i++){
                JSONObject obj = arrayArticles.getJSONObject(i);

                NewsData newsData = new NewsData();
                newsData.setTitle(obj.getString("title"));
                newsData.setUrlToImage(obj.getString("urlToImage"));
                newsData.setDescription(obj.getString("description"));
                newsData.setUrl(obj.getString("url"));
                newsList.add(newsData);
            }
            // runOnUiThread()를 호출하여 실시간 갱신한다.
            runOnUiThread(() -> {
                // 갱신된 데이터 내역을 어댑터에 알려줌
                mAdapter.notifyDataSetChanged();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}