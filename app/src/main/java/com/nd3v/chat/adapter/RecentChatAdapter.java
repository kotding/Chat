package com.nd3v.chat.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.nd3v.chat.AndroidUtil;
import com.nd3v.chat.ChatActivity;
import com.nd3v.chat.R;
import com.nd3v.chat.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentChatAdapter extends BaseAdapter
{
        List<DocumentSnapshot> docs;
        public  RecentChatAdapter(List<DocumentSnapshot> d)
        {
                docs = d;
        }
        @Override
        public int getCount() {
                return docs.size();
        }

        @Override
        public Object getItem(int position) {
                return docs.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
                View view = null;
                if(convertView == null)
                {
                        view = View.inflate(parent.getContext() , R.layout.recent_chat_row , null);
                }
                Log.i("DB" , position + "");
                Log.i("DB" , docs.size() + "");
                Log.i("DB" , docs.get(position).toString() + "");
                String username = docs.get(position).get("userName").toString();
                String userUrl = docs.get(position).get("avatarUrl").toString();
                String userId = docs.get(position).get("userId").toString();

                //ChatActivity.Friend fr = docs.get(position).toObject(ChatActivity.Friend.class);
                Log.i("DB" , docs.get(position).get("userName").toString());
                TextView usernameTw =  view.findViewById(R.id.recent_user_tw);
                ImageView avatar = view.findViewById(R.id.recent_icon);
                if(convertView == null)
                {
                        usernameTw.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        Intent intent = new Intent(parent.getContext(), ChatActivity.class);
                                        UserModel user = new UserModel(userId, "0123456789" , username , null);
                                        AndroidUtil.passUserModeAsIntent(intent,user);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        parent.getContext().startActivity(intent);
                                }
                        });

                }
                usernameTw.setText(username);
                Picasso.with(parent.getContext()).load(Uri.parse(userUrl)).into(avatar);
                return view;
        }
}
