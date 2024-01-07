package com.nd3v.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    String TAG = "BUG";
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    String phoneNumber;
    Long timeOut = 60L;
    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        phoneNumber = getIntent().getExtras().getString("phone");
        AndroidUtil.showToast(getApplicationContext(),phoneNumber);
        setup();
        sendOtp(phoneNumber , false);
    }

    private void setup()
    {
        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_btn);

        nextBtn.setOnClickListener((v) ->
        {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            setInProgress(true);
            signIn(credential);
        });
        resendOtpTextView.setOnClickListener((v) ->
        {
            sendOtp(phoneNumber , true);
        });
    }

    private  void sendOtp(String phoneNumber , boolean isResend)
    {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOut , TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(), "Check OTP code send to your phone");
                        setInProgress(true);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(true);
                        AndroidUtil.showToast(getApplicationContext(),"OTP verification successfully");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(), "OTP verification failed.");
                        setInProgress(true);
                    }
                });
        if(isResend)
        {
            options.setForceResendingToken(resendingToken);
            PhoneAuthProvider.verifyPhoneNumber(options.build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(options.build());
        }
    }

    private void startResendTimer() {

        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOut --;
                resendOtpTextView.setText("Resend OTP in " + timeOut + " seconds");
                if(timeOut <= 0)
                {
                    timeOut = 60L;
                    timer.cancel();
                    resendOtpTextView.setEnabled(true);
                }
            }
        } , 0 , 1000);

    }

    private  void signIn(PhoneAuthCredential phoneAuthCredential)
    {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(LoginOtpActivity.this , LoginUsernameActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }else {
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification failed.");
                }
            }
        });
    }
    private  void setInProgress(boolean isProgress)
    {
        if(isProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }
}