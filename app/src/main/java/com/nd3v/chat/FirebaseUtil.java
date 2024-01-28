package com.nd3v.chat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public  static  DocumentReference getChatRoomReference(String chatRoolId)
    {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoolId);
    }
    public  static  CollectionReference getChatRoomMessageReference(String chatroomId)
    {
        return  getChatRoomReference(chatroomId).collection("chats");
    }
    public  static  String getChatRoolId(String userId1 , String userId2)
    {
        if(userId1.hashCode()< userId2.hashCode())
        {
            return userId1 + "_"+ userId2;
        }else {
            return userId2 + "_" + userId1;
        }

    }
    public  static FirebaseStorage getFirebaseStorageInstance()
    {
        return FirebaseStorage.getInstance();
    }

    public  static StorageReference getFirebaseRef()
    {
        return getFirebaseStorageInstance().getReference();
    }

    public  static  StorageReference getAvatarStorageRef()
    {
        return getFirebaseRef().child("avatars");
    }
}
