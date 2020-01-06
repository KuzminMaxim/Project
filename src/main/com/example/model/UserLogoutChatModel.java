package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "chat")
public class UserLogoutChatModel {

    @Attribute(id = "logout_chat_id")
    private String chatId;

    @Attribute(id = "logout_user_name")
    private String userName;

    @Attribute(id = "user_logout_chat_time")
    private String userLogoutTime;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogoutTime() {
        return userLogoutTime;
    }

    public void setUserLogoutTime(String userLogoutTime) {
        this.userLogoutTime = userLogoutTime;
    }
}
