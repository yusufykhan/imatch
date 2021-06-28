package com.example.imatch.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imatch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Interest_Information_Activity extends AppCompatActivity {

    String SelectedInterest ,Interestinformation ;
    ImageView InterestImage , Go_to_Back ;
    TextView Interest_Information , Selected_interest_Heading  ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceDatabase ;
    FirebaseStorage storageDatabase ;
    StorageReference storageDataReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_information);

        InterestImage = findViewById(R.id.interest_Image);
        Interest_Information = findViewById(R.id.interest_Information);
        Selected_interest_Heading = findViewById(R.id.selected_interest_heading);
        Go_to_Back = findViewById(R.id.go_to_back);

        SelectedInterest = getIntent().getStringExtra("interest");
        Selected_interest_Heading.setText(SelectedInterest);

        firebaseDatabase = FirebaseDatabase.getInstance() ;
        referenceDatabase = firebaseDatabase.getReference("Interest_Information");

        storageDatabase = FirebaseStorage.getInstance();
        storageDataReference = storageDatabase.getReference("Interest_Images/").child(SelectedInterest+".jpg");


        set_Selected_Interest_Image();

        get_Information_About_Interest();

        go_back();

    }

    private void go_back() {

        Go_to_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentgoToInterestlist = new Intent(getApplicationContext(), InterestActivity.class);
                startActivity(intentgoToInterestlist);
                finish();
            }
        });

    }

    private void set_Selected_Interest_Image() {

        try{
            final File localFile = File.createTempFile("Interest","jpg");

            storageDataReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Successfully downloaded data to local file
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        InterestImage.setImageBitmap(bitmap);
                        // ...
                    }).addOnFailureListener(exception -> {
                // Handle failed download
                // ...
                Toast.makeText(Interest_Information_Activity.this, "Failed to load Images", Toast.LENGTH_SHORT).show();
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    private void get_Information_About_Interest() {


        Query checkMatch = referenceDatabase.orderByChild(SelectedInterest);

        checkMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Interestinformation = Objects.requireNonNull(snapshot.child(SelectedInterest).getValue()).toString();
                    Interest_Information.setText(Interestinformation);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}