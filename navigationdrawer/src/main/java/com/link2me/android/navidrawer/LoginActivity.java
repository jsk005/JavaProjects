package com.link2me.android.navidrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.link2me.android.common.PrefsHelper;
import com.link2me.android.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();

    Context mContext;

    EditText etId;
    EditText etPw;
    String userID;
    String userPW;
    String uID; // 스마트 기기의 대체 고유값

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LoginActivity.this, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        // 네트워크 연결 검사
        if(Utils.NetworkConnection(mContext) == false) Utils.NotConnected_showAlert(mContext,this);
        checkPermissions();
    }

    private void checkPermissions() {

        if(Build.VERSION.SDK_INT >= 26){ // 출처를 알 수 없는 앱 설정 화면 띄우기
            PackageManager pm = mContext.getPackageManager();
            Log.e("Package Name",getPackageName());
            if (!pm.canRequestPackageInstalls()){
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
        etId = findViewById(R.id.et_id);
        etPw = findViewById(R.id.et_pw);
        Button btn_login = findViewById(R.id.btn_login);

        etId.setText(PrefsHelper.read("userid",""));
        if(!PrefsHelper.read("userid","").equals("")){
            etPw.requestFocus();
        }
        etPw.setText(PrefsHelper.read("userpw",""));

        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        userID = etId.getText().toString().trim();
        userPW = etPw.getText().toString().trim();

        String phoneNO = Utils.getPhoneNumber(mContext);

        if(userID != null && !userID.isEmpty() && userPW != null && !userPW.isEmpty()){
            //LoginVolley(userID,userPW);
            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Log.e(TAG,"jsonObject : "+jsonObject);
                    String status = jsonObject.getString("status");
                    if(status.equals("success")){
                        JSONObject userinfo = new JSONObject(jsonObject.getString("userinfo"));
                        String userNM = userinfo.getString("userNM");
                        //Log.e(TAG,"userNM:"+userNM);
                        PrefsHelper.write("userid",userID);
                        PrefsHelper.write("userpw",userPW);
                        PrefsHelper.write("userNM",userNM);
                        PrefsHelper.write("mobileNO",userinfo.getString("mobileNO"));
                        PrefsHelper.write("profileImg",userinfo.getString("profileImg"));

                        Toast.makeText(mContext, "로그인 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.showAlert(mContext,status,jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            LoginRequest loginRequest = new LoginRequest(mContext,userID,userPW,responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(mContext); // RequestQueue 생성 및 초기화
            requestQueue.add(loginRequest); // 생성한 StringRequest를 RequestQueue에 추가
        }
    }


}