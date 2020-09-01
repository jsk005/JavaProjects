package com.link2me.android.navidrawer;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.PrefsHelper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    DrawerLayout drawer_layout;
    ActionBarDrawerToggle toggle;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        backPressHandler = new BackPressHandler(this);

        drawer_layout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        // content_main.xml 에서 지정한 버튼을 클릭하면 drawer 가 오픈되도록 함.
        findViewById(R.id.btn_drawer).setOnClickListener(this::onClick);

        ProfileView();

        // content_main.xml 에서 FloatingActionButton 클릭시 처리
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 액션 처리
                                Toast.makeText(MainActivity.this, "액션 잘 동작함", Toast.LENGTH_SHORT).show();
                            }
                        }).setActionTextColor(Color.GREEN)
                        .show();
            }
        });

    }

    private void ProfileView() {
        ImageView img_profile = findViewById(R.id.img_profile);
        TextView userNM = findViewById(R.id.tv_userNM);
        TextView mobileNO = findViewById(R.id.tv_mobileNO);

        String photoURL = Value.PhotoADDRESS + PrefsHelper.read("profileImg","") + ".jpg";
        // 사진 이미지가 존재하지 않을 수도 있으므로 존재 여부를 체크하여 존재하면 ImageView 에 표시한다.
        PhotoURLExists task = new PhotoURLExists();
        try {
            if(task.execute(photoURL).get()==true){
                Glide.with(mContext).load(photoURL).override(170, 200).into(img_profile);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        userNM.setText(PrefsHelper.read("userNM",""));
        mobileNO.setText(PrefsHelper.read("mobileNO",""));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }
    }

    // drawer 오픈된 상태에서 실행할 때 동작 이벤트 처리
    public void onClickNavItem(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                drawer_layout.closeDrawer(Gravity.LEFT, true);
                break;

            case R.id.btn_password:
                Intent passwd_intent = new Intent(mContext,PasswordActivity.class);
                startActivity(passwd_intent);
                break;

            case R.id.btn_appinfo:
                Intent appinfo_intent = new Intent(mContext,AppInfoActivity.class);
                startActivity(appinfo_intent);
                break;

            case R.id.btn_license_info:
                Intent licence_intent = new Intent(mContext, LicenseInfoActivity.class);
                startActivity(licence_intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            backPressHandler.onBackPressed();
        }
    }

}
