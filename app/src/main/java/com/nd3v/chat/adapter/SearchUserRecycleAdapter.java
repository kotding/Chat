package com.nd3v.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.nd3v.chat.AndroidUtil;
import com.nd3v.chat.ChatActivity;
import com.nd3v.chat.FirebaseUtil;
import com.nd3v.chat.R;
import com.nd3v.chat.model.UserModel;

public class SearchUserRecycleAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecycleAdapter.UserModelViewHolder> {

    Context context;
    public SearchUserRecycleAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameTextView.setText(model.getUsername());
        if(model.getUserId().equals(FirebaseUtil.CurrentUserId()))
        {
            holder.usernameTextView.setText((holder.usernameTextView.getText().toString()+ " (Me)"));
        }
        holder.phoneTextView.setText(model.getPhone());
        holder.itemView.setOnClickListener(v ->
        {
                Intent intent = new Intent(context , ChatActivity.class);
                AndroidUtil.passUserModeAsIntent(intent,model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycle_row,parent , false);
        return new UserModelViewHolder(view);
    }

    public class UserModelViewHolder extends RecyclerView.ViewHolder  {
        TextView usernameTextView;
        TextView phoneTextView;
        ImageView profilePic;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text);
            phoneTextView = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_imageview);
        }

    }


}
