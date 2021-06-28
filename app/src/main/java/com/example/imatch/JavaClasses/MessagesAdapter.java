package com.example.imatch.JavaClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imatch.JavaClasses.Messages;
import com.example.imatch.R;

import java.util.ArrayList;

import static com.example.imatch.Activity.MainActivity.UserName;


public class MessagesAdapter extends RecyclerView.Adapter {


    Context context ;
    ArrayList<Messages> messagesArrayList ;

    int messsage_send = 1 ;
    int message_received = 2 ;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        if(viewType ==messsage_send ){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_item_layout,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false);
            return new ReceiverViewHolder(view) ;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder)holder ;

            viewHolder.Message_TextView.setText(messages.getMessage());
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder ;

            viewHolder.Message_TextView.setText(messages.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position) ;

        if(UserName.equals(messages.getSenderId())){
            return messsage_send ;
        }
        else{
            return message_received ;
        }
    }

    class SenderViewHolder  extends RecyclerView.ViewHolder{

        TextView Message_TextView ;

        public SenderViewHolder(@NonNull  View itemView) {
            super(itemView);

            Message_TextView = itemView.findViewById(R.id.sender_Message);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView Message_TextView ;

        public ReceiverViewHolder(@NonNull  View itemView) {
            super(itemView);

            Message_TextView = itemView.findViewById(R.id.receiver_Message);
        }
    }

}
