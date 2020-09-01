package com.link2me.android.navidrawer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
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
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.READ_CALL_LOG,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView() {
        UpgradeChk();
    }

    private void UpgradeChk() {
       Response.Listener<String> versionResponse = response -> {
           try {
               JSONObject jsonObject = new JSONObject(response);
               int ServerVersion = Integer.parseInt(jsonObject.getString("version"));
               int LocalVersion = Utils.getVersionCode(mContext);
               if(LocalVersion < ServerVersion){
                   // 서버 버전이 더 높으면 업그레이드 로직을 체크하여 앱 자동 업데이트 처리한다.
                   UpgradeProcess();
               } else {
                   AutoLoginProgress();
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       };
       VersionRequest versionRequest = new VersionRequest(mContext,"a",versionResponse);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext); // RequestQueue 생성 및 초기화
        requestQueue.add(versionRequest); // 생성한 StringRequest를 RequestQueue에 추가
    }

    void UpgradeProcess(){
        AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
        ab.setMessage("버전 업그레이드 되었습니다. 새로운 버전으로 다시 설치해주십시오.");
        DialogInterface.OnClickListener dlgTel = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.e("TAG", "Upgrade Process Start");
                Intent intent = new Intent(SplashActivity.this, Upgrade.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        };
        DialogInterface.OnClickListener dlgList = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AutoLoginProgress();
                // 업그레이드를 취소하면 현재 버전으로 로그인 처리
                // 임직원이 많은 조직도에서 한꺼번에 업데이트 시도시 서버 과부하로 인한 문제 발생을 고려
            }
        };
        ab.setPositiveButton("업데이트", dlgTel);
        ab.setNegativeButton("취소", dlgList);
        ab.show();
    }

    void AutoLoginProgress() {
        // 자동 로그인 체크 검사
        userID = PrefsHelper.read("userid", "");
        userPW = PrefsHelper.read("userpw", "");

        if (userID != null && !userID.isEmpty() && userPW != null && !userPW.isEmpty()) {
            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("success")){
                        // LoginActivity 와는 처리하는 내용이 다를 경우 문제가 생길 수도 있겠더라.
                        // 서버의 개인정보가 수정되었을 때 자동로그인 처리를 하면 휴대폰 번호가 변경된 걸 감지 못할 수도 있다.
                        JSONObject userinfo = new JSONObject(jsonObject.getString("userinfo"));
                        String userNM = userinfo.getString("userNM");
                        //Log.e(TAG,"userNM:"+userNM);
                        PrefsHelper.write("userid",userID);
                        PrefsHelper.write("userpw",userPW);
                        PrefsHelper.write("userNM",userNM);
                        PrefsHelper.write("mobileNO",userinfo.getString("mobileNO"));
                        PrefsHelper.write("profileImg",userinfo.getString("profileImg"));

                        Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    } else {
                        if(status.contains("로그인 에러")){
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Utils.showAlert(mContext, status, jsonObject.getString("message"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            LoginRequest loginRequest = new LoginRequest(mContext,userID,userPW,responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(mContext); // RequestQueue 생성 및 초기화
            requestQueue.add(loginRequest); // 생성한 StringRequest를 RequestQueue에 추가

        } else {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            finish();
        }

    }
}