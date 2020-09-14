package com.link2me.android.interfaces;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.link2me.android.interfaces.interfaces.CustomDialogListener;

public class CustomDialog extends Dialog implements View.OnClickListener {
    Context mContext;

    private Button agreeButton, disAgreeButton;
    private TextView dialogTitle, dialogExplain;
    private EditText smsotp_auth;

    private CustomDialogListener customDialogListener;
    public void setDialogListener(CustomDialogListener listener) {
        // Activity에서 호출할 리스너
        customDialogListener = listener;
    }

    public CustomDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        inflateView();
    }

    private void inflateView() {
        dialogTitle = findViewById(R.id.dialog_title_text);
        dialogExplain = findViewById(R.id.dialog_explain_text);
        smsotp_auth = findViewById(R.id.et_auth_text);
        agreeButton = findViewById(R.id.btn_agree);
        disAgreeButton = findViewById(R.id.btn_disagree);

        dialogTitle.setText("SMS 인증 with OTP");
        dialogExplain.setText("인증번호 6자리를 입력하세요.");

        // 버튼 클릭 리스너 등록
        agreeButton.setOnClickListener(this::onClick);
        disAgreeButton.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_agree:
                String smsotp_value = smsotp_auth.getText().toString().trim();
                if(smsotp_value.length() != 6){
                    Toast.makeText(mContext, "인증번호 6자리를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 확인 버튼 눌렀을 때 interface 메서드 호출하여 값을 Activitity 로 전달
                customDialogListener.onAgreeButtonClicked(smsotp_value);
                dismiss();
                break;

            case R.id.btn_disagree:
                dismiss();
                break;
        }
    }
}
