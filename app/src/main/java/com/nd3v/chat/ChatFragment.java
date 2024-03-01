package com.nd3v.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nd3v.chat.adapter.RecentChatAdapter;
import com.nd3v.chat.model.UserModel;

import java.util.List;

public class ChatFragment extends Fragment {
    ListView listView;
    ViewGroup container;
    public ChatFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFragment();
    }

    void setupFragment()
    {
        listView = getView().findViewById(R.id.recent_recycle_view);

        FirebaseFirestore.getInstance().collection("users").document(FirebaseUtil.CurrentUserId()).collection("friends").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs =  queryDocumentSnapshots.getDocuments();
                RecentChatAdapter adapter = new RecentChatAdapter(docs);
                listView.setAdapter(adapter);

            }
        }
    );
    }

}