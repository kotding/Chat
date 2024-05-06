package com.nd3v.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_primary));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseUtil.IsLoggedIn()){
                    startActivity(new Intent(SplashActivity.this , MainActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this , LoginPhoneNumberActivity.class));
                }
                finish();
            }
        } , 1500);
    }
}