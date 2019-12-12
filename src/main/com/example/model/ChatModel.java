package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "chat")
public class ChatModel {

    @Attribute(id = "chat_name")
    private String chatName;

    @Attribute(id = "chat_name_of_creator")
    private String chatNameOfCreator;

    @Attribute(id = "chat_participant")
    private String chatParticipant;

    @Attribute(id = "chat_status")
    private String chatStatus = "active";

    public ChatModel(String chatName, String chatNameOfCreator) {
        this.chatName = chatName;
        this.chatNameOfCreator = chatNameOfCreator;
    }

    public ChatModel() {

    }

    public ChatModel(String chatParticipant){
        this.chatParticipant = chatParticipant;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatParticipant() {
        return chatParticipant;
    }

    public void setChatParticipant(String chatParticipant) {
        this.chatParticipant = chatParticipant;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatNameOfCreator() {
        return chatNameOfCreator;
    }

    public void setChatNameOfCreator(String chatNameOfCreator) {
        this.chatNameOfCreator = chatNameOfCreator;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }
}
