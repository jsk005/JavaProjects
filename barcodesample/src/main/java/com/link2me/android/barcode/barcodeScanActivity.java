package com.link2me.android.barcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.link2me.android.common.PrefsHelper;

public class barcodeScanActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;
    private IntentIntegrator integrator;
    private ViewfinderView viewfinderView;
    String barCodeScanResult;
    TextView textView;

    /*
    바코드 스캔과 QR코드 스캔은 동일한 소스코드를 사용한다.
    스캔하는 카메라 영역이 QR코드 스캔은 정사각형으로 제공되고,
    바코드 스캔은 Custom Activity 로 약간 수정하여 처리한다.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodescan);
        mContext = barcodeScanActivity.this;

        viewfinderView = findViewById(R.id.zxing_viewfinder_view);

        integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false); // 휴대폰 방향에 따라 가로, 세로로 자동 변경
        integrator.setBeepEnabled(true);
        integrator.initiateScan();

        // 바코드 스캔 결과를 화면에 보여주기 위한 임시 용도
        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(barcodeScanActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                if(result.getContents() != null) {
                    barCodeScanResult = result.getContents();
                    PrefsHelper.write("barCode",result.getContents()); // 내부 저장소에 저장
                    // 바코드 스캔 결과를 가지고 서버에서 정보를 조회하여 가져오거나, 다른 처리를 하면 된다.

                    textView.setText(barCodeScanResult); // 바코드를 스캔하여 읽은 결과를 화면에 표시
                    readBarcode(barCodeScanResult); // Toast 메시지로 보여주기 위한 용도
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void readBarcode(String barcode){
        Toast.makeText(getApplicationContext(),barcode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(barcodeScanActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}