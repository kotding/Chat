package com.nd3v.chat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil
{
    public  static  String CurrentUserId()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    public  static DocumentReference CurrentUserDetails()
    {
        return FirebaseFirestore.getInstance().collection("users").document();
    }
    public  static  boolean IsLoggedIn()
    {
        return CurrentUserId() != null;
    }
    public  static CollectionReference AllUserReferences()
    {
        return FirebaseFirestore.getInstance().collection("users");
    }
}
