package com.example.imatch.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imatch.Adapters.DetailsAdapter;
import com.example.imatch.JavaClasses.Detail;
import com.example.imatch.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.imatch.Activity.MainActivity.user_email;
import static com.example.imatch.Activity.MainActivity.sp;
import static com.example.imatch.Activity.MainActivity.MyName;
import static com.example.imatch.Activity.MainActivity.UserName;
import static com.example.imatch.Activity.MainActivity.UserPhoneNumber;


public class SettingActivity extends AppCompatActivity {

    BottomNavigationView Bottom_Nevigation ;
    TextView ShowingUsername ;
    Button update , logout , changephoto;
    ArrayList<Detail> DetailsStoringclassArrayList ;
    Detail details ;
    RecyclerView userDataList ;
    DetailsAdapter adapter ;
    ImageView UploadedImage ;
    FirebaseStorage storage ;
    StorageReference storageReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = getSharedPreferences("Login",MODE_PRIVATE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/").child(UserName);

        findides();

        setProfileImage();

        userDataList.setLayoutManager(new LinearLayoutManager(this));

        DetailsStoringclassArrayList.add(0 , new Detail("Name",MyName));
        DetailsStoringclassArrayList.add(1 , new Detail("Username",UserName));
        DetailsStoringclassArrayList.add(2 , new Detail("Email" ,user_email));
        DetailsStoringclassArrayList.add(3 , new Detail("Phone number",UserPhoneNumber));


        adapter = new DetailsAdapter(this,DetailsStoringclassArrayList );

        userDataList.setAdapter(adapter);

        Bottom_Nevigation.setSelectedItemId(R.id.setting);


        logout.setOnClickListener(v -> {
            sp.edit().putString("Signned_is_true",null).apply();
            sp.edit().putString("interest",null).apply();
            sp.edit().putBoolean("loggedIn",false).apply();
            Intent intentLogOut =new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intentLogOut);
            finish();
        });

        update.setOnClickListener(v -> {
            Intent intentUpdate =new Intent(getApplicationContext(),Update_Details_Activity.class);
            startActivity(intentUpdate);
        });

        changephoto.setOnClickListener(v -> {
            Intent intentchangeimage =new Intent(SettingActivity.this,Upload_Profile_Photo.class);
            sp.edit().putString("Signned_is_true","Change_Profile").apply();
            startActivity(intentchangeimage);
            finish();
        });


    }

    private void setProfileImage() {

        try{
            final File localFile = File.createTempFile("harry","jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Successfully downloaded data to local file

                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        UploadedImage.setImageBitmap(bitmap);
                        // ...
                    }).addOnFailureListener(exception -> {
                        // Handle failed download
                        // ...
                        Toast.makeText(SettingActivity.this, "Failed to load Images", Toast.LENGTH_LONG).show();
                    });
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @SuppressLint("NonConstantResourceId")
    private void findides() {

        details = new Detail();

        Bottom_Nevigation = findViewById(R.id.bottom_Nevigation) ;
        ShowingUsername = findViewById(R.id.log_In_Username);
        UploadedImage = findViewById(R.id.uploaded_image);
        changephoto = findViewById(R.id.change_profile_image);
        logout = findViewById(R.id.log_out_btn);
        update = findViewById(R.id.update_btn);
        DetailsStoringclassArrayList = new ArrayList<>();
        userDataList = findViewById(R.id.User_Data_list);

        ShowingUsername.setText(UserName);

        Bottom_Nevigation.setSelectedItemId(R.id.setting);

        Bottom_Nevigation.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.setting :
                    return true ;
                case R.id.interets :
                    startActivity(new Intent(getApplicationContext(),InterestActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
                case R.id.chat :
                    startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
                case R.id.matching :
                    startActivity(new Intent(getApplicationContext(), MatchingActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
            }
            return false;
        });

    }
}