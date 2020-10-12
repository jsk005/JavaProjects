package com.link2me.android.calculator;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.link2me.android.calculator.databinding.ActivityMainBinding;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity {
    String process;
    Boolean checkBracket = false;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // 1. inflate 호출하여 인스턴스 생성
        View view = binding.getRoot(); // 2. getRoot()
        setContentView(view); // 3. 화면상의 활성 뷰로 만든다.

        binding.btnClear.setOnClickListener(v -> {
            binding.tvInput.setText("");
            binding.tvOutput.setText("");
        });

        binding.btn0.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "0");
        });

        binding.btn1.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "1");
        });

        binding.btn2.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "2");
        });

        binding.btn3.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "3");
        });

        binding.btn4.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "4");
        });

        binding.btn5.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "5");
        });

        binding.btn6.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "6");
        });

        binding.btn7.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "7");
        });

        binding.btn8.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "8");
        });

        binding.btn9.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "9");
        });

        binding.btn0.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "0");
        });

        binding.btnAdd.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "+");
        });

        binding.btnMinus.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "-");
        });

        binding.btnMultiply.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "×");
        });

        binding.btnDivision.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "÷");
        });

        binding.btnDot.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + ".");
        });

        binding.btnPercent.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();
            binding.tvInput.setText(process + "%");
        });

        binding.btnEquals.setOnClickListener(v -> {
            process = binding.tvInput.getText().toString();

            process = process.replaceAll("×", "*");
            process = process.replaceAll("%",  "/100");
            process = process.replaceAll("÷","/");

            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);

            String finalResult = "";

            try {
                Scriptable scriptable = rhino.initStandardObjects();
                finalResult = rhino.evaluateString(scriptable,process,"javascript",1,null).toString();
            }catch (Exception e){
                finalResult="0";
            }
            binding.tvOutput.setText(finalResult);
        });

        binding.btnBracket.setOnClickListener(v -> {
            if (checkBracket){
                process = binding.tvInput.getText().toString();
                binding.tvInput.setText(process + ")");
                checkBracket = false;
            }
            else{
                process = binding.tvInput.getText().toString();
                binding.tvInput.setText(process + "(");
                checkBracket = true;
            }
        });

    }
}
