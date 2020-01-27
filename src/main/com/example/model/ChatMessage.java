package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "messages")
public class ChatMessage {

    private MessageType type;

    private String[] usersOnline;

    @Attribute(id = "message_chat_id")
    private String chatId;

    @Attribute(id = "message_chat_name")
    private String chatName;

    @Attribute(id = "message_content")
    private String content;

    @Attribute(id = "message_name_of_sender")
    private String sender;

    @Attribute(id = "message_time_of_send")
    private String currentDate;

    public ChatMessage(){}

    public ChatMessage(String messageNameOfSender, String messageContent, String currentDate) {
        this.sender = messageNameOfSender;
        this.content = messageContent;
        this.currentDate = currentDate;
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String[] getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(String[] usersOnline) {
        this.usersOnline = usersOnline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}