package com.link2me.android.qrcodesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.link2me.android.common.PrefsHelper;

public class ScanQRActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private IntentIntegrator qrScan;
    Context mContext;
    String QRcode;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        mContext = ScanQRActivity.this;

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QRcode Sample!");
        qrScan.initiateScan();

        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ScanQRActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                if(result.getContents() != null) {
                    QRcode = result.getContents();
                    PrefsHelper.write("QRCode",result.getContents());
                    // QRcode 결과를 가지고 서버에서 정보를 조회하여 가져오거나, 다른 처리를 하면 된다.
                    // QRcode 읽은 결과를 화면에 표시
                    textView.setText(QRcode);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
