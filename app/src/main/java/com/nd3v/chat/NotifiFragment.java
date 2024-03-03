package com.nd3v.chat;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotifiFragment extends Fragment {

    TextView notifiTW;
    CardView cv1 , cv2;
    public NotifiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notifi, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiTW = getView().findViewById(R.id.tw_notifi);

        loadNotificationContent();
    }

    void loadNotificationContent()
    {
        FirebaseFirestore.getInstance().collection("content").document("notifi").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                notifiTW.setText(Html.fromHtml(documentSnapshot.get("notifi_content").toString(),Html.FROM_HTML_MODE_COMPACT));
            }
        });
    }
}