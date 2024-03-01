package com.nd3v.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nd3v.chat.adapter.UploadAvatarActivity;
import com.nd3v.chat.model.UserModel;

public class LoginUsernameActivity extends AppCompatActivity {

    String phoneNumber;
    EditText usernameInput;
    UserModel userModel;
    Button letMeInBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);
        phoneNumber = getIntent().getExtras().getString("phone");
        setUp();
        //getUsername();
        setInProgress(false);
    }

    private  void setUp(){
        usernameInput = findViewById(R.id.login_username);
        letMeInBtn = findViewById(R.id.login_let_me_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        letMeInBtn.setOnClickListener((v)->{
            setUserName();
        });
    }

    private  void setInProgress(Boolean isProgress){
        if(isProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setEnabled(false);
        }else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setEnabled(true);
        }
    }
    void setUserName()
    {
        String username = usernameInput.getText().toString();
        if(username.isEmpty()|| username.length() <= 0)
        {
            usernameInput.setError("User name is not valid");
            return;
        }
        setInProgress(true);
        if(userModel == null)
        {
            userModel = new UserModel(FirebaseUtil.CurrentUserId(),phoneNumber , usernameInput.getText().toString() , Timestamp.now());
        }else
        {
            userModel.setUsername(usernameInput.getText().toString());
        }
        Intent intent = new Intent(LoginUsernameActivity.this , UploadAvatarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("USER_NAME" , userModel.getUsername());
        intent.putExtra("PHONE" , phoneNumber);
        startActivity(intent);

    }
    private  void getUsername()
    {
        setInProgress(true);
        FirebaseUtil.CurrentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful())
                {
                    userModel = task.getResult().toObject(UserModel.class);
                    if(userModel != null)
                    {
                        usernameInput.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
}