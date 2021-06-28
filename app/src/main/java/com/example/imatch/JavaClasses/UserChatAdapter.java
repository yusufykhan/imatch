package com.example.imatch.JavaClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.imatch.Activity.MessagesActivity;

import com.example.imatch.R;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.viewholder> {

    Context chatActivity;
    ArrayList<ChatStoring> userChatArray;
    FirebaseStorage storage ;
    StorageReference storageReference ;

    String Chated_Username , Chated_Name , ReceivedMessages;


    public UserChatAdapter(Context chatActivity, ArrayList<ChatStoring> userChatArray) {
        this.chatActivity = chatActivity ;
        this.userChatArray = userChatArray ;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(chatActivity);
        View view = layoutInflater.inflate(R.layout.userschats_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        ChatStoring chateduser = userChatArray.get(position);
        Chated_Username = chateduser.Username_Of_Chated_With ;
        Chated_Name = chateduser.Name_Of_Chated_With ;
        ReceivedMessages =  ""+chateduser.MessageReceivedFromthis ;


        if (!(ReceivedMessages.equals("0"))){
            holder.received_message_notification_card.setVisibility(View.VISIBLE);
        }

        holder.user_Name.setText(Chated_Name);
        holder.user_Username.setText(Chated_Username);
        holder.received_messages_number.setText(ReceivedMessages);


        set_image_to_chat(holder, Chated_Username);



        holder.itemView.setOnClickListener(v -> {
            Intent MessageIntent = new Intent(chatActivity , MessagesActivity.class);
            MessageIntent.putExtra("Chating_username" ,chateduser.getUsername_Of_Chated_With());
            MessageIntent.putExtra("Chating_name" , chateduser.getName_Of_Chated_With());

            chatActivity.startActivity(MessageIntent);


        });

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {

        Collections.sort(userChatArray, new Sent_Message_Compare());

        return userChatArray.size();
    }

    class  viewholder extends RecyclerView.ViewHolder{

        ImageView Chated_User_Image;
        TextView user_Name , user_Username ,received_messages_number;
        CardView chated_User_Card , received_message_notification_card;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            Chated_User_Image = itemView.findViewById(R.id.Chated_User_Image);
            user_Name = itemView.findViewById(R.id.Chated_Name);
            user_Username = itemView.findViewById(R.id.Chated_Username);
            chated_User_Card = itemView.findViewById(R.id.Chat_User_Card);
            received_message_notification_card = itemView.findViewById(R.id.message_receive_notification_card);
            received_messages_number = itemView.findViewById(R.id.received_messages_number);

        }
    }



    public void set_image_to_chat(viewholder holder , String Chated_Username){

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/").child(Chated_Username);

        try {
            final File localFile = File.createTempFile("usersProfile", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Successfully downloaded data to local file
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                holder.Chated_User_Image.setImageBitmap(bitmap);
            }).addOnFailureListener(exception -> {
                // Handle failed download
                // ...
                Toast.makeText(chatActivity, "Failed to upload Images", Toast.LENGTH_SHORT).show();
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}


class Sent_Message_Compare implements Comparator<ChatStoring>{


    @Override
    public int compare(ChatStoring o1, ChatStoring o2) {
        if(o1.MessageSentedTothis==o2.getMessageSentedTothis())
            return 0;
        else if(o1.MessageSentedTothis>o2.MessageSentedTothis)
            return -1;
        else
            return 1;
    }

}
