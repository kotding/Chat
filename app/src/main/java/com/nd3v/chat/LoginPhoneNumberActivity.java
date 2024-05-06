package com.nd3v.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker codePicker;
    EditText phoneInput;
    Button sendOTPBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_primary));
        setContentView(R.layout.activity_login_phone_number);
        Setup();
    }

    private  void Setup()
    {
        codePicker = findViewById(R.id.login_country_code);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOTPBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_phone_progress_bar);
        progressBar.setVisibility(View.GONE);

        codePicker.registerCarrierNumberEditText(phoneInput);
        codePicker.setCountryForPhoneCode(84);
        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(false)
                {
                    phoneInput.setError("Phone number not valid !");
                    return;
                }
                Intent intent = new Intent(LoginPhoneNumberActivity.this , LoginOtpActivity.class);
                intent.putExtra("phone" , codePicker.getFullNumberWithPlus());
                startActivity(intent);
            }
        });
    }
}