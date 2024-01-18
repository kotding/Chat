package com.nd3v.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.nd3v.chat.model.ChatMessageModel;
import com.nd3v.chat.model.ChatRoomModel;
import com.nd3v.chat.model.UserModel;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    String chatRoolId;
    ChatRoomModel chatRoomModel;

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
}