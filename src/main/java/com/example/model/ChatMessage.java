package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "messages")
public class ChatMessage {

    private MessageType type;

    @Attribute(id = "message_chat_name")
    private String chatName;

    @Attribute(id = "message_content")
    private String content;

    @Attribute(id = "message_name_of_sender")
    private String sender;

    public enum MessageType {
        CHAT, JOIN, LEAVE
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

}