package com.nd3v.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.nd3v.chat.adapter.SearchUserRecycleAdapter;
import com.nd3v.chat.model.UserModel;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchUserInput;
    ImageButton searchBtn;
    ImageButton backBtn;
    RecyclerView recyclerView;
    SearchUserRecycleAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchBtn = findViewById(R.id.search_user_btn);
        searchUserInput = findViewById(R.id.search_username_input);
        backBtn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_recycle_view);
        searchUserInput.requestFocus();
        backBtn.setOnClickListener(v ->
        {
            onBackPressed();
        });

        searchBtn.setOnClickListener(v ->
        {
            String searchTerm = searchUserInput.getText().toString();
            if(searchTerm.isEmpty()|| searchTerm.length() < 3){
                searchUserInput.setError("Invalid username");
                return;
            }
            setupSearchRecycleView(searchTerm);
        });

    }
    void setupSearchRecycleView(String searchTerm){

        Query query = FirebaseUtil.AllUserReferences().whereGreaterThan("username",searchTerm);
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
        adapter = new SearchUserRecycleAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }
}