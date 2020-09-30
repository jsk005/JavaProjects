package com.link2me.android.speechtext;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private TextToSpeech textToSpeech;
    private EditText speakText;
    private Button speakBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        mContext = TextToSpeechActivity.this;

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TextToSpeech");
        setSupportActionBar(toolbar);

        speakText = findViewById(R.id.et_text2voice);
        speakBtn = findViewById(R.id.btn_speech);

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.KOREAN);
            }
        });

        speakBtn.setOnClickListener(v -> texttoSpeak());
    }

    private void texttoSpeak() {
        String text = speakText.getText().toString();
        if ("".equals(text)) {
            text = "Please enter some text to speak.";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            String utteranceId = this.hashCode() + "";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext,SplashActivity.class));
        finish();
    }
}