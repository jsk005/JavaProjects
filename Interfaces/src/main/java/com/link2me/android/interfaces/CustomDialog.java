package com.link2me.android.interfaces;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private TextView dialogTitle, dialogExplain, count_view;
    private EditText smsotp_auth;
    CountDownTimer countDownTimer;
    private Boolean isExpire = false;

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
        count_view = findViewById(R.id.timer_text);
        smsotp_auth = findViewById(R.id.et_auth_text);
        agreeButton = findViewById(R.id.btn_agree);
        disAgreeButton = findViewById(R.id.btn_disagree);

        dialogTitle.setText("SMS 인증 with OTP");
        dialogExplain.setText("인증번호 6자리를 입력하세요.");
        countDown("03:00");

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

                if(isExpire == true){
                    Toast.makeText(mContext, "인증시간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 확인 버튼 눌렀을 때 interface 메서드 호출하여 값을 Activitity 로 전달
                customDialogListener.onAgreeButtonClicked(smsotp_value);
                countDownTimer.cancel();
                dismiss();
                break;

            case R.id.btn_disagree:
                countDownTimer.cancel();
                dismiss();
                break;
        }
    }

    public void countDown(String timer) {
        // time ex) 03:00
        long conversionTime = 0;

        String getMin = timer.substring(0, 2);
        String getSecond = timer.substring(3, 5);

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기)) , 두번쨰 인자 : 주기( 1000 = 1초)
        countDownTimer = new CountDownTimer(conversionTime, 1000) {
            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {
                isExpire = false;
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                if (min.length() == 1) { // 분이 한자리면 0을 붙인다
                    min = "0" + min;
                }

                if (second.length() == 1) {  // 초가 한자리면 0을 붙인다
                    second = "0" + second;
                }

                count_view.setText("남은시간 : "+ min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {
                isExpire = true;
                count_view.setText("인증시간 만료!");
                count_view.setTextColor(Color.RED);
            }
        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
        countDownTimer = null;
    }

}
