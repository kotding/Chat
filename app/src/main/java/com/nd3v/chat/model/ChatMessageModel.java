package com.nd3v.chat.model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    String message;
    String senderId;
    Timestamp timeStamp;
    public  ChatMessageModel(){}
    public ChatMessageModel(String message, String senderId, Timestamp timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
