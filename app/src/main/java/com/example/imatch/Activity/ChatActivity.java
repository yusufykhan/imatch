package com.example.imatch.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.imatch.Activity.MainActivity.UserName;

import com.example.imatch.JavaClasses.ChatStoring;
import com.example.imatch.R;
import com.example.imatch.JavaClasses.UserChatAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {

    BottomNavigationView Bottom_Nevigation ;
    RecyclerView userRecyclerView ;
    UserChatAdapter userChatAdapter ;
    ArrayList<ChatStoring> UserChatArray ;
    TextView Logged_In_Username ;

    FirebaseDatabase firebaseGetChat;
    DatabaseReference referenceGetChat;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Bottom_Nevigation = findViewById(R.id.bottom_Nevigation);
        Logged_In_Username = findViewById(R.id.logged_in_username);

        Logged_In_Username.setText(UserName);

        firebaseGetChat = FirebaseDatabase.getInstance();

        Bottom_Nevigation.setSelectedItemId(R.id.chat);
        UserChatArray = new ArrayList<>();


        Bottom_Nevigation.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.chat :
                    return true ;
                case R.id.interets :
                    startActivity(new Intent(getApplicationContext(),InterestActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
                case R.id.matching :
                    startActivity(new Intent(getApplicationContext(), MatchingActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
                case R.id.setting :
                    startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return false ;
            }
            return false;
        });



        referenceGetChat = firebaseGetChat.getReference("Chat_To_User");

        Query getchat = referenceGetChat.child(UserName).child("chats").orderByChild("username_Of_Chated_With");

        getchat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ChatStoring chated_users = dataSnapshot.getValue(ChatStoring.class);

                    UserChatArray.add(chated_users);
                }
                userChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRecyclerView = findViewById(R.id.userChatsRecyclerView);
        LinearLayoutManager linearLayoutManager ;
        linearLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(linearLayoutManager);
        userChatAdapter = new UserChatAdapter(ChatActivity.this,UserChatArray);
        userRecyclerView.setAdapter(userChatAdapter);


    }

}