package com.nd3v.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.auth.User;
import com.nd3v.chat.FirebaseUtil;
import com.nd3v.chat.R;
import com.nd3v.chat.model.ChatMessageModel;
import com.nd3v.chat.model.UserModel;

public class ChatRecycleAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecycleAdapter.ChatViewHolder> {

    Context context;
    public  ChatRecycleAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options , Context context)
    {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessageModel model) {
        Log.i("DEBUG",model.getMessage());
        if(model.getSenderId().equals(FirebaseUtil.CurrentUserId()))
        {
            holder.leftView.setVisibility(View.GONE);
            holder.rightView.setVisibility(View.VISIBLE);
            holder.senderTextView.setText(model.getMessage());
        }else {
            holder.rightView.setVisibility(View.GONE);
            holder.leftView.setVisibility(View.VISIBLE);
            holder.receiveTextView.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("DEBUG","CREATE VIEW HOLDER");
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycle_row,parent , false);
        return new ChatRecycleAdapter.ChatViewHolder(view);

    }

    public  class ChatViewHolder extends RecyclerView.ViewHolder
    {
        TextView senderTextView , receiveTextView;
        LinearLayout leftView , rightView;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.right_chat_textview);
            receiveTextView = itemView.findViewById(R.id.left_chat_textview);

            leftView = itemView.findViewById(R.id.left_chat_layout);
            rightView = itemView.findViewById(R.id.right_chat_layout);
        }
    }
}


