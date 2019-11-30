package com.example.controller;

import com.example.api.MyApi;
import com.example.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    MyApi api;

    @MessageMapping("/{chatName}.sendMessage")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws NoSuchFieldException, IllegalAccessException {

        System.out.println("chatMessage.getSender() in sendMessage in WebSocketController: " + chatMessage.getSender());
        System.out.println("chatMessage.getContent() in sendMessage in WebSocketController: " + chatMessage.getContent());
        System.out.println("chatMessage.getType() in sendMessage in WebSocketController: " + chatMessage.getType());
        System.out.println("chatMessage.getChatName() in sendMessage in WebSocketController: " + chatMessage.getChatName());

        api.save(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addUser")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        System.out.println("Sender in addUser in WebSocketController: " + chatMessage.getSender());
        System.out.println("chatName in addUser in WebSocketController: " + chatMessage.getChatName());

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());

        return chatMessage;
    }

}