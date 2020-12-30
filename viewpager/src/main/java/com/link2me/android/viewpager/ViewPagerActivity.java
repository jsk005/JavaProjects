package com.link2me.android.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.viewpager.fragment.AddressFragment;
import com.link2me.android.viewpager.fragment.Fragment4;
import com.link2me.android.viewpager.fragment.Fragment5;
import com.link2me.android.viewpager.fragment.HomeFragment;
import com.link2me.android.viewpager.fragment.WebViewFragment;

public class ViewPagerActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ViewPager mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    String code;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mContext = ViewPagerActivity.this;
        backPressHandler = new BackPressHandler(this);
        getSupportActionBar().hide();

        //code = getIntent().getExtras().getString("code"); // 다른 Activity에서 값을 넘겨 받았을 때
        code ="";
        Log.e(TAG,code);

        Fragment frag1 = new HomeFragment().newInstance(code,"");
        Fragment frag2 = new WebViewFragment().newInstance(code,"https://m.naver.com");
        Fragment frag3 = new AddressFragment().newInstance(code);
        Fragment frag4 = new Fragment4().newInstance(code,"");
        Fragment frag5 = new Fragment5().newInstance(code,"");

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        mViewPager.setOffscreenPageLimit(5);

        myPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFrag(frag1,"Home");
        myPagerAdapter.addFrag(frag2,"공지");
        myPagerAdapter.addFrag(frag3,"지도");
        myPagerAdapter.addFrag(frag4,"위치");
        myPagerAdapter.addFrag(frag5,"검색");

        mViewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}