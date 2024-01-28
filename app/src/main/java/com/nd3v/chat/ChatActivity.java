package com.nd3v.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nd3v.chat.adapter.ChatRecycleAdapter;
import com.nd3v.chat.model.ChatMessageModel;
import com.nd3v.chat.model.ChatRoomModel;
import com.nd3v.chat.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    String chatRoolId;
    ChatRoomModel chatRoomModel;
    ChatRecycleAdapter adapter;
    EditText messengeInput;
    Button sendBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatRoolId = FirebaseUtil.getChatRoolId(FirebaseUtil.CurrentUserId(), otherUser.getUserId());
        messengeInput = findViewById(R.id.input_messenge_edt);
        sendBtn = findViewById(R.id.messenge_send_btn);
        otherUsername = findViewById(R.id.orther_username_text);
        recyclerView = findViewById(R.id.chat_recycle_view);
        backBtn = findViewById(R.id.chat_back_btn);

        backBtn.setOnClickListener(v ->
        {
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());
        getOrCreateChatRoolModel();
        sendBtn.setOnClickListener(v ->
        {
            String mess = messengeInput.getText().toString().trim();
            if(mess.length() == 0) return;
            sendMessageToChatRoom(mess);
        });
        setChatRecycleView();
    }
    private  void sendMessageToChatRoom(String message)
    {
        ChatMessageModel messModel = new ChatMessageModel(message , FirebaseUtil.CurrentUserId() , Timestamp.now());
        messModel.setSenderId(FirebaseUtil.CurrentUserId());
        messModel.setTimeStamp(Timestamp.now());
        FirebaseUtil.getChatRoomReference(chatRoolId).set(chatRoomModel);

        FirebaseUtil.getChatRoomMessageReference(chatRoolId).add(messModel).addOnCompleteListener(task -> {
           if(task.isSuccessful())
           {
               messengeInput.setText("");
           }

        });
    }
    void getOrCreateChatRoolModel()
    {
        FirebaseUtil.getChatRoomReference(chatRoolId).get().addOnCompleteListener(task ->
        {
            if(task.isSuccessful())
            {
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel == null)
                {
                    chatRoomModel =
                            new ChatRoomModel(
                            chatRoolId,
                            Arrays.asList(FirebaseUtil.CurrentUserId() , otherUser.getUserId()),
                            Timestamp.now(),
                                    null
                    );
                    FirebaseUtil.getChatRoomReference(chatRoolId).set(chatRoomModel);
                }
            }
        });
    }

    void setChatRecycleView()
    {
        Log.i("DEBUG", "Chat room id" + chatRoolId);

        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoolId)
                .orderBy("timeStamp" , Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query , ChatMessageModel.class).build();
        adapter = new ChatRecycleAdapter(options , getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseUtil.getChatRoomMessageReference(chatRoolId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        //adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}