package com.link2me.android.speechtext;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.Utils;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private BackPressHandler backPressHandler;

    private int SPLASH_TIME_OUT = 5000;
    Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, SpeechToTextActivity.class));
            finish();
        }
    };

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
        backPressHandler = new BackPressHandler(this);

        if (Utils.NetworkConnection(mContext) == false)
            Utils.NotConnected_showAlert(mContext, this);
        checkPermissions();
    }

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 26) { // 출처를 알 수 없는 앱 설정 화면 띄우기
            PackageManager pm = mContext.getPackageManager();
            Log.e("Package Name", getPackageName());
            if (!pm.canRequestPackageInstalls()) {
                startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + getPackageName())));
            }
        }

        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(mContext)
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView() {

        // 5초기 되기전에 버튼을 클릭하면 화면이 SppechActivity로 이동하도록 처리
        FloatingActionButton fab_voice = findViewById(R.id.fab_voice);
        fab_voice.setOnClickListener(v -> {
            startActivity(new Intent(mContext, TextToSpeechActivity.class));
            finish();
        });

        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 두번 연속 누르면 앱 종료
        backPressHandler.onBackPressed();
    }

}
