package com.example.imatch.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imatch.JavaClasses.ChatStoring;
import com.example.imatch.JavaClasses.Messages;
import com.example.imatch.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static com.example.imatch.Activity.MainActivity.UserName;

import static com.example.imatch.Activity.MainActivity.interest;
import static com.example.imatch.Activity.MainActivity.MyName;

import static com.example.imatch.Activity.MainActivity.sp;


public class MatchingActivity extends AppCompatActivity {

    TextView Welcomemsg, MatchedUser, InterestDetail, StartMatchtxt;
    Button startMatching, sayHelloBtn;
    ImageView MatchedUserProfile ;
    String matcheduser ;
    ProgressBar SearchProgress;
    BottomNavigationView Bottom_Nevigation;
    ArrayList<String> alreadyMatchedList = new ArrayList<>();
    ArrayList<String> intrst = new ArrayList<>();
    FirebaseDatabase firebaseSearch;
    DatabaseReference referenceSearch , referenceSaveChat;
    FirebaseStorage storage ;
    StorageReference storageReference ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_matching);

        sp = getSharedPreferences("Login",MODE_PRIVATE);

        if (interest.isEmpty()){
            Intent intentInterestlist = new Intent(getApplicationContext(), InterestActivity.class);
            startActivity(intentInterestlist);
            sp.edit().putString("Signned_is_true",null).apply();
            finish();
        }

        findides();

        firebaseSearch = FirebaseDatabase.getInstance();
        referenceSearch = firebaseSearch.getReference("userdata");

        referenceSaveChat = firebaseSearch.getReference("Chat_To_User");


        Welcomemsg.setText("Welcome " + UserName);
        InterestDetail.setText("Interest : " + interest);

        sayHelloBtn.setOnClickListener(v1 -> sendingRequest());

        startMatching.setOnClickListener(v -> {

            alreadyMatchedList = addMatches_to_List();

            MatchedUser.setVisibility(View.GONE);
            sayHelloBtn.setVisibility(View.GONE);
            MatchedUserProfile.setVisibility(View.GONE);
            StartMatchtxt.setVisibility(View.GONE);
            SearchProgress.setVisibility(View.VISIBLE);
            SearchProgress.getProgress();

            Query checkMatch = referenceSearch.orderByChild("Interest").equalTo(interest);

            checkMatch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        matching(snapshot);
                        getRandomMatch();

                        storage = FirebaseStorage.getInstance();
                        storageReference = storage.getReference("images/").child(matcheduser);

                        getProfile_Of_Matched_User();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

    }

    @SuppressLint("SetTextI18n")
    private void getProfile_Of_Matched_User() {

        try{
            final File localFile = File.createTempFile("harry","jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {

                        // Successfully downloaded data to local file
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        MatchedUserProfile.setImageBitmap(bitmap);

                    }).addOnFailureListener(exception -> {
                        // Handle failed download
                        // ...
                        Toast.makeText(MatchingActivity.this, "Failed to load Images", Toast.LENGTH_LONG).show();
                    });
        }
        catch (IOException e){
            e.printStackTrace();
        }
        MatchedUserProfile.setVisibility(View.VISIBLE);
        MatchedUser.setVisibility(View.VISIBLE);
        MatchedUser.setText("You Matched with : " + matcheduser);
        SearchProgress.setVisibility(View.GONE);
        sayHelloBtn.setVisibility(View.VISIBLE);
        startMatching.setText("Again");
    }

    private void sendingRequest() {

        Query MatchedUser = referenceSearch.orderByChild("username").equalTo(matcheduser);

        MatchedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.exists()) {
                    for (DataSnapshot ds : snap.getChildren()) {
                        String MatchedName = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        System.out.println("already matched function call checking "+ MatchedName);

                        referenceSaveChat.child(UserName).child("chats").child(matcheduser).setValue(new ChatStoring(matcheduser,MatchedName ,0,1,"false"));
                        referenceSaveChat.child(matcheduser).child("chats").child(UserName).setValue(new ChatStoring(UserName,MyName, 1,0,"true"));

                        Intent MessageIntent = new Intent(MatchingActivity.this, MessagesActivity.class);


                        MessageIntent.putExtra("Chating_username" , matcheduser);
                        MessageIntent.putExtra("Chating_name" , MatchedName);

                        send_hello_message();


                        Toast.makeText(MatchingActivity.this, "Message Request is sent", Toast.LENGTH_LONG).show();
                        startActivity(MessageIntent);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void send_hello_message() {

        DatabaseReference referenceMessage ;

        referenceMessage = firebaseSearch.getReference("All_Messages");

            Date date = new Date();
            Messages message = new Messages(date.getTime() ,UserName,"Hello!");

            referenceMessage.child(UserName+matcheduser)
                    .child("messages")
                    .child(UserName)
                    .push().setValue(message)
                    .addOnCompleteListener(task -> referenceMessage
                            .child(matcheduser+UserName)
                            .child("messages")
                            .child(matcheduser)
                            .push().setValue(message));

    }

    @SuppressLint("NonConstantResourceId")
    private void findides() {

        startMatching = findViewById(R.id.StartMatchingbtn);
        InterestDetail = findViewById(R.id.showInterestDetails);
        Welcomemsg = findViewById(R.id.showUserDetails);
        MatchedUser = findViewById(R.id.matcheduser);
        MatchedUserProfile = findViewById(R.id.matched_user_profile);
        SearchProgress = findViewById(R.id.searchingProgress);
        StartMatchtxt = findViewById(R.id.startMatchText);
        sayHelloBtn = findViewById(R.id.SayHello);
        Bottom_Nevigation = findViewById(R.id.bottom_Nevigation);


        Bottom_Nevigation.setSelectedItemId(R.id.matching);


        Bottom_Nevigation.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.matching:
                    return true;
                case R.id.interets:
                    startActivity(new Intent(getApplicationContext(), InterestActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
                case R.id.setting:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
            }
            return false;
        });

    }

    @SuppressLint("SetTextI18n")
    private void matching(DataSnapshot snapshot) {


        for (DataSnapshot ds : snapshot.getChildren()) {
            String anotherUser = Objects.requireNonNull(ds.child("username").getValue()).toString();
            if (!(anotherUser.equals(UserName))) {
                    intrst.add(anotherUser);
            }
        }

    }




    private ArrayList<String> addMatches_to_List() {

        ArrayList<String> AlreadyMatchedList = new ArrayList<>();


        Query Matched_Chat = referenceSaveChat.child(UserName).child("chats").orderByChild("chated_With_Username");

        Matched_Chat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.exists()) {
                    for (DataSnapshot dschat : snap.getChildren()) {
                        String AlreadyMatch = Objects.requireNonNull(dschat.child("username_Of_Chated_With").getValue()).toString();
                        AlreadyMatchedList.add(AlreadyMatch);
                        System.out.println("already matched function call checking "+AlreadyMatch);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return AlreadyMatchedList ;
    }


    private void getRandomMatch()
    {
            for (Object obj2 : alreadyMatchedList) {
                System.out.println(obj2 + " comparing to list ");
                boolean isRemovedSucessfully;
                isRemovedSucessfully = intrst.remove(obj2);
                System.out.println("removes successfully " + isRemovedSucessfully);
            }

        if (!(intrst.isEmpty())) {

            Random random_method = new Random();
            // loop for picking random user of similar interest
            int index = random_method.nextInt(intrst.size());

            System.out.println("Random Element is :"
                    + intrst.get(index));

            matcheduser = intrst.get(index);

        }
        else {
            matcheduser = "No Match Found" ;
        }

    }

}