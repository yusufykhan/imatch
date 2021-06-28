package com.example.imatch.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.imatch.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.imatch.Activity.MainActivity.MyName;
import static com.example.imatch.Activity.MainActivity.UserName;
import static com.example.imatch.Activity.MainActivity.UserPassword;
import static com.example.imatch.Activity.MainActivity.UserPhoneNumber;
import static com.example.imatch.Activity.MainActivity.interest;
import static com.example.imatch.Activity.MainActivity.user_email;
import static com.example.imatch.Activity.MainActivity.sp;


public class Upload_Profile_Photo extends AppCompatActivity {

    TextView Skip ;
    ImageView UploadedImage ;
    Button UploadImageBtn , SaveImageBtn;
    public Uri imageUri ;
    FirebaseStorage storage ;
    StorageReference storageReference ;
    String usernameOfUser ,Already_logged_In ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_photo);

        sp = getSharedPreferences("Login",MODE_PRIVATE);

        user_email = sp.getString("user_email","");
        UserName = sp.getString("UserName","");
        interest = sp.getString("interest","");
        MyName = sp.getString("MyName","");
        UserPassword = sp.getString("UserPassword","");
        UserPhoneNumber = sp.getString("UserPhoneNumber","");
        usernameOfUser = UserName ;
        Already_logged_In = sp.getString("Signned_is_true","");


        if (Already_logged_In.isEmpty()){
            Intent intentimagechanged = new Intent(getApplicationContext(), MatchingActivity.class);
            startActivity(intentimagechanged);
            finish();
        }

        UploadedImage = findViewById(R.id.uploaded_image);
        UploadImageBtn = findViewById(R.id.upload_image_btn);
        SaveImageBtn = findViewById(R.id.save_image_btn);
        Skip = findViewById(R.id.skip);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");

        UploadImageBtn.setOnClickListener(v -> chooseProfilepic());

        Skip.setOnClickListener(v -> skipUploadImage() );

    }

    private void chooseProfilepic() {
        Intent intentimage = new Intent();
        intentimage.setType("image/*");
        intentimage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentimage,1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();

            UploadedImage.setVisibility(View.VISIBLE);
            SaveImageBtn.setVisibility(View.VISIBLE);
            UploadedImage.setImageURI(imageUri);
            UploadImageBtn.setText("Choose Different");

            SaveImageBtn.setOnClickListener(v -> {
                uploadProfile();
                skipUploadImage() ;
            });
        }
    }

    private void uploadProfile() {


        StorageReference profilesRef = storageReference.child(usernameOfUser);
        profilesRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

            // Successfully downloaded data to local file
            Toast.makeText(Upload_Profile_Photo.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(exception -> {
            // Handle failed download
            // ...
            Toast.makeText(Upload_Profile_Photo.this, "Failed to load Images", Toast.LENGTH_SHORT).show();
        });


    }

    private void skipUploadImage() {

        if(Already_logged_In.equals("Signned_is_true")) {
            Intent intentInterestlist = new Intent(getApplicationContext(), InterestActivity.class);
            sp.edit().putString("Signned_is_true",null).apply();
            startActivity(intentInterestlist);
            finish();
        }
        else {
            Intent intentimagechanged = new Intent(getApplicationContext(), MatchingActivity.class);
            sp.edit().putString("Signned_is_true",null).apply();
            startActivity(intentimagechanged);
            finish();
        }

    }


}

