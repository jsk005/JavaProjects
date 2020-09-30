package com.link2me.android.speechtext;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private final int REQ_CODE = 100;
    TextView speak2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = SpeechToTextActivity.this;

        speak2TextView = findViewById(R.id.text);
        ImageView speakImage = findViewById(R.id.speak);
        speakImage.setOnClickListener(view -> Speak2Text());
    }

    private void Speak2Text() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT","audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO",true);

        startActivityForResult(intent, REQ_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speechList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String result = speechList.get(0);
                    speak2TextView.setText(result);

                    Uri audioUri = data.getData();

                    ContentResolver contentResolver = getContentResolver();
                    InputStream inputStream = null;
                    OutputStream outputStream = null;

                    try {
                        inputStream = contentResolver.openInputStream(audioUri);
                        outputStream = null;

                        File targetFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test" );
                        if(!targetFile.exists()){
                            targetFile.mkdirs();
                        }
                        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".amr";
                        outputStream = new FileOutputStream(targetFile + "/" + fileName);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = inputStream.read(bytes)) != -1){
                            outputStream.write(bytes, 0 ,read);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(inputStream != null){
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if(outputStream != null){
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext,SplashActivity.class));
        finish();
    }

}