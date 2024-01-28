package com.nd3v.chat.adapter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.firebase.storage.UploadTask;
import com.nd3v.chat.AndroidUtil;
import com.nd3v.chat.FirebaseUtil;
import com.nd3v.chat.MainActivity;
import com.nd3v.chat.R;

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
    }

    void setUpButton()
    {
        uploadBtn = findViewById(R.id.btn_upload_avatar);
        avatar = findViewById(R.id.upload_avatar_img);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAvatar();
                Log.i("DEBUG" , "GGGGDDFQWE123133");
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
        uploadBtn.setText("Uploading ....");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatarBitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
        byte[] data = baos.toByteArray();

        UploadTask task = FirebaseUtil.getAvatarStorageRef().putBytes(data);
        task.addOnCompleteListener((task1 -> {
            if(task.isSuccessful())
            {
                uploadBtn.setText("Whelcome you!");
                uploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenHome(v);
                    }
                });
            }else {
                AndroidUtil.showToast(getApplicationContext() , "Upload failed , check mang cua may");
            }
        }));
    }

    void OpenHome(View v)
    {
        startActivity(new Intent(this , MainActivity.class));
    }

}