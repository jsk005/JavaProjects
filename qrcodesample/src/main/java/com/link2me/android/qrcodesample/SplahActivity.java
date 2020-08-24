package com.link2me.android.qrcodesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;

import java.util.ArrayList;

public class SplahActivity extends AppCompatActivity {
    Context mContext;
    String userID;
    String userPW;

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SplahActivity.this, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);
        mContext = SplahActivity.this;
        PrefsHelper.init(getApplicationContext()); // 한번만 실행하면 된다.
        // 네트워크 연결 검사
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
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView() {
        AutoLoginProgress();
    }

    void AutoLoginProgress() {
        // 자동 로그인 체크 검사
        userID = PrefsHelper.read("userid", "");
        userPW = PrefsHelper.read("userpw", "");

        // 로그인 로직은 직접 구현하기 바람.
        // 로그인 성공하면 화면 전환

        Intent intent = new Intent(SplahActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}