package com.nd3v.chat.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private  String userId;
    private  String phone;
    private  String username;
    private  String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/privatechat-30fe3.appspot.com/o/normal_avatar.png?alt=media&token=7ab3f3ff-e275-459f-b603-2a62b67478c0";
    private Timestamp createdTimeStamp;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    public  String getUserId() {return userId;}

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }
    public  String getAvatarUrl(){return avatarUrl;}
    public  void setAvatarUrl(String url)
    {
        avatarUrl = url;
    }

    public UserModel(String userId,String phone, String username, Timestamp createdTimeStamp) {
        this.phone = phone;
        this.username = username;
        this.createdTimeStamp = createdTimeStamp;
        this.userId = userId;
    }
    public  UserModel(){}
}
