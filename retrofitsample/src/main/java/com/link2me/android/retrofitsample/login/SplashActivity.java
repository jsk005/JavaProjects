package com.link2me.android.retrofitsample.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.link2me.android.retrofitsample.MainActivity;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;
import com.link2me.android.retrofitsample.R;
import com.link2me.android.retrofitsample.model.LoginResult;
import com.link2me.android.retrofitsample.network.RetrofitAPI;
import com.link2me.android.retrofitsample.network.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        PrefsHelper.init(getApplicationContext()); // 한번만 실행하면 된다.
        // 네트워크 연결 검사
        if (Utils.NetworkConnection(mContext) == false) {
            Utils.NotConnected_showAlert(mContext, this);
        } else {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(mContext)
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.READ_PHONE_STATE
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 26) { // 출처를 알 수 없는 앱 설정 화면 띄우기
            PackageManager pm = mContext.getPackageManager();
            Log.e("Package Name", getPackageName());
            if (!pm.canRequestPackageInstalls()) {
                startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + getPackageName())));
            }
        }

        AutoLoginProgress();

    }

    private void AutoLoginProgress() {
        // 자동 로그인 체크 검사
        String userID = PrefsHelper.read("userid", "");
        String userPW = PrefsHelper.read("userpw", ""); // 자동 저장처리 하지 않는게 원칙

        if (userID != null && !userID.isEmpty() && userPW != null && !userPW.isEmpty()) {
            String uID = Utils.getDeviceId(mContext); // 스마트폰 고유장치번호
            String mfoneNO = Utils.getPhoneNumber(mContext); // 스마트폰 전화번호

            RetrofitAPI mloginService = RetrofitService.getClient().create(RetrofitAPI.class);
            mloginService.Login(userID,userPW,uID,mfoneNO)
                    .enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {
                            LoginResult result = response.body();
                            if(result.getStatus().contains("success")){
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if(result.getStatus().contains("로그인 에러")){
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Utils.showAlert(mContext, result.getStatus(), result.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {

                        }
                    });

        } else {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            finish();
        }

    }
}