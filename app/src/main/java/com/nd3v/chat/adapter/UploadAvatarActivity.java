package com.nd3v.chat.adapter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.UploadTask;
import com.nd3v.chat.AndroidUtil;
import com.nd3v.chat.FirebaseUtil;
import com.nd3v.chat.MainActivity;
import com.nd3v.chat.R;
import com.nd3v.chat.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadAvatarActivity extends AppCompatActivity {

    Button uploadBtn;
    ImageView avatar;
    int CHOOSE_AVATAR = 0;
    Bitmap avatarBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);
        setUpButton();
    }

    void setUpButton()
    {
        uploadBtn = this.findViewById(R.id.btn_upload_avatar);
        avatar = this.findViewById(R.id.upload_avatar_img);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBtn.setText(uploadBtn.getText() + "C");
                ChooseAvatar();
            }
        });
    }


    void ChooseAvatar()
    {
        Log.i("DEBUG" , "Choose");
        Intent itent = new Intent( Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mGetContent.launch(itent);
    }
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    try {
                        Uri uri = o.getData().getData();
                        avatar.setImageURI(uri);
                        InputStream stream = getContentResolver().openInputStream(uri);
                        avatarBitmap = BitmapFactory.decodeStream(stream);
                        uploadBtn.setText("Upload avatar");
                        uploadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UploadAvatar(v);
                            }
                        });
                    }
                    catch (FileNotFoundException ex)
                    {
                        AndroidUtil.showToast(getApplicationContext(),"File not found, ngu");
                    }
                }
            });
    void UploadAvatar(View v)
    {
        uploadBtn.setText("Uploading image ....");
        uploadBtn.setClickable(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatarBitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = FirebaseUtil.getAvatarStorageRef().child(FirebaseUtil.CurrentUserId()).putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return FirebaseUtil.getAvatarStorageRef().child(FirebaseUtil.CurrentUserId()).getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    UserModel model = new UserModel(FirebaseUtil.CurrentUserId() , getIntent().getStringExtra("PHONE") , getIntent().getStringExtra("USER_NAME") , Timestamp.now());
                    model.setAvatarUrl(downloadUri.toString());
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseUtil.CurrentUserId()).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            uploadBtn.setText("All done!");
                            uploadBtn.setClickable(true);
                            uploadBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OpenHome(v);
                                }
                            });
                        }
                    });
                } else {
                    AndroidUtil.showToast(getApplicationContext() , task.getException().getMessage());
                }
            }
        });
    }

    void OpenHome(View v)
    {
        startActivity(new Intent(this , MainActivity.class));
    }

}

