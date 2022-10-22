package com.link2me.android.common;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.link2me.android.googlemap.GValue;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtils extends AppCompatActivity {
    // AESkey 와 URLKey 는 서버와 일치하도록 확인해야 한다.
    private static final String AESkey = "TBbitkey991ggis195971076janukims"; // AES 암호화  키
    private static final String URLKey = "jgsysyksr897213579"; // URL 신뢰성 향상을 위해 사용하는 키

    private static final byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    // URL 신뢰 향상을 위한 키 값
    public static String URLkey() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date current = new Date();
        String date = formater.format(current);

        String keyword = CipherUtils.URLKey + date;
        return keyword;
    }

    // 암호화
    public static String encryptAES(String data) {
        try {
            data = AES_Encode(data, CipherUtils.AESkey);
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
    public static String decryptAES(String data) {
        try {
            data = AES_Decode(data, CipherUtils.AESkey);
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

    private static String AES_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        return android.util.Base64.encodeToString(cipher.doFinal(textBytes), 0);
    }

    private static String AES_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = android.util.Base64.decode(str,0);
        //byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptRSA(String plainText){
        // 서버에서 가져온 공개키를 넣어준다.
        String encryptedData = null;
        try {
            encryptedData = CipherUtils.RSA_Encode(plainText, GValue.publicKeyStr);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String RSA_Encode(String plainText, String base64PublicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, InvalidKeySpecException, IllegalBlockSizeException {

        //평문으로 전달받은 공개키를 공개키 객체로 만드는 과정
        byte[] decodedBase64PubKey = java.util.Base64.getDecoder().decode(base64PublicKey);
        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));

        // 만들어진 공개키 객체를 기반으로 암호화모드로 설정하는 과정
        // Java와 Java 간에 암호화/복호화시에는 RSA 를 사용한다.
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding"); // Java - PHP 간 통신
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        return java.util.Base64.getEncoder().encodeToString(bytePlain);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptRSA(String encrypted, String base64PrivateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException {

        byte[] decodedBase64PrivateKey = java.util.Base64.getDecoder().decode(base64PrivateKey);

        PrivateKey privateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));

        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        byte[] byteEncrypted = java.util.Base64.getDecoder().decode(encrypted.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        return new String(bytePlain, "utf-8");
    }

}
