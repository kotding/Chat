package com.nd3v.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
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
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_primary));
        searchBtn = findViewById(R.id.search_user_btn);
        searchUserInput = findViewById(R.id.search_username_input);
        backBtn = findViewById(R.id.chat_back_btn);
        recyclerView = findViewById(R.id.search_recycle_view);
        searchUserInput.requestFocus();
        backBtn.setOnClickListener(v ->
        {
            onBackPressed();
        });

        searchBtn.setOnClickListener(v ->
        {
            String searchTerm = searchUserInput.getText().toString();
            if(searchTerm.isEmpty()){
                searchUserInput.setError("Invalid username");
                return;
            }
            setupSearchRecycleView(searchTerm);
        });

    }
    void setupSearchRecycleView(String searchTerm){
        Query query = FirebaseUtil.AllUserReferences().whereGreaterThanOrEqualTo("username", searchTerm).orderBy("username", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().isEmpty()) {
            } else {
                FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
                adapter = new SearchUserRecycleAdapter(options, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null)
        {
            adapter.startListening();
        }

    }

/*    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
        {
            adapter.stopListening();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
        {
            adapter.startListening();
        }

    }*/
}