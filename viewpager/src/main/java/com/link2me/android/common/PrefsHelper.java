package com.link2me.android.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    public static final String PREFERENCE_NAME="pref";
    private Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;
    private static PrefsHelper instance;

    public static synchronized PrefsHelper init(Context context){
        if(instance == null)
            instance = new PrefsHelper(context);
        return instance;
    }

    private PrefsHelper(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE );
        prefsEditor = prefs.edit();
    }

    public static String read(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static void write(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        prefsEditor.putInt(key, value).commit();
    }

    public static boolean read(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }
}
