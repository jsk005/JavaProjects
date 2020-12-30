package com.link2me.android.recyclerview;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.recyclerview.fragment.MapViewFragment;
import com.link2me.android.recyclerview.fragment.NotificationsFragment;
import com.link2me.android.recyclerview.fragment.RecyclerViewFragment;

public class ViewpagerActivity extends FragmentActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ViewPager2 mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    private String[] titles = new String[]{"연락처", "웹뷰", "지도"};

    String code;
    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mContext = ViewpagerActivity.this;
        backPressHandler = new BackPressHandler(this);
        //getSupportActionBar().hide();

        Fragment frag1 = new RecyclerViewFragment().newInstance(code,"");
        Fragment frag2 = new NotificationsFragment().newInstance(code,"https://m.naver.com");
        Fragment frag3 = new MapViewFragment().newInstance(code,"");

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        mViewPager.setOffscreenPageLimit(3);

        myPagerAdapter = new MyViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag1);
        myPagerAdapter.addFrag(frag2);
        myPagerAdapter.addFrag(frag3);

        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            backPressHandler.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}