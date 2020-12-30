package com.link2me.android.viewpager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.viewpager2.fragment.AddressFragment;
import com.link2me.android.viewpager2.fragment.HomeFragment;
import com.link2me.android.viewpager2.fragment.WebviewFragment;

public class ViewPagerActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ViewPager2 mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    private String[] titles = new String[]{"리스트", "웹뷰", "연락처"};

    String code;
    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = ViewPagerActivity.this;
        backPressHandler = new BackPressHandler(this);

        //code = getIntent().getExtras().getString("code"); // 다른 Activity에서 값을 넘겨 받았을 때
        code = "";
        Log.e(TAG, code);

        Fragment frag1 = new HomeFragment().newInstance(code,"");
        Fragment frag2 = new WebviewFragment().newInstance(code,"https://m.naver.com");
        Fragment frag3 = new AddressFragment().newInstance(code);

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);

        myPagerAdapter = new MyViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag1);
        myPagerAdapter.addFrag(frag2);
        myPagerAdapter.addFrag(frag3);

        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();
    }

}