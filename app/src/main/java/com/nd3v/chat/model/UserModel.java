package com.nd3v.chat.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private  String userId;
    private  String phone;
    private  String username;
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

    public UserModel(String userId,String phone, String username, Timestamp createdTimeStamp) {
        this.phone = phone;
        this.username = username;
        this.createdTimeStamp = createdTimeStamp;
        this.userId = userId;
    }
    public  UserModel(){}
}
