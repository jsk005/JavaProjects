package com.link2me.android.recyclerview;

import android.content.Context;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.link2me.android.common.AES256Cipher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Value extends AppCompatActivity {
    private static String key = "abcdefghijklmnopqrstuvwxyz123456"; // AES 암호화  키
    private static final String URLKEY = "jgsysyksr897213579"; // URL 신뢰성 향상을 위해 사용하는 키

    // URL 신뢰 향상을 위한 키 값
    public static String URLkey() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date current = new Date();
        String date = formater.format(current);

        String keyword = Value.URLKEY + date;
        return keyword;
    }

    // 암호화
    public static String encrypt(String data) {
        try {
            data = AES256Cipher.AES_encrypt(data, Value.key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 복호화
    public static String decrypt(String data) {
        try {
            data = AES256Cipher.AES_decrypt(data, Value.key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getDeviceId(Context context) {
        // 단말기의 ID 정보를 얻기 위해서는 READ_PHONE_STATE 권한이 필요
        String deviceId;
        deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

}
