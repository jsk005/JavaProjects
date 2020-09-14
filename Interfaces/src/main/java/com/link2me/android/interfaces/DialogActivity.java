package com.link2me.android.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.link2me.android.interfaces.interfaces.CustomDialogListener;

public class DialogActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mContext = DialogActivity.this;

        TextView textView = findViewById(R.id.tv_custom_dialog);
        Button button = findViewById(R.id.btn_click_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그 생성
                CustomDialog dialog = new CustomDialog(mContext);
                dialog.setDialogListener(new CustomDialogListener() {
                    @Override
                    public void onAgreeButtonClicked(String smsotp) {
                        textView.setText("인증번호 : "+smsotp);
                        veirifySmsOtpNo(smsotp);
                    }

                    @Override
                    public void onDisAgreeButtonClicked() {

                    }
                });
                dialog.show();
            }
        });
    }

    private void veirifySmsOtpNo(String smsotpNO){

    }
}