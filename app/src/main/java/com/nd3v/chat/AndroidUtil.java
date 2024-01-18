package com.nd3v.chat;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nd3v.chat.model.UserModel;

public class AndroidUtil {
    public  static  void showToast(Context context , String message)
    {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show();
    }
    public  static void   passUserModeAsIntent(Intent intent , UserModel model)
    {
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUserId());
    }

    public  static  UserModel getUserModelFromIntent(Intent intent)
    {
        String userId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("username");
        String phone = intent.getStringExtra("phone");
        UserModel user = new UserModel(userId , phone , userName , null);
        return user;
    }
}
