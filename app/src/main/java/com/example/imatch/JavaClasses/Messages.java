package com.example.imatch.JavaClasses;

public class Messages {

    String Message ;
    String SenderId ;
    long  TimeStamp ;

    public Messages() {
    }

    public Messages(long timeStamp , String senderId , String message  ) {
        Message = message;
        SenderId = senderId;
        TimeStamp = timeStamp;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
