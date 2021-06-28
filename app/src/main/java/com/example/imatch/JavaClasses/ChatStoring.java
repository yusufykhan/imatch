package com.example.imatch.JavaClasses;

public class ChatStoring {

    String Username_Of_Chated_With ;
    String Name_Of_Chated_With  ;
    int MessageReceivedFromthis;
    int MessageSentedTothis ;
    String isMessageSeen ;

    public ChatStoring() {
    }

    public ChatStoring(String username_Of_Chated_With, String name_Of_Chated_With, int messageReceivedFromthis, int messageSentedTothis, String isMessageSeen) {
        Username_Of_Chated_With = username_Of_Chated_With;
        Name_Of_Chated_With = name_Of_Chated_With;
        MessageReceivedFromthis = messageReceivedFromthis;
        MessageSentedTothis = messageSentedTothis;
        this.isMessageSeen = isMessageSeen;
    }

    public String getIsMessageSeen() {
        return isMessageSeen;
    }

    public void setIsMessageSeen(String isMessageSeen) {
        this.isMessageSeen = isMessageSeen;
    }

    public int getMessageReceivedFromthis() {
        return MessageReceivedFromthis;
    }

    public void setMessageReceivedFromthis(int messageReceivedFromthis) {
        MessageReceivedFromthis = messageReceivedFromthis;
    }

    public int getMessageSentedTothis() {
        return MessageSentedTothis;
    }

    public void setMessageSentedTothis(int messageSentedTothis) {
        MessageSentedTothis = messageSentedTothis;
    }

    public String getUsername_Of_Chated_With() {
        return Username_Of_Chated_With;
    }

    public void setUsername_Of_Chated_With(String username_Of_Chated_With) {
        Username_Of_Chated_With = username_Of_Chated_With;
    }

    public String getName_Of_Chated_With() {
        return Name_Of_Chated_With;
    }

    public void setName_Of_Chated_With(String name_Of_Chated_With) {
        Name_Of_Chated_With = name_Of_Chated_With;
    }
}
