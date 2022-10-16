package com.link2me.android.room_mvvm.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;
import com.link2me.android.room_mvvm.databinding.ActivitySplashBinding;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private ActivitySplashBinding binding;

    Handler handler = new Handler(Looper.getMainLooper());
    private int delay = 2000;

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = SplashActivity.this;
        PrefsHelper.init(getApplicationContext()); // 최초 Activity에서 한번만 실행하면 된다.

        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            // Android 11 에서 전화번호 읽어보기 권한이 변경되었다. AndroidManifest.xml 파일도 같이 수정한다.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                        .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                        .setPermissions(
                                android.Manifest.permission.READ_PHONE_NUMBERS,
                                android.Manifest.permission.READ_CALL_LOG,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .check();
            } else { // Android 10 이하
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                        .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                        .setPermissions(
                                android.Manifest.permission.READ_PHONE_STATE,
                                android.Manifest.permission.READ_CALL_LOG,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .check();
            }

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

        LoginProgress();
    }

    private void LoginProgress() {
        handler.postDelayed(() -> {
            Utils.goNextActivity(mContext, MainActivity.class);
            finish();
        },delay);
    }
}