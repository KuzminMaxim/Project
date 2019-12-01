package com.example.controller;

import com.example.api.MyApi;
import com.example.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/{chatName}.sendMessage")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws NoSuchFieldException, IllegalAccessException {

        api.save(chatMessage);
        logger.info("Message has been sent");

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addUser")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        logger.info("User has been added: " + chatMessage.getSender() + " in chat: " + chatMessage.getChatName());

        return chatMessage;
    }

}