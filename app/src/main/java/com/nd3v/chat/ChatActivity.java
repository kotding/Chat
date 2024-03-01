package com.nd3v.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nd3v.chat.adapter.ChatRecycleAdapter;
import com.nd3v.chat.model.ChatMessageModel;
import com.nd3v.chat.model.ChatRoomModel;
import com.nd3v.chat.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

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
    ImageView otherIcon;
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
        otherIcon = findViewById(R.id.profile_pic_imageview);
        Picasso.with(getApplicationContext()).load(Uri.parse(otherUser.getAvatarUrl())).into(otherIcon);
        backBtn.setOnClickListener(v ->
        {
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());
        getOrCreateChatRoomModel();
        sendBtn.setOnClickListener(v ->
        {
            String mess = messengeInput.getText().toString().trim();
            if(mess.length() == 0) return;
            sendMessageToChatRoom(mess);
        });

        setChatRecycleView();
        addOrUpdateFriend();
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
    void addOrUpdateFriend()
    {
        Friend fr = new Friend();
        fr.userId = otherUser.getUserId();
        fr.isBestFriend = false;
        fr.userName = otherUser.getUsername();
        fr.avatarUrl = "https://firebasestorage.googleapis.com/v0/b/privatechat-30fe3.appspot.com/o/normal_avatar.png?alt=media&token=7ab3f3ff-e275-459f-b603-2a62b67478c0";
        FirebaseFirestore.getInstance().collection("users").document(FirebaseUtil.CurrentUserId()).collection("friends").document(otherUser.getUserId()).set(fr);
        fr.userId = FirebaseUtil.CurrentUserId();
        FirebaseFirestore.getInstance().collection("users").document(otherUser.getUserId()).collection("friends").document(fr.userId).set(fr);
    }
    void getOrCreateChatRoomModel()
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
    public class  Friend
    {
        public String userId = "";
        public Boolean isBestFriend = false;
        public String avatarUrl = "";
        public  String userName = "";
        public  Friend(){
            this.userId = "";
            this.isBestFriend = false;
            this.avatarUrl = "";
            this.userName = "";
        }
    }
}