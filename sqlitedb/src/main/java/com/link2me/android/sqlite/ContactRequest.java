package com.link2me.android.sqlite;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.link2me.android.common.Utils;

import java.util.HashMap;
import java.util.Map;

public class ContactRequest extends StringRequest {
    final static private  String URL = Value.IPADDRESS + "getContact.php";
    private Map<String,String> params;

    public ContactRequest(Context context, String idx, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("idx", Utils.getDeviceId(context)); // 스마트폰 고유장치번호
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
