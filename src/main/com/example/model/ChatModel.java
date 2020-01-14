package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "chat")
public class ChatModel {

    private String userAvatar;

    @Attribute(id = "chat_date_of_creation")
    private String chatDateOfCreation;

    @Attribute(id = "chat_name")
    private String chatName;

    @Attribute(id = "chat_id")
    private  String id;

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

    public ChatModel() {}

    public ChatModel(String chatParticipant){
        this.chatParticipant = chatParticipant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatDateOfCreation() {
        return chatDateOfCreation;
    }

    public void setChatDateOfCreation(String chatDateOfCreation) {
        this.chatDateOfCreation = chatDateOfCreation;
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

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
