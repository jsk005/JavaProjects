package com.link2me.android.loginsample;

import android.content.Context;
import android.os.Build;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.link2me.android.common.Utils;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private  String URL = Value.IPADDRESS + "loginChk.php";
    private Map<String,String> params;

    public LoginRequest(Context context, String userID, String userPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("password", userPW);
        params.put("uID", Utils.getDeviceId(context)); // 스마트폰 고유장치번호
        params.put("mfoneNO", Utils.getPhoneNumber(context)); // 스마트폰 전화번호
        params.put("AppVersion", String.valueOf(Utils.getVersionCode(context)));
        params.put("phoneVersion", Build.VERSION.RELEASE);
        params.put("phoneBrand", Build.BRAND);
        params.put("phoneModel", Build.MODEL);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
