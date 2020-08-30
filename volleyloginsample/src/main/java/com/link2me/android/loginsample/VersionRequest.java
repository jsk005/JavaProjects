package com.link2me.android.loginsample;

import android.content.Context;
import android.os.Build;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.link2me.android.common.Utils;

import java.util.HashMap;
import java.util.Map;

public class VersionRequest extends StringRequest {
    final static private  String URL = Value.IPADDRESS + "versionChk.php";
    private Map<String,String> params;

    public VersionRequest(Context context, String ostype, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("ostype", ostype);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
