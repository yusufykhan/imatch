package com.example.imatch.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imatch.JavaClasses.Messages;
import com.example.imatch.JavaClasses.MessagesAdapter;
import com.example.imatch.R;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.example.imatch.Activity.MainActivity.UserName;

public class MessagesActivity extends AppCompatActivity {


    TextView MessagingNameTextview , MessagingUsenameTextview ;
    ImageView go_to_back ;
    String receiverUsername ,receiverName ;
    EditText SendMessage ;
    CardView SendBtn ;
    RecyclerView messages_RecyclerView ;
    String senderRoom ;
    String receiverRoom ;
    int sent_message_number;
    String isOnline ;

    MessagesAdapter messagesAdapter ;

    ArrayList<Messages> messagesArrayList ;

    FirebaseDatabase firebasrMessage ;
    DatabaseReference referenceMessage ,referenceSentMessage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        findides();


        setadapter() ;

        get_Sented_Unreaded_Messages();


        queryTheMessages();


        SendBtn.setOnClickListener(v -> send_Message() );


        go_to_back.setOnClickListener(v -> {
            referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("isMessageSeen").setValue("false");
            startActivity(new Intent(MessagesActivity.this,ChatActivity.class));
            finish();
        });

    }

    private void send_Message() {

        String msg= SendMessage.getText().toString();

        if (msg.isEmpty()){
            Toast.makeText(MessagesActivity.this, "Type Message First", Toast.LENGTH_SHORT).show();
        }
        else {
            SendMessage.setText("");
            Date date = new Date();

            Messages message = new Messages(date.getTime() ,UserName,msg);


            sent_message_number += 1;

            if (isOnline.equals("false")) {
                referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("messageReceivedFromthis").setValue(sent_message_number);
                referenceSentMessage.child(UserName).child("chats").child(receiverUsername).child("messageSentedTothis").setValue(sent_message_number);
            }

            referenceMessage.child(senderRoom)
                    .child("messages")
                    .child(UserName)
                    .push().setValue(message)
                    .addOnCompleteListener(task -> referenceMessage.child(receiverRoom)
                            .child("messages")
                            .child(receiverUsername)
                            .push().setValue(message)
                            .addOnCompleteListener(task1 -> {

                            }));

        }

    }

    private void get_Sented_Unreaded_Messages() {


        Query getSentMessagesnumber = referenceSentMessage.child(UserName).child("chats").child(receiverUsername);
        getSentMessagesnumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    isOnline = Objects.requireNonNull(snapshot.child("isMessageSeen").getValue()).toString();
                    sent_message_number = Integer.parseInt(Objects.requireNonNull(snapshot.child("messageSentedTothis").getValue()).toString());
                    System.out.println("number of message received already by user : "+sent_message_number);
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    private void queryTheMessages() {

        Query getMessages = referenceMessage.child(senderRoom).child("messages").child(UserName);

        getMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for (DataSnapshot dsSnap : snapshot.getChildren()){

                    Messages messages = dsSnap.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    private void setadapter() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);

        messages_RecyclerView.setLayoutManager(linearLayoutManager);

        messagesAdapter = new MessagesAdapter(MessagesActivity.this , messagesArrayList);

        messages_RecyclerView.setAdapter(messagesAdapter);



    }

    private void findides() {

        messagesArrayList = new ArrayList<>();

        receiverUsername = getIntent().getStringExtra("Chating_username");
        receiverName = getIntent().getStringExtra("Chating_name");


        go_to_back = findViewById(R.id.go_to_back);
        messages_RecyclerView = findViewById(R.id.MessageRecyclerView);
        SendMessage = findViewById(R.id.SendMessageEditxt);
        SendBtn = findViewById(R.id.SendBtn);

        firebasrMessage = FirebaseDatabase.getInstance();
        referenceMessage = firebasrMessage.getReference("All_Messages");
        referenceSentMessage = firebasrMessage.getReference("Chat_To_User");


        referenceSentMessage.child(UserName).child("chats").child(receiverUsername).child("messageReceivedFromthis").setValue(0);
        referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("messageSentedTothis").setValue(0);
        referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("isMessageSeen").setValue("true");

        MessagingNameTextview = findViewById(R.id.Chatting_Name);
        MessagingUsenameTextview = findViewById(R.id.Chatting_Username);
        MessagingNameTextview.setText(receiverName);
        MessagingUsenameTextview.setText(receiverUsername);

        senderRoom = UserName + receiverUsername ;

        receiverRoom = receiverUsername + UserName ;



    }

    @Override
    public void onBackPressed() {
        referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("isMessageSeen").setValue("false");
        startActivity(new Intent(MessagesActivity.this,ChatActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("isMessageSeen").setValue("false");
    }

    @Override
    protected void onResume() {
        super.onResume();
        referenceSentMessage.child(receiverUsername).child("chats").child(UserName).child("isMessageSeen").setValue("true");
    }
}

